package com.klass.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassInterfaceName;
import org.jetbrains.annotations.NotNull;

public abstract class KlassInterfaceNameMixin extends ASTWrapperPsiElement
    implements KlassInterfaceName {
  public KlassInterfaceNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    return KlassPsiImplUtil.getReference(this);
  }
}
