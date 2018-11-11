package com.klass.intellij.highlighter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.highlighter.type.DataTypeType;
import com.klass.intellij.highlighter.type.Multiplicity;
import com.klass.intellij.highlighter.type.Type;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassAssociationEndName;
import com.klass.intellij.psi.KlassBooleanLiteral;
import com.klass.intellij.psi.KlassClassModifier;
import com.klass.intellij.psi.KlassCriteriaOperator;
import com.klass.intellij.psi.KlassDataType;
import com.klass.intellij.psi.KlassDataTypeDeclaration;
import com.klass.intellij.psi.KlassDummyMultiplicity;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassEnumerationLiteral;
import com.klass.intellij.psi.KlassEnumerationProperty;
import com.klass.intellij.psi.KlassEnumerationType;
import com.klass.intellij.psi.KlassExpressionLiteral;
import com.klass.intellij.psi.KlassExpressionLiterals;
import com.klass.intellij.psi.KlassExpressionLiteralsParens;
import com.klass.intellij.psi.KlassExpressionMemberName;
import com.klass.intellij.psi.KlassExpressionNativeValue;
import com.klass.intellij.psi.KlassExpressionValue;
import com.klass.intellij.psi.KlassExpressionVariableName;
import com.klass.intellij.psi.KlassFloatLiteralNode;
import com.klass.intellij.psi.KlassIntegerLiteralNode;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassInterfaceName;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassKlassName;
import com.klass.intellij.psi.KlassLowerBound;
import com.klass.intellij.psi.KlassMember;
import com.klass.intellij.psi.KlassMemberName;
import com.klass.intellij.psi.KlassMultiplicity;
import com.klass.intellij.psi.KlassNombreText;
import com.klass.intellij.psi.KlassOperator;
import com.klass.intellij.psi.KlassOptionalMarker;
import com.klass.intellij.psi.KlassParameterDeclaration;
import com.klass.intellij.psi.KlassParameterDeclarations;
import com.klass.intellij.psi.KlassParameterDeclarationsParens;
import com.klass.intellij.psi.KlassParameterName;
import com.klass.intellij.psi.KlassParameterNames;
import com.klass.intellij.psi.KlassParameterizedProperty;
import com.klass.intellij.psi.KlassParameterizedPropertyName;
import com.klass.intellij.psi.KlassPrimitiveType;
import com.klass.intellij.psi.KlassPrimitiveTypeProperty;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassProjectionName;
import com.klass.intellij.psi.KlassProjectionParameterizedPropertyNode;
import com.klass.intellij.psi.KlassRelationship;
import com.klass.intellij.psi.KlassServiceProjectionClause;
import com.klass.intellij.psi.KlassStringLiteralNode;
import com.klass.intellij.psi.KlassUpperBound;
import com.klass.intellij.psi.KlassUrlConstant;
import com.klass.intellij.psi.KlassVerb;
import com.klass.intellij.psi.KlassVisitor;
import com.klass.intellij.reference.KlassMemberReference;
import com.klass.intellij.reference.KlassParameterReference;
import com.klass.intellij.reference.KlassParameterizedPropertyReference;
import com.klass.intellij.reference.KlassProjectionReference;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnnotatorKlassVisitor extends KlassVisitor
{
    public static final ImmutableList<String> TEMPORAL_PROPERTY_MODIFIERS =
            Lists.immutable.with("validTemporal", "systemTemporal", "bitemporal");

    private final AnnotationHolder annotationHolder;

    public AnnotatorKlassVisitor(AnnotationHolder annotationHolder)
    {
        this.annotationHolder = annotationHolder;
    }

    @Override
    public void visitAssociation(@NotNull KlassAssociation klassAssociation)
    {
        List<KlassAssociationEnd> associationEndList = klassAssociation.getAssociationBlock().getAssociationBody().getAssociationEndList();
        int                       size               = associationEndList.size();
        if (size == 0)
        {
            String message = "Expected association ends.";
            this.annotationHolder.createErrorAnnotation(klassAssociation, message);
        }
        else if (size == 1)
        {
            String message = "Expected two association ends.";
            this.annotationHolder.createErrorAnnotation(associationEndList.get(0), message);
        }
        else if (size > 2)
        {
            String message = "Expected two association ends.";
            this.annotationHolder.createErrorAnnotation(associationEndList.get(2), message);
        }
    }

    @Override
    public void visitAssociationEnd(@NotNull KlassAssociationEnd klassAssociationEnd)
    {
        if (klassAssociationEnd.getMultiplicity() == null)
        {
            this.annotationHolder.createErrorAnnotation(klassAssociationEnd, "Expected multiplicity");
        }
    }

    @Override
    public void visitCriteriaOperator(@NotNull KlassCriteriaOperator criteriaOperator)
    {
        KlassExpressionValue sourceExpressionValue =
                criteriaOperator.getSourceExpressionValue().getExpressionValue();
        KlassExpressionValue targetExpressionValue =
                criteriaOperator.getTargetExpressionValue().getExpressionValue();

        List<Type> possibleSourceTypes = this.getPossibleTypes(sourceExpressionValue);
        List<Type> possibleTargetTypes = this.getPossibleTypes(targetExpressionValue);
        if (!Type.compatible(possibleSourceTypes, possibleTargetTypes))
        {
            String message = String.format(
                    "Incompatible types: '%s' and '%s' in '%s'.",
                    possibleSourceTypes.get(0),
                    possibleTargetTypes.get(0),
                    criteriaOperator.getText());
            this.annotationHolder.createErrorAnnotation(
                    criteriaOperator,
                    message);
        }

        if (!possibleSourceTypes.isEmpty()
                && ListAdapter.adapt(possibleSourceTypes).allSatisfy(type -> type.getMultiplicity().isToMany()))
        {
            String message = String.format(
                    "Invalid multiplicity '%s'.",
                    possibleSourceTypes.get(0));
            this.annotationHolder.createErrorAnnotation(
                    criteriaOperator,
                    message);
        }
        KlassOperator operator   = criteriaOperator.getOperator();
        boolean       expectMany = operator.getText().equals("in");
        boolean       actualMany = ListAdapter.adapt(possibleTargetTypes).allSatisfy(type -> type.getMultiplicity().isToMany());
        if (!possibleTargetTypes.isEmpty() && actualMany != expectMany)
        {
            String message = String.format(
                    "Invalid multiplicity '%s'.",
                    possibleTargetTypes.get(0));
            this.annotationHolder.createErrorAnnotation(
                    criteriaOperator,
                    message);
        }
    }

    @Override
    public void visitDummyMultiplicity(@NotNull KlassDummyMultiplicity klassDummyMultiplicity)
    {
        this.annotationHolder.createErrorAnnotation(klassDummyMultiplicity, "Expected multiplicity");
    }

    @Override
    public void visitEnumerationType(@NotNull KlassEnumerationType klassEnumerationType)
    {
        this.annotateUnresolvableReference(klassEnumerationType);
    }

    private void applyClassName(PsiElement psiElement)
    {
        Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(psiElement, null);
        infoAnnotation.setTextAttributes(KlassHighlightingColors.CLASS_NAME_ATTRIBUTES);
    }

    @Override
    public void visitExpressionVariableName(@NotNull KlassExpressionVariableName expressionVariableName)
    {
        this.annotateCannotResolve(expressionVariableName);
    }

    @Override
    public void visitInterface(@NotNull KlassInterface klassInterface)
    {
        List<KlassMember> propertyList = klassInterface.getInterfaceBlock().getInterfaceBody().getMemberList();
        Map<String, Long> propertyCountByName = propertyList.stream()
                .map(PsiNamedElement::getName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (KlassMember klassMember : propertyList)
        {
            String propertyName = klassMember.getName();
            Long   occurrences  = propertyCountByName.get(propertyName);
            if (occurrences > 1)
            {
                String message = String.format("Duplicate property '%s'", propertyName);
                this.annotationHolder.createErrorAnnotation(klassMember.getNombre(), message);
            }
        }
    }

    @Override
    public void visitKlass(@NotNull KlassKlass klassKlass)
    {
        List<KlassMember> propertyList = klassKlass.getClassBlock().getClassBody().getMemberList();
        Map<String, Long> propertyCountByName = propertyList.stream()
                .map(PsiNamedElement::getName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (KlassMember klassMember : propertyList)
        {
            String propertyName = klassMember.getName();
            Long   occurrences  = propertyCountByName.get(propertyName);
            if (occurrences > 1)
            {
                String message = String.format("Duplicate property '%s'", propertyName);
                this.annotationHolder.createErrorAnnotation(klassMember.getNombre(), message);
            }
        }
    }

    @Override
    public void visitInterfaceName(@NotNull KlassInterfaceName klassInterfaceName)
    {
        this.annotateUnresolvableReference(klassInterfaceName);
    }

    @Override
    public void visitKlassName(@NotNull KlassKlassName klassKlassName)
    {
        this.annotateUnresolvableReference(klassKlassName);
    }

    @Override
    public void visitProjectionName(@NotNull KlassProjectionName klassProjectionName)
    {
        this.annotateUnresolvableReference(klassProjectionName);
    }

    private void annotateUnresolvableReference(PsiElement elementWithReference)
    {
        PsiReference reference = elementWithReference.getReference();
        if (reference != null && reference.resolve() == null)
        {
            String message = String.format("Cannot resolve symbol '%s'", elementWithReference.getText());
            this.annotationHolder.createErrorAnnotation(elementWithReference, message);
        }
        this.applyClassName(elementWithReference);
    }

    @Override
    public void visitLowerBound(@NotNull KlassLowerBound klassLowerBound)
    {
        String text = klassLowerBound.getText();
        if (!text.equals("0") && !text.equals("1"))
        {
            String message = "Expected 0 or 1 for the lower bound.";
            this.annotationHolder.createErrorAnnotation(klassLowerBound, message);
        }
    }

    @Override
    public void visitMemberName(@NotNull KlassMemberName propertyName)
    {
        KlassMemberReference memberReference = (KlassMemberReference) propertyName.getReference();
        PsiElement           resolved        = memberReference.resolve();
        if (resolved == null)
        {
            String message = String.format("Cannot resolve symbol '%s'", propertyName.getText());
            this.annotationHolder.createErrorAnnotation(
                    propertyName,
                    message);
        }
        else if (resolved instanceof KlassPrimitiveTypeProperty
                || resolved instanceof KlassEnumerationProperty
                || resolved instanceof KlassClassModifier)
        {
            Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(propertyName, null);
            infoAnnotation.setTextAttributes(KlassHighlightingColors.INSTANCE_FINAL_FIELD_ATTRIBUTES);
        }
        else if (resolved instanceof KlassEnumerationLiteral)
        {
            Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(propertyName, null);
            infoAnnotation.setTextAttributes(KlassHighlightingColors.STATIC_FINAL_FIELD_ATTRIBUTES);
        }
    }

    @Override
    public void visitNombreText(@NotNull KlassNombreText klassNombreText)
    {
        Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(klassNombreText, null);
        PsiElement parent         = klassNombreText.getParent().getParent();
        if (parent instanceof KlassMemberName)
        {
            // Handled specially by resolving reference first
        }
        else if (parent instanceof KlassEnumeration)
        {
            infoAnnotation.setTextAttributes(KlassHighlightingColors.ENUM_NAME_ATTRIBUTES);
        }
        else if (parent instanceof KlassMember
                || parent instanceof KlassAssociationEnd
                || parent instanceof KlassAssociationEndName
                || parent instanceof KlassParameterizedPropertyName)
        {
            infoAnnotation.setTextAttributes(KlassHighlightingColors.INSTANCE_FINAL_FIELD_ATTRIBUTES);
        }
        else if (parent instanceof KlassEnumerationLiteral)
        {
            infoAnnotation.setTextAttributes(KlassHighlightingColors.ENUM_LITERAL_ATTRIBUTES);
        }
        else if (parent instanceof KlassParameterDeclaration)
        {
            // infoAnnotation.setTextAttributes(KlassHighlightingColors.PATH_PARAMETER);

            infoAnnotation.setTextAttributes(KlassHighlightingColors.PARAMETER_ATTRIBUTES);
        }
        else if (parent instanceof KlassParameterName)
        {
            infoAnnotation.setTextAttributes(KlassHighlightingColors.LOCAL_VARIABLE_ATTRIBUTES);
        }
        else if (parent instanceof KlassUrlConstant)
        {
            infoAnnotation.setTextAttributes(KlassHighlightingColors.URL_CONSTANT);
        }
        else if (parent instanceof KlassExpressionVariableName)
        {
            infoAnnotation.setTextAttributes(KlassHighlightingColors.LOCAL_VARIABLE_ATTRIBUTES);
        }
        else
        {
            infoAnnotation.setTextAttributes(KlassHighlightingColors.CLASS_NAME_ATTRIBUTES);
        }
    }

    @Override
    public void visitParameterDeclaration(@NotNull KlassParameterDeclaration parameterDeclaration)
    {
        if (parameterDeclaration.getDataTypeDeclaration().getMultiplicity() == null)
        {
            String message = String.format(
                    "Expected a type declaration on parameter: '%s'.",
                    parameterDeclaration.getText());
            this.annotationHolder.createErrorAnnotation(
                    parameterDeclaration,
                    message);
        }
    }

    @Override
    public void visitParameterName(@NotNull KlassParameterName parameterName)
    {
        this.annotateCannotResolve(parameterName);
    }

    @Override
    public void visitProjectionParameterizedPropertyNode(@NotNull KlassProjectionParameterizedPropertyNode projectionParameterizedPropertyNode)
    {
        KlassParameterizedPropertyName parameterizedPropertyName =
                projectionParameterizedPropertyNode.getParameterizedPropertyName();
        KlassParameterizedPropertyReference parameterizedPropertyReference =
                (KlassParameterizedPropertyReference) parameterizedPropertyName.getReference();
        KlassParameterizedProperty parameterizedProperty =
                (KlassParameterizedProperty) parameterizedPropertyReference.resolve();

        if (parameterizedProperty == null)
        {
            return;
        }
        List<KlassParameterName> parameterNameList = projectionParameterizedPropertyNode.getParameterNamesParens().getParameterNames().getParameterNameList();

        KlassProjection projection =
                PsiTreeUtil.getParentOfType(projectionParameterizedPropertyNode, KlassProjection.class);

        MutableList<KlassParameterDeclaration> propertyParameterDeclarations =
                ListAdapter.adapt(parameterizedProperty.getPropertyParameterDeclarationsParens().getParameterDeclarations().getParameterDeclarationList());

        MutableList<KlassParameterDeclaration> projectionParameterDeclarations =
                ListAdapter.adapt(parameterNameList)
                        .collect(KlassParameterName::getReference)
                        .collect(KlassParameterReference.class::cast)
                        .collect(KlassParameterReference::resolve)
                        .collect(KlassParameterDeclaration.class::cast);

        if (parameterNameList.size() == propertyParameterDeclarations.size())
        {
            this.typeCheckPassedParameters(
                    parameterNameList,
                    propertyParameterDeclarations,
                    projectionParameterDeclarations);
        }
        else
        {
            String message = String.format(
                    "Projection '%s' cannot be applied to given types;%nrequired: '%s'%nfound:'%s'%nreason: actual and formal argument lists differ in length.",
                    parameterizedPropertyName.getText(),
                    propertyParameterDeclarations
                            .collect(KlassParameterDeclaration::getDataTypeDeclaration)
                            .collect(PsiElement::getText)
                            .makeString(),
                    projectionParameterDeclarations
                            .collect(KlassParameterDeclaration::getDataTypeDeclaration)
                            .collect(PsiElement::getText)
                            .makeString());
            this.annotationHolder.createErrorAnnotation(
                    parameterizedPropertyName,
                    message);
        }
    }

    @Override
    public void visitRelationship(@NotNull KlassRelationship klassRelationship)
    {
        DetectExpressionThisMember visitor = new DetectExpressionThisMember();
        klassRelationship.getCriteriaExpression().accept(visitor);
        if (!visitor.hasThisMember())
        {
            String message = "Expected at least one clause with a reference to 'this'.";
            this.annotationHolder.createErrorAnnotation(klassRelationship, message);
        }
    }

    @Override
    public void visitServiceProjectionClause(@NotNull KlassServiceProjectionClause projectionClause)
    {
        KlassParameterNames parameterNames = projectionClause.getParameterNames();
        List<KlassParameterName> parameterNameList = parameterNames == null
                ? Arrays.asList()
                : parameterNames.getParameterNameList();

        KlassProjectionName      projectionName      = projectionClause.getProjectionName();
        KlassProjectionReference projectionReference = (KlassProjectionReference) projectionName.getReference();
        KlassProjection          projection          = (KlassProjection) projectionReference.resolve();
        if (projection != null)
        {
            MutableList<KlassParameterDeclaration> serviceParameterDeclarations =
                    ListAdapter.adapt(parameterNameList)
                            .collect(KlassParameterName::getReference)
                            .collect(KlassParameterReference.class::cast)
                            .collect(KlassParameterReference::resolve)
                            .collect(KlassParameterDeclaration.class::cast);

            if (serviceParameterDeclarations.contains(null))
            {
                return;
            }

            MutableList<KlassParameterDeclaration> projectionParameterDeclarations =
                    Optional.ofNullable(projection.getParameterDeclarationsParens())
                            .map(KlassParameterDeclarationsParens::getParameterDeclarations)
                            .map(KlassParameterDeclarations::getParameterDeclarationList)
                            .map(ListAdapter::adapt)
                            .orElse(Lists.mutable.empty());

            if (parameterNameList.size() == projectionParameterDeclarations.size())
            {
                this.typeCheckPassedParameters(
                        parameterNameList,
                        projectionParameterDeclarations,
                        serviceParameterDeclarations);
            }
            else
            {
                String message = String.format(
                        "Projection '%s' cannot be applied to given types;%nrequired: '%s'%nfound:'%s'%nreason: actual and formal argument lists differ in length.",
                        projectionName.getText(),
                        projectionParameterDeclarations
                                .collect(KlassParameterDeclaration::getDataTypeDeclaration)
                                .collect(PsiElement::getText)
                                .makeString(),
                        serviceParameterDeclarations
                                .collect(KlassParameterDeclaration::getDataTypeDeclaration)
                                .collect(PsiElement::getText)
                                .makeString());
                this.annotationHolder.createErrorAnnotation(
                        projectionClause.getProjectionName(),
                        message);
            }
        }
    }

    @Override
    public void visitUpperBound(@NotNull KlassUpperBound klassUpperBound)
    {
        String text = klassUpperBound.getText();
        if (!text.equals("1") && !text.equals("*"))
        {
            String message = "Expected 1 or * for the upper bound.";
            this.annotationHolder.createErrorAnnotation(klassUpperBound, message);
        }
    }

    @Override
    public void visitVerb(@NotNull KlassVerb klassVerb)
    {
        Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(klassVerb, null);
        infoAnnotation.setTextAttributes(KlassHighlightingColors.VERB);
    }

    public void typeCheckPassedParameters(
            List<KlassParameterName> parameterNameList,
            MutableList<KlassParameterDeclaration> propertyParameterDeclarations,
            MutableList<KlassParameterDeclaration> projectionParameterDeclarations)
    {
        for (int i = 0; i < parameterNameList.size(); i++)
        {
            KlassParameterName        parameterName               = parameterNameList.get(i);
            KlassParameterDeclaration serviceParameterDeclaration = projectionParameterDeclarations.get(i);
            KlassParameterDeclaration projectionParameterDeclaration =
                    propertyParameterDeclarations.get(i);

            List<Type> serviceParameterTypes =
                    this.getParameterDeclarationType(serviceParameterDeclaration);
            List<Type> projectionParameterTypes =
                    this.getParameterDeclarationType(projectionParameterDeclaration);

            if (!Type.compatible(serviceParameterTypes, projectionParameterTypes))
            {
                String message = String.format(
                        "Incompatible types: '%s' cannot be converted to '%s'.",
                        serviceParameterTypes.get(0),
                        projectionParameterTypes.get(0));
                this.annotationHolder.createErrorAnnotation(
                        parameterName,
                        message);
            }
        }
    }

    public void annotateCannotResolve(@NotNull PsiElement psiElement)
    {
        PsiReference reference = psiElement.getReference();
        PsiElement   resolved  = reference.resolve();
        this.annotateCannotResolve(psiElement, resolved);
    }

    public void annotateCannotResolve(
            @NotNull PsiElement expressionVariableName,
            PsiElement resolved)
    {
        if (resolved == null)
        {
            String message = String.format("Cannot resolve symbol '%s'", expressionVariableName.getText());
            this.annotationHolder.createErrorAnnotation(
                    expressionVariableName,
                    message);
        }
    }

    private List<Type> getPossibleTypes(KlassExpressionValue expressionValue)
    {
        if (expressionValue instanceof KlassExpressionLiteral)
        {
            KlassExpressionLiteral expressionLiteral = (KlassExpressionLiteral) expressionValue;
            return AnnotatorKlassVisitor.getExpressionLiteralTypes(expressionLiteral, Multiplicity.ONE_TO_ONE);
        }

        if (expressionValue instanceof KlassExpressionLiteralsParens
                || expressionValue instanceof KlassExpressionLiterals)
        {
            KlassExpressionLiterals      expressionLiteralListNode = ((KlassExpressionLiteralsParens) expressionValue).getExpressionLiterals();
            List<KlassExpressionLiteral> expressionLiteralList     = expressionLiteralListNode.getExpressionLiteralList();
            int                          size                      = expressionLiteralList.size();
            // TODO: Test empty literal list
            return AnnotatorKlassVisitor.getExpressionLiteralTypes(
                    expressionLiteralList.get(0),
                    Multiplicity.ZERO_TO_MANY);
        }

        if (expressionValue instanceof KlassExpressionNativeValue)
        {
            KlassExpressionNativeValue expressionNativeValue = (KlassExpressionNativeValue) expressionValue;
            if (expressionNativeValue.getText().equals("user"))
            {
                return Collections.singletonList(new Type(
                        DataTypeType.PRIMITIVE_TYPE,
                        "String",
                        Multiplicity.ONE_TO_ONE));
            }
            throw new UnsupportedOperationException(expressionNativeValue.getText());
        }

        if (expressionValue instanceof KlassExpressionMemberName)
        {
            KlassExpressionMemberName expressionMemberName = (KlassExpressionMemberName) expressionValue;

            /* TODO: factor in expressionMemberName.getAssociationEndNameList() */
            KlassMemberName      propertyName = expressionMemberName.getMemberName();
            KlassMemberReference reference    = (KlassMemberReference) propertyName.getReference();
            PsiElement           resolve      = reference.resolve();
            if (resolve instanceof KlassPrimitiveTypeProperty)
            {
                KlassPrimitiveTypeProperty primitiveTypeProperty = (KlassPrimitiveTypeProperty) resolve;
                KlassPrimitiveType         primitiveType         = primitiveTypeProperty.getPrimitiveType();
                KlassOptionalMarker        optionalMarker        = primitiveTypeProperty.getOptionalMarker();
                Multiplicity multiplicity =
                        optionalMarker == null ? Multiplicity.ONE_TO_ONE : Multiplicity.ZERO_TO_ONE;
                String dataTypeText = primitiveType.getText();
                MutableList<Type> result = Lists.mutable.with(new Type(
                        DataTypeType.PRIMITIVE_TYPE,
                        dataTypeText,
                        multiplicity));
                if (dataTypeText.equals("ID"))
                {
                    result.add(new Type(DataTypeType.PRIMITIVE_TYPE, "Long", multiplicity));
                }
                return result;
            }
            if (resolve instanceof KlassEnumerationProperty)
            {
                KlassEnumerationProperty enumerationProperty = (KlassEnumerationProperty) resolve;
                // TODO: Create a common interface above KlassEnumerationType and KlassDataType and reduce some code duplication
                KlassEnumerationType enumerationType = enumerationProperty.getEnumerationType();
                KlassOptionalMarker  optionalMarker  = enumerationProperty.getOptionalMarker();
                return Collections.singletonList(new Type(
                        DataTypeType.PRIMITIVE_TYPE,
                        enumerationType.getText(),
                        optionalMarker == null ? Multiplicity.ONE_TO_ONE : Multiplicity.ZERO_TO_ONE));
            }
            if (resolve instanceof KlassEnumerationLiteral)
            {
                KlassEnumerationLiteral enumerationLiteral = (KlassEnumerationLiteral) resolve;
                KlassEnumeration enumeration = PsiTreeUtil.getParentOfType(enumerationLiteral, KlassEnumeration.class);

                return Collections.singletonList(new Type(
                        DataTypeType.PRIMITIVE_TYPE,
                        enumeration.getName(),
                        Multiplicity.ONE_TO_ONE));
            }
            if (resolve instanceof KlassClassModifier)
            {
                KlassClassModifier classModifier = (KlassClassModifier) resolve;
                String             modifierText  = classModifier.getText();
                if (!TEMPORAL_PROPERTY_MODIFIERS.contains(modifierText))
                {
                    throw new AssertionError(modifierText);
                }
                Type instantType = new Type(
                        DataTypeType.PRIMITIVE_TYPE,
                        "Instant",
                        Multiplicity.ONE_TO_ONE);
                // TODO: Date literals in the language, infinity and now global variables
                Type temporalInstantType = new Type(
                        DataTypeType.PRIMITIVE_TYPE,
                        "TemporalInstant",
                        Multiplicity.ONE_TO_ONE);
                Type temporalRangeType = new Type(
                        DataTypeType.PRIMITIVE_TYPE,
                        "TemporalRange",
                        Multiplicity.ONE_TO_ONE);
                return Lists.immutable.with(instantType, temporalInstantType, temporalRangeType).castToList();
            }
            if (resolve != null)
            {
                throw new AssertionError(resolve.getClass());
            }
        }

        if (expressionValue instanceof KlassExpressionVariableName)
        {
            KlassExpressionVariableName expressionVariableName = (KlassExpressionVariableName) expressionValue;
            PsiReference                reference              = expressionVariableName.getReference();
            KlassParameterDeclaration   parameterDeclaration   = (KlassParameterDeclaration) reference.resolve();
            if (parameterDeclaration != null)
            {
                List<Type> result = this.getParameterDeclarationType(parameterDeclaration);
                if (result != null)
                {
                    return result;
                }
                throw new UnsupportedOperationException(expressionVariableName.getText());
            }
        }

        return Lists.immutable.<Type>empty().castToList();
    }

    public static List<Type> getExpressionLiteralTypes(
            KlassExpressionLiteral expressionLiteral,
            Multiplicity multiplicity)
    {
        KlassBooleanLiteral booleanLiteral = expressionLiteral.getBooleanLiteral();
        if (booleanLiteral != null)
        {
            return Collections.singletonList(new Type(DataTypeType.PRIMITIVE_TYPE, "Boolean", multiplicity));
        }

        KlassIntegerLiteralNode integerLiteralNode = expressionLiteral.getIntegerLiteralNode();
        if (integerLiteralNode != null)
        {
            return Arrays.asList(
                    new Type(DataTypeType.PRIMITIVE_TYPE, "Integer", multiplicity),
                    new Type(DataTypeType.PRIMITIVE_TYPE, "Long", multiplicity));
        }
        KlassFloatLiteralNode floatLiteralNode = expressionLiteral.getFloatLiteralNode();
        if (floatLiteralNode != null)
        {
            return Arrays.asList(
                    new Type(DataTypeType.PRIMITIVE_TYPE, "Float", multiplicity),
                    new Type(DataTypeType.PRIMITIVE_TYPE, "Double", multiplicity));
        }
        KlassStringLiteralNode stringLiteralNode = expressionLiteral.getStringLiteralNode();
        if (stringLiteralNode != null)
        {
            return Collections.singletonList(new Type(DataTypeType.PRIMITIVE_TYPE, "String", multiplicity));
        }
        throw new AssertionError(expressionLiteral.getText());
    }

    @Nullable
    private List<Type> getParameterDeclarationType(KlassParameterDeclaration parameterDeclaration)
    {
        KlassDataTypeDeclaration dataTypeDeclaration = parameterDeclaration.getDataTypeDeclaration();
        KlassMultiplicity        multiplicity        = dataTypeDeclaration.getMultiplicity();
        KlassDataType            dataType            = dataTypeDeclaration.getDataType();
        DataTypeType             dataTypeType        = this.getDataTypeType(dataType);

        if (dataTypeType == null)
        {
            return null;
        }

        return Collections.singletonList(new Type(
                dataTypeType,
                dataType.getText(),
                this.getMultiplicity(multiplicity)));
    }

    private DataTypeType getDataTypeType(KlassDataType dataType)
    {
        if (dataType instanceof KlassPrimitiveType)
        {
            return DataTypeType.PRIMITIVE_TYPE;
        }
        if (dataType instanceof KlassEnumerationType)
        {
            return DataTypeType.ENUMERATION;
        }
        return null;
    }

    private Multiplicity getMultiplicity(KlassMultiplicity multiplicity)
    {
        String lowerBound  = multiplicity.getLowerBound().getText();
        String uppderBound = multiplicity.getUpperBound().getText();

        if (lowerBound.equals("0") && uppderBound.equals("1"))
        {
            return Multiplicity.ZERO_TO_ONE;
        }

        if (lowerBound.equals("1") && uppderBound.equals("1"))
        {
            return Multiplicity.ONE_TO_ONE;
        }

        if (lowerBound.equals("0") && uppderBound.equals("*"))
        {
            return Multiplicity.ZERO_TO_MANY;
        }

        if (lowerBound.equals("1") && uppderBound.equals("*"))
        {
            return Multiplicity.ONE_TO_MANY;
        }

        throw new AssertionError(multiplicity.getText());
    }
}
