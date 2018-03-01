package com.klass.intellij.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassFile;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassTopLevelItem;

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
            return Arrays.stream(PsiTreeUtil.getChildrenOfType(this.element, KlassTopLevelItem.class))
                    .map(KlassStructureViewElement::new)
                    .toArray(TreeElement[]::new);
        }

        if (this.element instanceof KlassKlass)
        {
            return ((KlassKlass) this.element).getPropertyList()
                    .stream()
                    .map(KlassStructureViewElement::new)
                    .toArray(TreeElement[]::new);
        }

        if (this.element instanceof KlassAssociation)
        {
            ((KlassAssociation) this.element).getAssociationEndList().toArray(new TreeElement[]{});
        }

        return EMPTY_ARRAY;
    }
}
