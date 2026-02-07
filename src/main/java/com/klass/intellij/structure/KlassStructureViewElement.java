package com.klass.intellij.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassFile;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassServiceGroup;
import com.klass.intellij.psi.KlassTopLevelItem;
import com.klass.intellij.psi.KlassUrlGroup;
import com.klass.intellij.psi.impl.KlassPsiImplUtil;
import java.util.Arrays;

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
    // Grammar-Kit does not generate getPresentation() delegation to KlassPsiImplUtil,
    // so we call KlassPsiImplUtil directly for each PSI element type.
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
    if (this.element instanceof KlassServiceGroup klassServiceGroup) {
      return KlassPsiImplUtil.getPresentation(klassServiceGroup);
    }
    if (this.element instanceof KlassUrlGroup klassUrlGroup) {
      return KlassPsiImplUtil.getPresentation(klassUrlGroup);
    }
    if (this.element instanceof NavigationItem) {
      return ((NavigationItem) this.element).getPresentation();
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
