package com.klass.intellij.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassEnumerationLiteral;
import com.klass.intellij.psi.KlassEnumerationProperty;
import com.klass.intellij.psi.KlassFile;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassParameterizedProperty;
import com.klass.intellij.psi.KlassParameterizedPropertySignature;
import com.klass.intellij.psi.KlassPrimitiveTypeProperty;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassService;
import com.klass.intellij.psi.KlassServiceGroup;
import com.klass.intellij.psi.KlassTopLevelItem;
import com.klass.intellij.psi.KlassUrlGroup;
import com.klass.intellij.psi.impl.KlassPsiImplUtil;
import java.util.Arrays;
import javax.swing.*;

public class KlassStructureViewElement implements StructureViewTreeElement {
  private final PsiElement element;

  public KlassStructureViewElement(PsiElement element) {
    this.element = element;
  }

  @Override
  public Object getValue() {
    return this.element;
  }

  @Override
  public void navigate(boolean requestFocus) {
    if (this.element instanceof NavigationItem) {
      ((NavigationItem) this.element).navigate(requestFocus);
    }
  }

  @Override
  public boolean canNavigate() {
    return this.element instanceof NavigationItem && ((NavigationItem) this.element).canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return this.element instanceof NavigationItem
        && ((NavigationItem) this.element).canNavigateToSource();
  }

  @Override
  public ItemPresentation getPresentation() {
    // Grammar-Kit does not generate getPresentation() delegation to KlassPsiImplUtil
    // (purgeOldFiles deletes generated types before the util class is scanned for methods).
    // Call KlassPsiImplUtil directly for every PSI type that appears in the structure view.
    if (this.element instanceof PsiFile psiFile) {
      return new ItemPresentation() {
        @Override
        public String getPresentableText() {
          return psiFile.getName();
        }

        @Override
        public Icon getIcon(boolean unused) {
          return psiFile.getIcon(0);
        }
      };
    }
    if (this.element instanceof KlassInterface klassInterface) {
      return KlassPsiImplUtil.getPresentation(klassInterface);
    }
    if (this.element instanceof KlassKlass klassKlass) {
      return KlassPsiImplUtil.getPresentation(klassKlass);
    }
    if (this.element instanceof KlassEnumeration klassEnumeration) {
      return KlassPsiImplUtil.getPresentation(klassEnumeration);
    }
    if (this.element instanceof KlassAssociation klassAssociation) {
      return KlassPsiImplUtil.getPresentation(klassAssociation);
    }
    if (this.element instanceof KlassProjection klassProjection) {
      return KlassPsiImplUtil.getPresentation(klassProjection);
    }
    if (this.element instanceof KlassServiceGroup klassServiceGroup) {
      return KlassPsiImplUtil.getPresentation(klassServiceGroup);
    }
    if (this.element instanceof KlassUrlGroup klassUrlGroup) {
      return KlassPsiImplUtil.getPresentation(klassUrlGroup);
    }
    if (this.element instanceof KlassPrimitiveTypeProperty primitiveTypeProperty) {
      return KlassPsiImplUtil.getPresentation(primitiveTypeProperty);
    }
    if (this.element instanceof KlassEnumerationProperty enumerationProperty) {
      return KlassPsiImplUtil.getPresentation(enumerationProperty);
    }
    if (this.element instanceof KlassParameterizedProperty parameterizedProperty) {
      return KlassPsiImplUtil.getPresentation(parameterizedProperty);
    }
    if (this.element
        instanceof KlassParameterizedPropertySignature parameterizedPropertySignature) {
      return KlassPsiImplUtil.getPresentation(parameterizedPropertySignature);
    }
    if (this.element instanceof KlassAssociationEnd associationEnd) {
      return KlassPsiImplUtil.getPresentation(associationEnd);
    }
    if (this.element instanceof KlassEnumerationLiteral enumerationLiteral) {
      return KlassPsiImplUtil.getPresentation(enumerationLiteral);
    }
    if (this.element instanceof KlassService klassService) {
      return KlassPsiImplUtil.getPresentation(klassService);
    }
    if (this.element instanceof NavigationItem navigationItem) {
      return navigationItem.getPresentation();
    }
    return null;
  }

  @Override
  public TreeElement[] getChildren() {
    if (this.element instanceof KlassFile) {
      KlassTopLevelItem[] childrenOfType =
          PsiTreeUtil.getChildrenOfType(this.element, KlassTopLevelItem.class);
      if (childrenOfType == null) {
        return EMPTY_ARRAY;
      }
      return Arrays.stream(childrenOfType)
          .map(KlassStructureViewElement::new)
          .toArray(TreeElement[]::new);
    }

    if (this.element instanceof KlassInterface klassInterface) {
      return klassInterface.getInterfaceBlock().getInterfaceBody().getMemberList().stream()
          .map(KlassStructureViewElement::new)
          .toArray(TreeElement[]::new);
    }

    if (this.element instanceof KlassKlass klassKlass) {
      return klassKlass.getClassBlock().getClassBody().getMemberList().stream()
          .map(KlassStructureViewElement::new)
          .toArray(TreeElement[]::new);
    }

    if (this.element instanceof KlassEnumeration klassEnumeration) {
      return klassEnumeration
          .getEnumerationBlock()
          .getEnumerationBody()
          .getEnumerationLiteralList()
          .stream()
          .map(KlassStructureViewElement::new)
          .toArray(TreeElement[]::new);
    }

    if (this.element instanceof KlassAssociation klassAssociation) {
      return klassAssociation
          .getAssociationBlock()
          .getAssociationBody()
          .getAssociationEndList()
          .stream()
          .map(KlassStructureViewElement::new)
          .toArray(TreeElement[]::new);
    }

    if (this.element instanceof KlassServiceGroup klassServiceGroup) {
      return klassServiceGroup
          .getServiceGroupBlock()
          .getServiceGroupBody()
          .getUrlGroupList()
          .stream()
          .map(KlassStructureViewElement::new)
          .toArray(TreeElement[]::new);
    }

    if (this.element instanceof KlassUrlGroup klassUrlGroup) {
      return klassUrlGroup.getServiceList().stream()
          .map(KlassStructureViewElement::new)
          .toArray(TreeElement[]::new);
    }

    return EMPTY_ARRAY;
  }
}
