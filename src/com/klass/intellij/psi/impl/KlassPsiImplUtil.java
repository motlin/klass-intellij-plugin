package com.klass.intellij.psi.impl;

import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.*;
import com.klass.intellij.reference.KlassAssociationEndTypeReference;
import com.klass.intellij.reference.KlassDataTypeReference;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.swing.*;

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

    public static String getName(KlassDataTypeProperty element)
    {
        return element.getDataTypePropertyName().getText();
    }

    public static PsiElement setName(KlassDataTypeProperty element, String newName)
    {
        ASTNode dataTypePropertyNameNode = element.getDataTypePropertyName().getNode();
        if (dataTypePropertyNameNode != null)
        {
            KlassDataTypeProperty dataTypeProperty =
                    KlassElementFactory.createDataTypeProperty(element.getProject(), newName);
            ASTNode newDataTypePropertyNameNode = dataTypeProperty.getDataTypePropertyName().getNode();
            element.getNode().replaceChild(dataTypePropertyNameNode, newDataTypePropertyNameNode);
        }
        return element;
    }

    public static PsiElement getNameIdentifier(KlassDataTypeProperty element)
    {
        return element.getDataTypePropertyName();
    }

    public static String getName(KlassAssociationEnd element)
    {
        return element.getAssociationEndName().getText();
    }

    public static PsiElement setName(KlassAssociationEnd element, String newName)
    {
        ASTNode associationEndNameNode = element.getAssociationEndName().getNode();
        if (associationEndNameNode != null)
        {
            KlassAssociationEnd associationEnd =
                    KlassElementFactory.createAssociationEnd(element.getProject(), newName);
            ASTNode newAssociationEndNameNode = associationEnd.getAssociationEndName().getNode();
            element.getNode().replaceChild(associationEndNameNode, newAssociationEndNameNode);
        }
        return element;
    }

    public static PsiElement getNameIdentifier(KlassAssociationEnd element)
    {
        return element.getAssociationEndName();
    }

    public static PsiReference getReference(KlassAssociationEndType klassAssociationEndType)
    {
        String associationEndType = klassAssociationEndType.getText();
        if (associationEndType == null)
        {
            return null;
        }

        return new KlassAssociationEndTypeReference(klassAssociationEndType, associationEndType);
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

    public static ItemPresentation getPresentation(KlassClass element)
    {
        return new ItemPresentation()
        {
            @Nullable
            @Override
            public String getPresentableText()
            {
                return element.getClassName().getText();
            }

            @NotNull
            @Override
            public String getLocationString()
            {
                return element.getContainingFile().getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused)
            {
                return AllIcons.Nodes.Class;
            }
        };
    }

    public static ItemPresentation getPresentation(KlassAssociation element)
    {
        return new ItemPresentation()
        {
            @Nullable
            @Override
            public String getPresentableText()
            {
                return element.getAssociationName().getText();
            }

            @NotNull
            @Override
            public String getLocationString()
            {
                return element.getContainingFile().getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused)
            {
                return AllIcons.Javaee.PersistenceRelationship;
            }
        };
    }

    public static ItemPresentation getPresentation(KlassAssociationEnd element)
    {
        return new ItemPresentation()
        {
            @Nullable
            @Override
            public String getPresentableText()
            {
                return element.getAssociationEndName().getText();
            }

            @NotNull
            @Override
            public String getLocationString()
            {
                return element.getAssociationEndType().getText() + element.getMultiplicity().getText();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused)
            {
                return AllIcons.Nodes.Property;
            }
        };
    }

    public static ItemPresentation getPresentation(KlassDataTypeProperty element)
    {
        return new ItemPresentation()
        {
            @Nullable
            @Override
            public String getPresentableText()
            {
                return element.getDataTypePropertyName().getText();
            }

            @NotNull
            @Override
            public String getLocationString()
            {
                return element.getDataType().getText() + this.getOptionalMarker();
            }

            private String getOptionalMarker()
            {
                KlassOptionalMarker optionalMarker = element.getOptionalMarker();
                return optionalMarker == null ? "" : optionalMarker.getText();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused)
            {
                return AllIcons.Nodes.Field;
            }
        };
    }
}
