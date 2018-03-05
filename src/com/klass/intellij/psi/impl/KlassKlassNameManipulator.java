package com.klass.intellij.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassKlassName;
import org.jetbrains.annotations.NotNull;

public class KlassKlassNameManipulator implements ElementManipulator
{
    @Override
    public PsiElement handleContentChange(
            @NotNull PsiElement element,
            @NotNull TextRange range,
            String newContent) throws IncorrectOperationException
    {
        ASTNode associationEndTypeNode = element.getNode();
        if (associationEndTypeNode != null)
        {
            KlassKlassName associationEndType =
                    KlassElementFactory.createAssociationEndType(element.getProject(), newContent);
            ASTNode newAssociationEndTypeNode = associationEndType.getNode();
            element.getParent().getNode().replaceChild(associationEndTypeNode, newAssociationEndTypeNode);
        }
        return element;
    }

    @Override
    public PsiElement handleContentChange(
            @NotNull PsiElement element, String newContent) throws IncorrectOperationException
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".handleContentChange() not implemented yet");
    }

    @NotNull
    @Override
    public TextRange getRangeInElement(@NotNull PsiElement element)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getRangeInElement() not implemented yet");
    }
}
