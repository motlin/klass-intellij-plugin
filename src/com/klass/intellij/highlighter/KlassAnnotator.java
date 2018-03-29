package com.klass.intellij.highlighter;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.highlighter.type.Multiplicity;
import com.klass.intellij.highlighter.type.PrimitiveTypeType;
import com.klass.intellij.highlighter.type.Type;
import com.klass.intellij.psi.*;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KlassAnnotator implements Annotator
{
    public static final ImmutableList<String> TEMPORAL_PROPERTY_KEYWORDS =
            Lists.immutable.with("validTemporal", "systemTemporal", "bitemporal");

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder)
    {
        element.accept(new AnnotatorKlassVisitor(holder));
    }

    private static class AnnotatorKlassVisitor extends KlassVisitor
    {
        private final AnnotationHolder annotationHolder;

        private AnnotatorKlassVisitor(AnnotationHolder annotationHolder)
        {
            this.annotationHolder = annotationHolder;
        }

        private void applyClassName(PsiElement psiElement)
        {
            Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(psiElement, null);
            infoAnnotation.setTextAttributes(KlassHighlightingColors.CLASS_NAME_ATTRIBUTES);
        }

        @Override
        public void visitVerb(@NotNull KlassVerb klassVerb)
        {
            Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(klassVerb, null);
            infoAnnotation.setTextAttributes(KlassHighlightingColors.VERB);
        }

        @Override
        public void visitNombreText(@NotNull KlassNombreText klassNombreText)
        {
            Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(klassNombreText, null);
            PsiElement parent = klassNombreText.getParent().getParent();
            if (parent instanceof KlassPropertyName)
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
        public void visitKlass(@NotNull KlassKlass klassKlass)
        {
            List<KlassMember> propertyList = klassKlass.getMemberList();
            Map<String, Long> propertyCountByName = propertyList.stream()
                    .map(PsiNamedElement::getName)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            for (KlassMember klassMember : propertyList)
            {
                String propertyName = klassMember.getName();
                Long occurrences = propertyCountByName.get(propertyName);
                if (occurrences > 1)
                {
                    String message = String.format("Duplicate property '%s'", propertyName);
                    this.annotationHolder.createErrorAnnotation(klassMember.getNombre(), message);
                }
            }
        }

        @Override
        public void visitEnumerationType(@NotNull KlassEnumerationType klassEnumerationType)
        {
            PsiReference reference = klassEnumerationType.getReference();
            if (reference != null && reference.resolve() == null)
            {
                String message = String.format("Cannot resolve symbol '%s'", klassEnumerationType.getText());
                this.annotationHolder.createErrorAnnotation(klassEnumerationType, message);
            }
            this.applyClassName(klassEnumerationType);
        }

        @Override
        public void visitAssociation(@NotNull KlassAssociation klassAssociation)
        {
            List<KlassAssociationEnd> associationEndList = klassAssociation.getAssociationEndList();
            int size = associationEndList.size();
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
        public void visitKlassName(@NotNull KlassKlassName klassKlassName)
        {
            PsiReference reference = klassKlassName.getReference();
            if (reference != null && reference.resolve() == null)
            {
                String message = String.format("Cannot resolve symbol '%s'", klassKlassName.getText());
                this.annotationHolder.createErrorAnnotation(klassKlassName, message);
            }
            this.applyClassName(klassKlassName);
        }

        @Override
        public void visitDummyMultiplicity(@NotNull KlassDummyMultiplicity klassDummyMultiplicity)
        {
            this.annotationHolder.createErrorAnnotation(klassDummyMultiplicity, "Expected multiplicity");
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
        public void visitExpressionVariableName(@NotNull KlassExpressionVariableName expressionVariableName)
        {
            this.annotateCannotResolve(expressionVariableName);
        }

        public void annotateCannotResolve(@NotNull PsiElement psiElement)
        {
            PsiReference reference = psiElement.getReference();
            PsiElement resolved = reference.resolve();
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

        @Override
        public void visitPropertyName(@NotNull KlassPropertyName propertyName)
        {
            PsiReference reference = propertyName.getReference();
            PsiElement resolved = reference.resolve();
            if (resolved == null)
            {
                String message = String.format("Cannot resolve symbol '%s'", propertyName.getText());
                this.annotationHolder.createErrorAnnotation(
                        propertyName,
                        message);
            }
            else if (resolved instanceof KlassDataTypeProperty
                    || resolved instanceof KlassEnumerationProperty
                    || resolved instanceof KlassKeywordOnClass)
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
        public void visitParameterDeclaration(@NotNull KlassParameterDeclaration parameterDeclaration)
        {
            if (parameterDeclaration.getPrimitiveTypeDeclaration().getMultiplicity() == null)
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

            if (ListAdapter.adapt(possibleSourceTypes).allSatisfy(type -> type.getMultiplicity().isToMany()))
            {
                String message = String.format(
                        "Invalid multiplicity '%s'.",
                        possibleSourceTypes.get(0));
                this.annotationHolder.createErrorAnnotation(
                        criteriaOperator,
                        message);
            }
            KlassOperator operator = criteriaOperator.getOperator();
            boolean expectMany = operator.getText().equals("in");
            if (ListAdapter.adapt(possibleTargetTypes).allSatisfy(type -> type.getMultiplicity().isToMany())
                    != expectMany)
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
        public void visitServiceProjectionClause(@NotNull KlassServiceProjectionClause projectionClause)
        {
            List<KlassParameterName> parameterNameList = projectionClause.getParameterNameList();
            KlassProjectionName projectionName = projectionClause.getProjectionName();
            KlassProjectionReference projectionReference = (KlassProjectionReference) projectionName.getReference();
            KlassProjection projection = (KlassProjection) projectionReference.resolve();
            if (projection != null)
            {
                MutableList<KlassParameterDeclaration> projectionParameterDeclarations =
                        ListAdapter.adapt(projection.getParameterDeclarationList());

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
                                    .collect(KlassParameterDeclaration::getPrimitiveTypeDeclaration)
                                    .collect(PsiElement::getText)
                                    .makeString(),
                            serviceParameterDeclarations
                                    .collect(KlassParameterDeclaration::getPrimitiveTypeDeclaration)
                                    .collect(PsiElement::getText)
                                    .makeString());
                    this.annotationHolder.createErrorAnnotation(
                            projectionClause.getProjectionName(),
                            message);
                }
            }
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

            List<KlassParameterName> parameterNameList = projectionParameterizedPropertyNode.getParameterNameList();

            KlassProjection projection =
                    PsiTreeUtil.getParentOfType(projectionParameterizedPropertyNode, KlassProjection.class);

            MutableList<KlassParameterDeclaration> propertyParameterDeclarations =
                    ListAdapter.adapt(parameterizedProperty.getParameterDeclarationList());

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
                                .collect(KlassParameterDeclaration::getPrimitiveTypeDeclaration)
                                .collect(PsiElement::getText)
                                .makeString(),
                        projectionParameterDeclarations
                                .collect(KlassParameterDeclaration::getPrimitiveTypeDeclaration)
                                .collect(PsiElement::getText)
                                .makeString());
                this.annotationHolder.createErrorAnnotation(
                        parameterizedPropertyName,
                        message);
            }
        }

        public void typeCheckPassedParameters(
                List<KlassParameterName> parameterNameList,
                MutableList<KlassParameterDeclaration> propertyParameterDeclarations,
                MutableList<KlassParameterDeclaration> projectionParameterDeclarations)
        {
            for (int i = 0; i < parameterNameList.size(); i++)
            {
                KlassParameterName parameterName = parameterNameList.get(i);
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

        private List<Type> getPossibleTypes(KlassExpressionValue expressionValue)
        {
            KlassExpressionLiteral expressionLiteral = expressionValue.getExpressionLiteral();
            if (expressionLiteral != null)
            {
                return KlassAnnotator.getExpressionLiteralTypes(expressionLiteral, Multiplicity.ONE_TO_ONE);
            }

            KlassExpressionLiteralList expressionLiteralListNode = expressionValue.getExpressionLiteralList();
            if (expressionLiteralListNode != null)
            {
                List<KlassExpressionLiteral> expressionLiteralList =
                        expressionLiteralListNode.getExpressionLiteralList();
                int size = expressionLiteralList.size();
                // TODO: Test empty literal list
                return KlassAnnotator.getExpressionLiteralTypes(
                        expressionLiteralList.get(0),
                        Multiplicity.ZERO_TO_MANY);
            }

            KlassExpressionNativeValue expressionNativeValue = expressionValue.getExpressionNativeValue();
            if (expressionNativeValue != null)
            {
                if (expressionNativeValue.getText().equals("user"))
                {
                    return Collections.singletonList(new Type(
                            PrimitiveTypeType.DATA_TYPE,
                            "String",
                            Multiplicity.ONE_TO_ONE));
                }
                throw new UnsupportedOperationException(expressionNativeValue.getText());
            }

            KlassExpressionProperty expressionProperty = expressionValue.getExpressionProperty();
            if (expressionProperty != null)
            {
                KlassPropertyName propertyName = expressionProperty.getPropertyName();
                KlassMemberReference reference = (KlassMemberReference) propertyName.getReference();
                PsiElement resolve = reference.resolve();
                if (resolve instanceof KlassDataTypeProperty)
                {
                    KlassDataTypeProperty dataTypeProperty = (KlassDataTypeProperty) resolve;
                    KlassDataType dataType = dataTypeProperty.getDataType();
                    KlassOptionalMarker optionalMarker = dataTypeProperty.getOptionalMarker();
                    Multiplicity multiplicity =
                            optionalMarker == null ? Multiplicity.ONE_TO_ONE : Multiplicity.ZERO_TO_ONE;
                    String dataTypeText = dataType.getText();
                    MutableList<Type> result = Lists.mutable.with(new Type(
                            PrimitiveTypeType.DATA_TYPE,
                            dataTypeText,
                            multiplicity));
                    if (dataTypeText.equals("ID"))
                    {
                        result.add(new Type(PrimitiveTypeType.DATA_TYPE, "Long", multiplicity));
                    }
                    return result;
                }
                if (resolve instanceof KlassEnumerationProperty)
                {
                    KlassEnumerationProperty enumerationProperty = (KlassEnumerationProperty) resolve;
                    // TODO: Create a common interface above KlassEnumerationType and KlassDataType and reduce some code duplication
                    KlassEnumerationType enumerationType = enumerationProperty.getEnumerationType();
                    KlassOptionalMarker optionalMarker = enumerationProperty.getOptionalMarker();
                    return Collections.singletonList(new Type(
                            PrimitiveTypeType.DATA_TYPE,
                            enumerationType.getText(),
                            optionalMarker == null ? Multiplicity.ONE_TO_ONE : Multiplicity.ZERO_TO_ONE));
                }
                if (resolve instanceof KlassEnumerationLiteral)
                {
                    KlassEnumerationLiteral enumerationLiteral = (KlassEnumerationLiteral) resolve;
                    KlassEnumeration enumeration = (KlassEnumeration) enumerationLiteral.getParent();

                    return Collections.singletonList(new Type(
                            PrimitiveTypeType.DATA_TYPE,
                            enumeration.getName(),
                            Multiplicity.ONE_TO_ONE));
                }
                if (resolve instanceof KlassKeywordOnClass)
                {
                    KlassKeywordOnClass keywordOnClass = (KlassKeywordOnClass) resolve;
                    String keywordText = keywordOnClass.getText();
                    if (!TEMPORAL_PROPERTY_KEYWORDS.contains(keywordText))
                    {
                        throw new AssertionError(keywordText);
                    }
                    return Collections.singletonList(new Type(
                            PrimitiveTypeType.DATA_TYPE,
                            "Instant",
                            Multiplicity.ONE_TO_ONE));
                }
                throw new AssertionError(resolve.getClass());
            }

            KlassExpressionVariableName expressionVariableName = expressionValue.getExpressionVariableName();
            if (expressionVariableName != null)
            {
                PsiReference reference = expressionVariableName.getReference();
                KlassParameterDeclaration parameterDeclaration = (KlassParameterDeclaration) reference.resolve();
                List<Type> result = this.getParameterDeclarationType(parameterDeclaration);
                if (result != null)
                {
                    return result;
                }
                throw new UnsupportedOperationException(expressionVariableName.getText());
            }

            throw new AssertionError(expressionValue.getText());
        }

        @Override
        public void visitParameterName(@NotNull KlassParameterName parameterName)
        {
            this.annotateCannotResolve(parameterName);
        }

        @Nullable
        private List<Type> getParameterDeclarationType(KlassParameterDeclaration parameterDeclaration)
        {
            KlassPrimitiveTypeDeclaration primitiveTypeDeclaration = parameterDeclaration.getPrimitiveTypeDeclaration();
            KlassMultiplicity multiplicity = primitiveTypeDeclaration.getMultiplicity();
            KlassDataType dataType = primitiveTypeDeclaration.getDataType();
            if (dataType != null)
            {
                return Collections.singletonList(new Type(
                        PrimitiveTypeType.DATA_TYPE,
                        dataType.getText(),
                        this.getMultiplicity(multiplicity)));
            }
            KlassEnumerationType enumerationType = primitiveTypeDeclaration.getEnumerationType();
            if (enumerationType != null)
            {
                return Collections.singletonList(new Type(
                        PrimitiveTypeType.ENUMERATION,
                        enumerationType.getText(),
                        this.getMultiplicity(multiplicity)));
            }
            return null;
        }

        private Multiplicity getMultiplicity(KlassMultiplicity multiplicity)
        {
            String lowerBound = multiplicity.getLowerBound().getText();
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

    private static List<Type> getExpressionLiteralTypes(
            KlassExpressionLiteral expressionLiteral,
            Multiplicity multiplicity)
    {
        KlassBooleanLiteral booleanLiteral = expressionLiteral.getBooleanLiteral();
        if (booleanLiteral != null)
        {
            return Collections.singletonList(new Type(PrimitiveTypeType.DATA_TYPE, "Boolean", multiplicity));
        }

        KlassIntegerLiteralNode integerLiteralNode = expressionLiteral.getIntegerLiteralNode();
        if (integerLiteralNode != null)
        {
            return Arrays.asList(
                    new Type(PrimitiveTypeType.DATA_TYPE, "Integer", multiplicity),
                    new Type(PrimitiveTypeType.DATA_TYPE, "Long", multiplicity));
        }
        KlassFloatLiteralNode floatLiteralNode = expressionLiteral.getFloatLiteralNode();
        if (floatLiteralNode != null)
        {
            return Arrays.asList(
                    new Type(PrimitiveTypeType.DATA_TYPE, "Float", multiplicity),
                    new Type(PrimitiveTypeType.DATA_TYPE, "Double", multiplicity));
        }
        KlassStringLiteralNode stringLiteralNode = expressionLiteral.getStringLiteralNode();
        if (stringLiteralNode != null)
        {
            return Collections.singletonList(new Type(PrimitiveTypeType.DATA_TYPE, "String", multiplicity));
        }
        throw new AssertionError(expressionLiteral.getText());
    }
}
