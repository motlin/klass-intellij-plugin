package com.klass.intellij.structure;

import java.util.Arrays;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassFile;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassServiceGroup;
import com.klass.intellij.psi.KlassTopLevelItem;
import com.klass.intellij.psi.KlassUrlGroup;

public class KlassStructureViewElement implements StructureViewTreeElement
{
    private final PsiElement element;

    public KlassStructureViewElement(PsiElement element)
    {
        this.element = element;
    }

    @Override
    public Object getValue()
    {
        return this.element;
    }

    @Override
    public void navigate(boolean requestFocus)
    {
        if (this.element instanceof NavigationItem)
        {
            ((NavigationItem) this.element).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate()
    {
        return this.element instanceof NavigationItem
                && ((NavigationItem) this.element).canNavigate();
    }

    @Override
    public boolean canNavigateToSource()
    {
        return this.element instanceof NavigationItem
                && ((NavigationItem) this.element).canNavigateToSource();
    }

    @Override
    public ItemPresentation getPresentation()
    {
        return this.element instanceof NavigationItem
                ? ((NavigationItem) this.element).getPresentation()
                : null;
    }

    @Override
    public TreeElement[] getChildren()
    {
        if (this.element instanceof KlassFile)
        {
            KlassTopLevelItem[] childrenOfType = PsiTreeUtil.getChildrenOfType(this.element, KlassTopLevelItem.class);
            if (childrenOfType == null)
            {
                return EMPTY_ARRAY;
            }
            return Arrays.stream(childrenOfType)
                    .map(KlassStructureViewElement::new)
                    .toArray(TreeElement[]::new);
        }

        if (this.element instanceof KlassInterface)
        {
            return ((KlassInterface) this.element)
                    .getInterfaceBlock()
                    .getInterfaceBody()
                    .getMemberList()
                    .stream()
                    .map(KlassStructureViewElement::new)
                    .toArray(TreeElement[]::new);
        }

        if (this.element instanceof KlassKlass)
        {
            return ((KlassKlass) this.element)
                    .getClassBlock()
                    .getClassBody()
                    .getMemberList()
                    .stream()
                    .map(KlassStructureViewElement::new)
                    .toArray(TreeElement[]::new);
        }

        if (this.element instanceof KlassAssociation)
        {
            return ((KlassAssociation) this.element)
                    .getAssociationBlock()
                    .getAssociationBody()
                    .getAssociationEndList()
                    .stream()
                    .map(KlassStructureViewElement::new)
                    .toArray(TreeElement[]::new);
        }

        if (this.element instanceof KlassServiceGroup)
        {
            return ((KlassServiceGroup) this.element)
                    .getServiceGroupBlock()
                    .getServiceGroupBody()
                    .getUrlGroupList()
                    .stream()
                    .map(KlassStructureViewElement::new)
                    .toArray(TreeElement[]::new);
        }

        if (this.element instanceof KlassUrlGroup)
        {
            return ((KlassUrlGroup) this.element).getServiceList()
                    .stream()
                    .map(KlassStructureViewElement::new)
                    .toArray(TreeElement[]::new);
        }

        return EMPTY_ARRAY;
    }
}
