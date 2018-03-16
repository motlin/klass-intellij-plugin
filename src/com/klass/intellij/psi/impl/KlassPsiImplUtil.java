package com.klass.intellij.psi.impl;

import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.*;
import com.klass.intellij.reference.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class KlassPsiImplUtil
{
    public static PsiElement setName(KlassKlass element, String newName)
    {
        ASTNode classNameNode = element.getNombre().getNode();
        if (classNameNode != null)
        {
            KlassKlass klass = KlassElementFactory.createClass(element.getProject(), newName);
            ASTNode newClassNameNode = klass.getNombre().getNode();
            element.getNode().replaceChild(classNameNode, newClassNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassEnumeration element, String newName)
    {
        ASTNode classNameNode = element.getNombre().getNode();
        if (classNameNode != null)
        {
            KlassEnumeration klass = KlassElementFactory.createEnumeration(element.getProject(), newName);
            ASTNode newEnumerationNameNode = klass.getNombre().getNode();
            element.getNode().replaceChild(classNameNode, newEnumerationNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassAssociation element, String newName)
    {
        ASTNode associationNameNode = element.getNombre().getNode();
        if (associationNameNode != null)
        {
            KlassAssociation association = KlassElementFactory.createAssociation(element.getProject(), newName);
            ASTNode newAssociationNameNode = association.getNombre().getNode();
            element.getNode().replaceChild(associationNameNode, newAssociationNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassProjection element, String newName)
    {
        ASTNode classNameNode = element.getNombre().getNode();
        if (classNameNode != null)
        {
            KlassProjection klass = KlassElementFactory.createProjection(element.getProject(), newName);
            ASTNode newProjectionNameNode = klass.getNombre().getNode();
            element.getNode().replaceChild(classNameNode, newProjectionNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassDataTypeProperty element, String newName)
    {
        ASTNode primitivePropertyNameNode = element.getNombre().getNode();
        if (primitivePropertyNameNode != null)
        {
            KlassDataTypeProperty dataTypeProperty =
                    KlassElementFactory.createDataTypeProperty(element.getProject(), newName);
            ASTNode newPrimitivePropertyNameNode = dataTypeProperty.getNombre().getNode();
            element.getNode().replaceChild(primitivePropertyNameNode, newPrimitivePropertyNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassEnumerationProperty element, String newName)
    {
        ASTNode primitivePropertyNameNode = element.getNombre().getNode();
        if (primitivePropertyNameNode != null)
        {
            KlassEnumerationProperty enumerationProperty =
                    KlassElementFactory.createEnumerationProperty(element.getProject(), newName);
            ASTNode newPrimitivePropertyNameNode = enumerationProperty.getNombre().getNode();
            element.getNode().replaceChild(primitivePropertyNameNode, newPrimitivePropertyNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassParameterizedProperty element, String newName)
    {
        ASTNode primitivePropertyNameNode = element.getNombre().getNode();
        if (primitivePropertyNameNode != null)
        {
            KlassParameterizedProperty parameterizedProperty =
                    KlassElementFactory.createParameterizedProperty(element.getProject(), newName);
            ASTNode newPrimitivePropertyNameNode = parameterizedProperty.getNombre().getNode();
            element.getNode().replaceChild(primitivePropertyNameNode, newPrimitivePropertyNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassAssociationEnd element, String newName)
    {
        ASTNode associationEndNameNode = element.getNombre().getNode();
        if (associationEndNameNode != null)
        {
            KlassAssociationEnd associationEnd =
                    KlassElementFactory.createAssociationEnd(element.getProject(), newName);
            ASTNode newAssociationEndNameNode = associationEnd.getNombre().getNode();
            element.getNode().replaceChild(associationEndNameNode, newAssociationEndNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassPathParameter element, String newName)
    {
        ASTNode classNameNode = element.getNombre().getNode();
        if (classNameNode != null)
        {
            KlassPathParameter klass = KlassElementFactory.createPathParameter(element.getProject(), newName);
            ASTNode newPathParameterNameNode = klass.getNombre().getNode();
            element.getNode().replaceChild(classNameNode, newPathParameterNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassParameterDeclaration element, String newName)
    {
        ASTNode classNameNode = element.getNombre().getNode();
        if (classNameNode != null)
        {
            KlassParameterDeclaration klass =
                    KlassElementFactory.createParameterDeclaration(element.getProject(), newName);
            ASTNode newPathParameterNameNode = klass.getNombre().getNode();
            element.getNode().replaceChild(classNameNode, newPathParameterNameNode);
        }
        return element;
    }

    public static PsiReference getReference(KlassKlassName klassKlassName)
    {
        String className = klassKlassName.getText();
        if (className == null)
        {
            return null;
        }

        return new KlassKlassReference(klassKlassName, className);
    }

    public static PsiReference getReference(KlassPropertyName klassPropertyName)
    {
        String associationEndType = klassPropertyName.getText();
        if (associationEndType == null)
        {
            return null;
        }

        return new KlassMemberReference(klassPropertyName, associationEndType);
    }

    public static PsiReference getReference(KlassAssociationEndName klassAssociationEndName)
    {
        String associationEndType = klassAssociationEndName.getText();
        if (associationEndType == null)
        {
            return null;
        }

        return new KlassAssociationEndReference(klassAssociationEndName, associationEndType);
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

    public static PsiReference getReference(KlassEnumerationType klassEnumerationType)
    {
        String enumerationType = klassEnumerationType.getText();
        if (enumerationType == null)
        {
            return null;
        }

        return new KlassEnumerationReference(klassEnumerationType, enumerationType);
    }

    public static PsiReference getReference(KlassDummyMultiplicity klassDummyMultiplicity)
    {
        return new KlassDummyMultiplicityReference(klassDummyMultiplicity);
    }

    public static PsiReference getReference(KlassProjectionName klassProjectionName)
    {
        String projectionName = klassProjectionName.getText();
        if (projectionName == null)
        {
            return null;
        }

        return new KlassProjectionReference(klassProjectionName, projectionName);
    }

    public static PsiReference getReference(KlassExpressionVariableName klassExpressionVariableName)
    {
        String expressionVariableName = klassExpressionVariableName.getText();
        if (expressionVariableName == null)
        {
            return null;
        }

        return new KlassExpressionVariableNameReference(klassExpressionVariableName, expressionVariableName);
    }

    public static ItemPresentation getPresentation(KlassKlass element)
    {
        return new KlassNamedElementItemPresentation(element, null, AllIcons.Nodes.Class);
    }

    public static ItemPresentation getPresentation(KlassEnumeration element)
    {
        return new KlassNamedElementItemPresentation(element, null, AllIcons.Nodes.Enum);
    }

    public static ItemPresentation getPresentation(KlassAssociation element)
    {
        return new KlassNamedElementItemPresentation(element, null, AllIcons.Javaee.PersistenceRelationship);
    }

    public static ItemPresentation getPresentation(KlassProjection element)
    {
        return new KlassNamedElementItemPresentation(element, null, AllIcons.Hierarchy.Subtypes);
    }

    public static ItemPresentation getPresentation(KlassServiceGroup element)
    {
        return new ItemPresentation()
        {
            @Nullable
            @Override
            public String getPresentableText()
            {
                return element.getKlassName().getText();
            }

            @Nullable
            @Override
            public String getLocationString()
            {
                return null;
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused)
            {
                return AllIcons.Gutter.Java9Service;
            }
        };
    }

    public static ItemPresentation getPresentation(KlassUrlGroup element)
    {
        return new ItemPresentation()
        {
            @Nullable
            @Override
            public String getPresentableText()
            {
                return element.getUrl().getText();
            }

            @Nullable
            @Override
            public String getLocationString()
            {
                return null;
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused)
            {
                return AllIcons.Gutter.Java9Service;
            }
        };
    }

    public static ItemPresentation getPresentation(KlassAssociationEnd element)
    {
        String locationString = element.getKlassName().getText()
                + element.getMultiplicity().getText();
        return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Property);
    }

    public static ItemPresentation getPresentation(KlassDataTypeProperty element)
    {
        KlassOptionalMarker optionalMarker = element.getOptionalMarker();
        String optionalMarkerText = optionalMarker == null ? "" : optionalMarker.getText();
        String locationString = element.getDataType().getText() + optionalMarkerText;
        return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
    }

    public static ItemPresentation getPresentation(KlassEnumerationProperty element)
    {
        KlassOptionalMarker optionalMarker = element.getOptionalMarker();
        String optionalMarkerText = optionalMarker == null ? "" : optionalMarker.getText();
        String locationString = element.getEnumerationType().getText() + optionalMarkerText;
        return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
    }

    public static ItemPresentation getPresentation(KlassParameterizedProperty element)
    {
        String locationString = element.getKlassName().getText()
                + element.getMultiplicity().getText();
        return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
    }

    public static ItemPresentation getPresentation(KlassService element)
    {
        String locationString = element.getVerb().getText();
        return new ItemPresentation()
        {
            @Nullable
            @Override
            public String getPresentableText()
            {
                return element.getVerb().getText();
            }

            @Nullable
            @Override
            public String getLocationString()
            {
                KlassServiceCriteriaClause serviceCriteriaClause = element.getServiceCriteriaClause();
                if (serviceCriteriaClause == null)
                {
                    return null;
                }
                return serviceCriteriaClause.getCriteriaExpression().getText();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused)
            {
                return AllIcons.Gutter.Java9Service;
            }
        };
    }
}
