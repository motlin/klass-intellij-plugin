package com.klass.intellij.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassPrimitiveType;
import org.jetbrains.annotations.NotNull;

public abstract class KlassPrimitiveTypeMixin extends KlassDataTypeImpl
    implements KlassPrimitiveType {
  public KlassPrimitiveTypeMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    return KlassPsiImplUtil.getReference(this);
  }
}
