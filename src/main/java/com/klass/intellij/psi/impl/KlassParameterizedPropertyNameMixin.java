package com.klass.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassParameterizedPropertyName;
import org.jetbrains.annotations.NotNull;

public abstract class KlassParameterizedPropertyNameMixin extends ASTWrapperPsiElement
    implements KlassParameterizedPropertyName {
  public KlassParameterizedPropertyNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    return KlassPsiImplUtil.getReference(this);
  }
}
