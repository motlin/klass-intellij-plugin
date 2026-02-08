package com.klass.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import com.klass.intellij.psi.KlassNombre;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class KlassNamedElementImpl extends ASTWrapperPsiElement {
  public KlassNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull abstract KlassNombre getNombre();

  @Override
  public int getTextOffset() {
    return this.getNameIdentifier().getTextOffset();
  }

  @Override
  public String getName() {
    return this.getNombre().getText();
  }

  @Nullable public PsiElement getNameIdentifier() {
    return this.getNombre();
  }

  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    KlassNombre nombre = getNombre();
    if (nombre != null) {
      PsiElement nombreText = nombre.getNombreText();
      if (nombreText != null) {
        PsiElement identifier = nombreText.getFirstChild();
        if (identifier instanceof LeafPsiElement) {
          ((LeafPsiElement) identifier).replaceWithText(name);
        }
      }
    }
    return this;
  }
}
