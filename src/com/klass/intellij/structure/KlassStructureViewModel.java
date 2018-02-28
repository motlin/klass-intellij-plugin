package com.klass.intellij.structure;

import com.intellij.ide.structureView.StructureViewModel.ElementInfoProvider;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import com.klass.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class KlassStructureViewModel
        extends StructureViewModelBase
        implements ElementInfoProvider
{
    public KlassStructureViewModel(PsiFile psiFile)
    {
        super(psiFile, new KlassStructureViewElement(psiFile));
    }

    @Override
    @NotNull
    public Sorter[] getSorters()
    {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element)
    {
        return element instanceof KlassKlass
                || element instanceof KlassAssociation
                || element instanceof KlassEnumeration;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element)
    {
        return element instanceof KlassDataTypeProperty
                || element instanceof KlassEnumerationProperty
                || element instanceof KlassAssociationEnd
                || element instanceof KlassEnumerationLiteral;
    }
}
