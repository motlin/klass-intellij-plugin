package com.klass.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.klass.intellij.psi.KlassTypedElement;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractKlassInnerProjectionNode extends ASTWrapperPsiElement implements KlassTypedElement
{
    public AbstractKlassInnerProjectionNode(@NotNull ASTNode node)
    {
        super(node);
    }

    @NotNull
    @Override
    public PsiElement getType()
    {
        System.out.println("AbstractKlassInnerProjectionNode.getType");
        return null;
    }
}
