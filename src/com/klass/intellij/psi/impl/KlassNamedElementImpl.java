package com.klass.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.klass.intellij.psi.KlassNombre;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class KlassNamedElementImpl extends ASTWrapperPsiElement
{
    public KlassNamedElementImpl(@NotNull ASTNode node)
    {
        super(node);
    }

    @NotNull
    abstract KlassNombre getNombre();

    @Override
    public int getTextOffset()
    {
        return this.getNameIdentifier().getTextOffset();
    }

    @Override
    public String getName()
    {
        return this.getNombre().getText();
    }

    @Nullable
    public PsiElement getNameIdentifier()
    {
        return this.getNombre();
    }
}
