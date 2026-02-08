package com.klass.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassClassifierName;
import org.jetbrains.annotations.NotNull;

public abstract class KlassClassifierNameMixin extends ASTWrapperPsiElement
    implements KlassClassifierName {
  public KlassClassifierNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    return KlassPsiImplUtil.getReference(this);
  }
}
