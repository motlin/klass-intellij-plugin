package com.klass.intellij.psi.impl;

import com.intellij.icons.AllIcons;
import com.intellij.icons.AllIcons.Nodes;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassAssociationEndName;
import com.klass.intellij.psi.KlassClassifierName;
import com.klass.intellij.psi.KlassDummyMultiplicity;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassEnumerationLiteral;
import com.klass.intellij.psi.KlassEnumerationPrettyName;
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
import com.klass.intellij.psi.KlassParameterName;
import com.klass.intellij.psi.KlassParameterizedProperty;
import com.klass.intellij.psi.KlassParameterizedPropertyName;
import com.klass.intellij.psi.KlassParameterizedPropertySignature;
import com.klass.intellij.psi.KlassPrimitiveType;
import com.klass.intellij.psi.KlassPrimitiveTypeProperty;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassProjectionName;
import com.klass.intellij.psi.KlassService;
import com.klass.intellij.psi.KlassServiceBlock;
import com.klass.intellij.psi.KlassServiceCriteriaClause;
import com.klass.intellij.psi.KlassServiceGroup;
import com.klass.intellij.psi.KlassUrlGroup;
import com.klass.intellij.reference.KlassAssociationEndReference;
import com.klass.intellij.reference.KlassClassifierReference;
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
import javax.swing.*;
import org.jetbrains.annotations.Nullable;

public class KlassPsiImplUtil {
  public static PsiReference getReference(KlassClassifierName klassClassifierName) {
    String classifierName = klassClassifierName.getText();
    if (classifierName == null) {
      return null;
    }

    return new KlassClassifierReference(klassClassifierName, classifierName);
  }

  public static PsiReference getReference(KlassInterfaceName klassInterfaceName) {
    String interfaceName = klassInterfaceName.getText();
    if (interfaceName == null) {
      return null;
    }

    return new KlassInterfaceReference(klassInterfaceName, interfaceName);
  }

  public static PsiReference getReference(KlassKlassName klassKlassName) {
    String className = klassKlassName.getText();
    if (className == null) {
      return null;
    }

    return new KlassKlassReference(klassKlassName, className);
  }

  public static PsiReference getReference(KlassMemberName klassMemberName) {
    String memberName = klassMemberName.getText();
    if (memberName == null) {
      return null;
    }

    return new KlassMemberReference(klassMemberName, memberName);
  }

  public static PsiReference getReference(KlassAssociationEndName klassAssociationEndName) {
    String associationEndName = klassAssociationEndName.getText();
    if (associationEndName == null) {
      return null;
    }

    return new KlassAssociationEndReference(klassAssociationEndName, associationEndName);
  }

  public static PsiReference getReference(
      KlassParameterizedPropertyName klassParameterizedPropertyName) {
    String parameterizedPropertyType = klassParameterizedPropertyName.getText();
    if (parameterizedPropertyType == null) {
      return null;
    }

    return new KlassParameterizedPropertyReference(
        klassParameterizedPropertyName, parameterizedPropertyType);
  }

  public static PsiReference getReference(KlassParameterName klassParameterName) {
    String parameterNameText = klassParameterName.getText();
    if (parameterNameText == null) {
      return null;
    }

    return new KlassParameterReference(klassParameterName, parameterNameText);
  }

  public static PsiReference getReference(KlassExpressionNativeValue klassExpressionNativeValue) {
    String expressionNativeValueText = klassExpressionNativeValue.getText();
    if (expressionNativeValueText == null) {
      return null;
    }

    return new KlassExpressionNativeValueReference(
        klassExpressionNativeValue, expressionNativeValueText);
  }

  public static PsiReference getReference(KlassPrimitiveType klassPrimitiveType) {
    String dataType = klassPrimitiveType.getText();
    if (dataType == null) {
      return null;
    }

    return new KlassDataTypeReference(klassPrimitiveType, dataType);
  }

  public static PsiReference getReference(KlassEnumerationType klassEnumerationType) {
    String enumerationType = klassEnumerationType.getText();
    if (enumerationType == null) {
      return null;
    }

    return new KlassEnumerationReference(klassEnumerationType, enumerationType);
  }

  public static PsiReference getReference(KlassDummyMultiplicity klassDummyMultiplicity) {
    return new KlassDummyMultiplicityReference(klassDummyMultiplicity);
  }

  public static PsiReference getReference(KlassProjectionName klassProjectionName) {
    String projectionName = klassProjectionName.getText();
    if (projectionName == null) {
      return null;
    }

    return new KlassProjectionReference(klassProjectionName, projectionName);
  }

  public static PsiReference getReference(KlassExpressionVariableName klassExpressionVariableName) {
    String expressionVariableName = klassExpressionVariableName.getText();
    if (expressionVariableName == null) {
      return null;
    }

    return new KlassExpressionVariableNameReference(
        klassExpressionVariableName, expressionVariableName);
  }

  public static ItemPresentation getPresentation(KlassInterface element) {
    return new KlassNamedElementItemPresentation(element, null, Nodes.Interface);
  }

  public static ItemPresentation getPresentation(KlassKlass element) {
    return new KlassNamedElementItemPresentation(element, null, AllIcons.Nodes.Class);
  }

  public static ItemPresentation getPresentation(KlassEnumeration element) {
    return new KlassNamedElementItemPresentation(element, null, AllIcons.Nodes.Enum);
  }

  public static ItemPresentation getPresentation(KlassAssociation element) {
    return new KlassNamedElementItemPresentation(element, null, Nodes.Related);
  }

  public static ItemPresentation getPresentation(KlassProjection element) {
    return new KlassNamedElementItemPresentation(
        element,
        "on " + element.getClassifierName().getNombre().getNombreText().getText(),
        AllIcons.Hierarchy.Subtypes);
  }

  public static ItemPresentation getPresentation(KlassServiceGroup element) {
    return new ItemPresentation() {
      @Nullable @Override
      public String getPresentableText() {
        return element.getKlassName().getText();
      }

      @Nullable @Override
      public String getLocationString() {
        return null;
      }

      @Nullable @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Gutter.Java9Service;
      }
    };
  }

  public static ItemPresentation getPresentation(KlassUrlGroup element) {
    return new ItemPresentation() {
      @Nullable @Override
      public String getPresentableText() {
        return element.getUrl().getText();
      }

      @Nullable @Override
      public String getLocationString() {
        return null;
      }

      @Nullable @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Gutter.Java9Service;
      }
    };
  }

  public static ItemPresentation getPresentation(KlassAssociationEnd element) {
    String locationString = element.getKlassName().getText() + element.getMultiplicity().getText();
    return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Property);
  }

  public static ItemPresentation getPresentation(KlassPrimitiveTypeProperty element) {
    boolean hasKeyProperty =
        element.getPropertyModifierList().stream()
            .anyMatch(propertyModifier -> propertyModifier.getText().equals("key"));
    String keyModifier = hasKeyProperty ? "key " : "";

    KlassOptionalMarker optionalMarker = element.getOptionalMarker();
    String optionalMarkerText = optionalMarker == null ? "" : optionalMarker.getText();

    String locationString = keyModifier + element.getPrimitiveType().getText() + optionalMarkerText;
    return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
  }

  public static ItemPresentation getPresentation(KlassEnumerationProperty element) {
    KlassOptionalMarker optionalMarker = element.getOptionalMarker();
    String optionalMarkerText = optionalMarker == null ? "" : optionalMarker.getText();
    String locationString = element.getEnumerationType().getText() + optionalMarkerText;
    return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
  }

  public static ItemPresentation getPresentation(KlassParameterizedProperty element) {
    String locationString = element.getKlassName().getText() + element.getMultiplicity().getText();
    return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
  }

  public static ItemPresentation getPresentation(KlassParameterizedPropertySignature element) {
    String locationString =
        element.getClassifierName().getText() + element.getMultiplicity().getText();
    return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
  }

  public static ItemPresentation getPresentation(KlassEnumerationLiteral element) {
    KlassEnumerationPrettyName prettyName = element.getEnumerationPrettyName();
    String locationString = prettyName != null ? prettyName.getText() : null;
    return new KlassNamedElementItemPresentation(element, locationString, AllIcons.Nodes.Field);
  }

  public static ItemPresentation getPresentation(KlassService element) {
    return new ItemPresentation() {
      @Nullable @Override
      public String getPresentableText() {
        return element.getVerb().getText();
      }

      @Nullable @Override
      public String getLocationString() {
        KlassServiceBlock serviceBlock = element.getServiceBlock();
        if (serviceBlock == null) {
          return null;
        }
        KlassServiceCriteriaClause serviceCriteriaClause =
            serviceBlock.getServiceBody().getServiceCriteriaClause();
        if (serviceCriteriaClause == null) {
          return null;
        }
        return serviceCriteriaClause.getCriteriaExpression().getText();
      }

      @Nullable @Override
      public Icon getIcon(boolean unused) {
        return AllIcons.Gutter.Java9Service;
      }
    };
  }
}
