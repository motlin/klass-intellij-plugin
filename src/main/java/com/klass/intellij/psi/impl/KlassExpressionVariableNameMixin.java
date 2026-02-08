package com.klass.intellij.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassExpressionVariableName;
import org.jetbrains.annotations.NotNull;

public abstract class KlassExpressionVariableNameMixin extends KlassExpressionValueImpl
    implements KlassExpressionVariableName {
  public KlassExpressionVariableNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    return KlassPsiImplUtil.getReference(this);
  }
}
