package com.klass.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassParameterName;
import org.jetbrains.annotations.NotNull;

public abstract class KlassParameterNameMixin extends ASTWrapperPsiElement
    implements KlassParameterName {
  public KlassParameterNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    return KlassPsiImplUtil.getReference(this);
  }
}
