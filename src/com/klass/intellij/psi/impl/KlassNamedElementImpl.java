package com.klass.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.klass.intellij.psi.KlassNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class KlassNamedElementImpl extends ASTWrapperPsiElement implements KlassNamedElement
{
    public KlassNamedElementImpl(@NotNull ASTNode node)
    {
        super(node);
    }
}
