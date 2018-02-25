package com.klass.intellij.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.*;
import com.klass.intellij.reference.KlassAssociationEndTypeReference;
import com.klass.intellij.reference.KlassDataTypeReference;

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

    public static String getName(KlassAssociation element)
    {
        return element.getAssociationName().getText();
    }

    public static PsiElement setName(KlassAssociation element, String newName)
    {
        ASTNode associationNameNode = element.getAssociationName().getNode();
        if (associationNameNode != null)
        {
            KlassAssociation association = KlassElementFactory.createAssociation(element.getProject(), newName);
            ASTNode newAssociationNameNode = association.getAssociationName().getNode();
            element.getNode().replaceChild(associationNameNode, newAssociationNameNode);
        }
        return element;
    }

    public static PsiElement getNameIdentifier(KlassAssociation element)
    {
        return element.getAssociationName();
    }

    public static PsiReference getReference(KlassAssociationEndType klassAssociationEndType)
    {
        String className = klassAssociationEndType.getText();
        if (className == null)
        {
            return null;
        }

        return new KlassAssociationEndTypeReference(klassAssociationEndType, className);
    }

    public static PsiReference getReference(KlassDataType klassDataType)
    {
        String dataType = klassDataType.getText();
        if (dataType == null)
        {
            return null;
        }

        return new KlassDataTypeReference(klassDataType, dataType);
    }
}
