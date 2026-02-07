package com.klass.intellij.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassEnumerationType;
import org.jetbrains.annotations.NotNull;

public abstract class KlassEnumerationTypeMixin extends KlassDataTypeImpl
    implements KlassEnumerationType {
  public KlassEnumerationTypeMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    return KlassPsiImplUtil.getReference(this);
  }
}
