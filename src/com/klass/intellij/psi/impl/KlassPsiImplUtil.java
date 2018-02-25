package com.klass.intellij.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassAssociationEndType;
import com.klass.intellij.psi.KlassClass;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassTypes;
import com.klass.intellij.reference.KlassClassReference;

public class KlassPsiImplUtil
{
    public static String getName(KlassClass element)
    {
        return element.getClassName().getText();
    }

    public static PsiElement setName(KlassClass element, String newName)
    {
        ASTNode classNameNode = element.getClassName().getNode();
        if (classNameNode != null)
        {
            KlassClass klass = KlassElementFactory.createClass(element.getProject(), newName);
            ASTNode newClassNameNode = klass.getClassName().getNode();
            element.getNode().replaceChild(classNameNode, newClassNameNode);
        }
        return element;
    }

    public static PsiElement getNameIdentifier(KlassClass element)
    {
        return element.getClassName();
    }

    public static String getSourceName(KlassAssociationImpl element)
    {
        ASTNode keyNode = element
                .getNode()
                .findChildByType(KlassTypes.SOURCE_ASSOCIATION_END)
                .findChildByType(KlassTypes.ASSOCIATION_END_NAME);

        if (keyNode == null)
        {
            return null;
        }

        // TODO: Delete
        return keyNode.getText().replaceAll("\\\\ ", " ");
    }

    public static PsiReference getReference(KlassAssociationEndType klassAssociationEndType)
    {
        String className = klassAssociationEndType.getText();
        if (className == null)
        {
            return null;
        }

        return new KlassClassReference(klassAssociationEndType, className);
    }
}
