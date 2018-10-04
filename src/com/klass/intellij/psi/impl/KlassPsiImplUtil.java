package com.klass.intellij.psi.impl;

import javax.swing.*;

import com.intellij.icons.AllIcons;
import com.intellij.icons.AllIcons.Nodes;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassAssociationEndName;
import com.klass.intellij.psi.KlassAssociationEndSignature;
import com.klass.intellij.psi.KlassDummyMultiplicity;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassEnumerationLiteral;
import com.klass.intellij.psi.KlassEnumerationProperty;
import com.klass.intellij.psi.KlassEnumerationType;
import com.klass.intellij.psi.KlassExpressionNativeValue;
import com.klass.intellij.psi.KlassExpressionVariableName;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassInterfaceName;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassKlassName;
import com.klass.intellij.psi.KlassMemberName;
import com.klass.intellij.psi.KlassOptionalMarker;
import com.klass.intellij.psi.KlassParameterDeclaration;
import com.klass.intellij.psi.KlassParameterName;
import com.klass.intellij.psi.KlassParameterizedProperty;
import com.klass.intellij.psi.KlassParameterizedPropertyName;
import com.klass.intellij.psi.KlassParameterizedPropertySignature;
import com.klass.intellij.psi.KlassPrimitiveType;
import com.klass.intellij.psi.KlassPrimitiveTypeProperty;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassProjectionName;
import com.klass.intellij.psi.KlassService;
import com.klass.intellij.psi.KlassServiceCriteriaClause;
import com.klass.intellij.psi.KlassServiceGroup;
import com.klass.intellij.psi.KlassUrlGroup;
import com.klass.intellij.reference.KlassAssociationEndReference;
import com.klass.intellij.reference.KlassDataTypeReference;
import com.klass.intellij.reference.KlassDummyMultiplicityReference;
import com.klass.intellij.reference.KlassEnumerationReference;
import com.klass.intellij.reference.KlassExpressionNativeValueReference;
import com.klass.intellij.reference.KlassExpressionVariableNameReference;
import com.klass.intellij.reference.KlassInterfaceReference;
import com.klass.intellij.reference.KlassKlassReference;
import com.klass.intellij.reference.KlassMemberReference;
import com.klass.intellij.reference.KlassParameterReference;
import com.klass.intellij.reference.KlassParameterizedPropertyReference;
import com.klass.intellij.reference.KlassProjectionReference;
import org.jetbrains.annotations.Nullable;

public class KlassPsiImplUtil
{
    public static PsiElement setName(KlassInterface element, String newName)
    {
        ASTNode classNameNode = element.getNombre().getNode();
        if (classNameNode != null)
        {
            KlassInterface klassInterface   = KlassElementFactory.createInterface(element.getProject(), newName);
            ASTNode        newInterfaceNameNode = klassInterface.getNombre().getNode();
            element.getNode().replaceChild(classNameNode, newInterfaceNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassKlass element, String newName)
    {
        ASTNode classNameNode = element.getNombre().getNode();
        if (classNameNode != null)
        {
            KlassKlass klass            = KlassElementFactory.createClass(element.getProject(), newName);
            ASTNode    newClassNameNode = klass.getNombre().getNode();
            element.getNode().replaceChild(classNameNode, newClassNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassEnumeration element, String newName)
    {
        ASTNode classNameNode = element.getNombre().getNode();
        if (classNameNode != null)
        {
            KlassEnumeration klass = KlassElementFactory.createEnumeration(
                    element.getProject(),
                    newName);
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
            KlassAssociation association = KlassElementFactory.createAssociation(
                    element.getProject(),
                    newName);
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
            KlassProjection klass                 = KlassElementFactory.createProjection(element.getProject(), newName);
            ASTNode         newProjectionNameNode = klass.getNombre().getNode();
            element.getNode().replaceChild(classNameNode, newProjectionNameNode);
        }
        return element;
    }

    public static PsiElement setName(KlassPrimitiveTypeProperty element, String newName)
    {
        ASTNode primitivePropertyNameNode = element.getNombre().getNode();
        if (primitivePropertyNameNode != null)
        {
            KlassPrimitiveTypeProperty primitiveTypeProperty = KlassElementFactory.createPrimitiveTypeProperty(
                    element.getProject(),
                    newName);
            ASTNode newPrimitivePropertyNameNode = primitiveTypeProperty.getNombre().getNode();
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

    public static PsiElement setName(KlassParameterizedPropertySignature element, String newName)
    {
        ASTNode primitivePropertyNameNode = element.getNombre().getNode();
        if (primitivePropertyNameNode != null)
        {
            KlassParameterizedPropertySignature parameterizedPropertySignature =
                    KlassElementFactory.createParameterizedPropertySignature(element.getProject(), newName);
            ASTNode newPrimitivePropertyNameNode = parameterizedPropertySignature.getNombre().getNode();
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

    public static PsiElement setName(KlassAssociationEndSignature element, String newName)
    {
        ASTNode associationEndNameNode = element.getNombre().getNode();
        if (associationEndNameNode != null)
        {
            KlassAssociationEndSignature associationEndSignature =
                    KlassElementFactory.createAssociationEndSignature(element.getProject(), newName);
            ASTNode newAssociationEndSignatureNameNode = associationEndSignature.getNombre().getNode();
            element.getNode().replaceChild(associationEndNameNode, newAssociationEndSignatureNameNode);
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

    public static PsiElement setName(KlassEnumerationLiteral element, String newName)
    {
        ASTNode primitiveLiteralNameNode = element.getNombre().getNode();
        if (primitiveLiteralNameNode != null)
        {
            KlassEnumerationLiteral enumerationLiteral =
                    KlassElementFactory.createEnumerationLiteral(element.getProject(), newName);
            ASTNode newPrimitiveLiteralNameNode = enumerationLiteral.getNombre().getNode();
            element.getNode().replaceChild(primitiveLiteralNameNode, newPrimitiveLiteralNameNode);
        }
        return element;
    }

    public static PsiReference getReference(KlassInterfaceName klassInterfaceName)
    {
        String interfaceName = klassInterfaceName.getText();
        if (interfaceName == null)
        {
            return null;
        }

        return new KlassInterfaceReference(klassInterfaceName, interfaceName);
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

    public static PsiReference getReference(KlassMemberName klassMemberName)
    {
        String memberName = klassMemberName.getText();
        if (memberName == null)
        {
            return null;
        }

        return new KlassMemberReference(klassMemberName, memberName);
    }

    public static PsiReference getReference(KlassAssociationEndName klassAssociationEndName)
    {
        String associationEndName = klassAssociationEndName.getText();
        if (associationEndName == null)
        {
            return null;
        }

        return new KlassAssociationEndReference(klassAssociationEndName, associationEndName);
    }

    public static PsiReference getReference(KlassParameterizedPropertyName klassParameterizedPropertyName)
    {
        String parameterizedPropertyType = klassParameterizedPropertyName.getText();
        if (parameterizedPropertyType == null)
        {
            return null;
        }

        return new KlassParameterizedPropertyReference(klassParameterizedPropertyName, parameterizedPropertyType);
    }

    public static PsiReference getReference(KlassParameterName klassParameterName)
    {
        String parameterNameText = klassParameterName.getText();
        if (parameterNameText == null)
        {
            return null;
        }

        return new KlassParameterReference(klassParameterName, parameterNameText);
    }

    public static PsiReference getReference(KlassExpressionNativeValue klassExpressionNativeValue)
    {
        String expressionNativeValueText = klassExpressionNativeValue.getText();
        if (expressionNativeValueText == null)
        {
            return null;
        }

        return new KlassExpressionNativeValueReference(klassExpressionNativeValue, expressionNativeValueText);
    }

    public static PsiReference getReference(KlassPrimitiveType klassPrimitiveType)
    {
        String dataType = klassPrimitiveType.getText();
        if (dataType == null)
        {
            return null;
        }

        return new KlassDataTypeReference(klassPrimitiveType, dataType);
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

    public static ItemPresentation getPresentation(KlassInterface element)
    {
        return new KlassNamedElementItemPresentation(element, null, Nodes.Interface);
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
        return new KlassNamedElementItemPresentation(element, "on "
                + element.getKlassName().getNombre().getNombreText().getText(), AllIcons.Hierarchy.Subtypes);
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

    public static ItemPresentation getPresentation(KlassPrimitiveTypeProperty element)
    {
        boolean hasKeyProperty = element.getPropertyModifierList()
                .stream()
                .anyMatch(propertyModifier -> propertyModifier.getText().equals("key"));
        String keyModifier = hasKeyProperty ? "key " : "";

        KlassOptionalMarker optionalMarker     = element.getOptionalMarker();
        String              optionalMarkerText = optionalMarker == null ? "" : optionalMarker.getText();

        String locationString = keyModifier + element.getPrimitiveType().getText() + optionalMarkerText;
        return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
    }

    public static ItemPresentation getPresentation(KlassEnumerationProperty element)
    {
        KlassOptionalMarker optionalMarker     = element.getOptionalMarker();
        String              optionalMarkerText = optionalMarker == null ? "" : optionalMarker.getText();
        String              locationString     = element.getEnumerationType().getText() + optionalMarkerText;
        return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
    }

    public static ItemPresentation getPresentation(KlassParameterizedProperty element)
    {
        String locationString = element.getKlassName().getText()
                + element.getMultiplicity().getText();
        return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
    }

    public static ItemPresentation getPresentation(KlassParameterizedPropertySignature element)
    {
        String locationString = element.getClassifierName().getText()
                + element.getMultiplicity().getText();
        return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
    }

    public static ItemPresentation getPresentation(KlassService element)
    {
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
                KlassServiceCriteriaClause serviceCriteriaClause = element.getServiceBlock().getServiceBody().getServiceCriteriaClause();
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
