package com.klass.intellij.structure;

import com.intellij.ide.structureView.StructureViewModel.ElementInfoProvider;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassEnumerationLiteral;
import com.klass.intellij.psi.KlassEnumerationProperty;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassMember;
import com.klass.intellij.psi.KlassParameterizedProperty;
import com.klass.intellij.psi.KlassParameterizedPropertySignature;
import com.klass.intellij.psi.KlassPrimitiveTypeProperty;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassService;
import com.klass.intellij.psi.KlassServiceGroup;
import com.klass.intellij.psi.KlassTopLevelItem;
import com.klass.intellij.psi.KlassUrlGroup;
import org.jetbrains.annotations.NotNull;

public class KlassStructureViewModel extends StructureViewModelBase implements ElementInfoProvider {
  public KlassStructureViewModel(PsiFile psiFile) {
    super(psiFile, new KlassStructureViewElement(psiFile));
  }

  @Override
  @NotNull public Sorter[] getSorters() {
    return new Sorter[] {};
  }

  @Override
  protected Class<?> @NotNull [] getSuitableClasses() {
    return new Class[] {
      KlassTopLevelItem.class,
      KlassInterface.class,
      KlassKlass.class,
      KlassEnumeration.class,
      KlassAssociation.class,
      KlassProjection.class,
      KlassServiceGroup.class,
      KlassMember.class,
      KlassAssociationEnd.class,
      KlassEnumerationLiteral.class,
      KlassUrlGroup.class,
      KlassService.class,
    };
  }

  @Override
  public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
    Object value = element.getValue();
    return value instanceof KlassKlass
        || value instanceof KlassInterface
        || value instanceof KlassAssociation
        || value instanceof KlassEnumeration
        || value instanceof KlassServiceGroup
        || value instanceof KlassUrlGroup;
  }

  @Override
  public boolean isAlwaysLeaf(StructureViewTreeElement element) {
    Object value = element.getValue();
    return value instanceof KlassPrimitiveTypeProperty
        || value instanceof KlassEnumerationProperty
        || value instanceof KlassParameterizedProperty
        || value instanceof KlassParameterizedPropertySignature
        || value instanceof KlassAssociationEnd
        || value instanceof KlassEnumerationLiteral
        || value instanceof KlassService;
  }
}
