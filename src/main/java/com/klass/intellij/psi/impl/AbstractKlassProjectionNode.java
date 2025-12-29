package com.klass.intellij.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassProjectionNode;
import com.klass.intellij.psi.KlassTypedElement;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractKlassProjectionNode extends KlassNombredImpl
    implements KlassTypedElement, KlassProjectionNode {
  public AbstractKlassProjectionNode(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull @Override
  public PsiElement getType() {
    if (this instanceof KlassProjection) {
      return ((KlassProjection) this).getKlassName();
    }

    throw new AssertionError(this);
  }
}
