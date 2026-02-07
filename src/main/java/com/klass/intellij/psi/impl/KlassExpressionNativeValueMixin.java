package com.klass.intellij.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassExpressionNativeValue;
import org.jetbrains.annotations.NotNull;

public abstract class KlassExpressionNativeValueMixin extends KlassExpressionValueImpl
    implements KlassExpressionNativeValue {
  public KlassExpressionNativeValueMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    return KlassPsiImplUtil.getReference(this);
  }
}
