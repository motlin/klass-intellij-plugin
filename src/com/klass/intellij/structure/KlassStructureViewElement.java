package com.klass.intellij.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.psi.*;

import java.util.Arrays;

public class KlassStructureViewElement implements StructureViewTreeElement, SortableTreeElement
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
    public String getAlphaSortKey()
    {
        return this.element instanceof PsiNamedElement
                ? ((PsiNamedElement) this.element).getName()
                : null;
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

        if (this.element instanceof KlassKlass)
        {
            return ((KlassKlass) this.element).getMemberList()
                    .stream()
                    .map(KlassStructureViewElement::new)
                    .toArray(TreeElement[]::new);
        }

        if (this.element instanceof KlassAssociation)
        {
            return ((KlassAssociation) this.element).getAssociationEndList()
                    .stream()
                    .map(KlassStructureViewElement::new)
                    .toArray(TreeElement[]::new);
        }

        if (this.element instanceof KlassServiceGroup)
        {
            return ((KlassServiceGroup) this.element).getUrlGroupList()
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
