package com.klass.intellij.highlighter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import com.klass.intellij.psi.KlassDataTypeDeclaration;
import com.klass.intellij.psi.KlassDummyMultiplicity;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassEnumerationLiteral;
import com.klass.intellij.psi.KlassEnumerationProperty;
import com.klass.intellij.psi.KlassEnumerationType;
import com.klass.intellij.psi.KlassExpressionLiteral;
import com.klass.intellij.psi.KlassExpressionLiteralList;
import com.klass.intellij.psi.KlassExpressionMemberName;
import com.klass.intellij.psi.KlassExpressionNativeValue;
import com.klass.intellij.psi.KlassExpressionValue;
import com.klass.intellij.psi.KlassExpressionVariableName;
import com.klass.intellij.psi.KlassFloatLiteralNode;
import com.klass.intellij.psi.KlassIntegerLiteralNode;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassKlassName;
import com.klass.intellij.psi.KlassLowerBound;
import com.klass.intellij.psi.KlassMember;
import com.klass.intellij.psi.KlassMemberName;
import com.klass.intellij.psi.KlassMultiplicity;
import com.klass.intellij.psi.KlassNamedElement;
import com.klass.intellij.psi.KlassNombre;
import com.klass.intellij.psi.KlassNombreText;
import com.klass.intellij.psi.KlassOperator;
import com.klass.intellij.psi.KlassOptionalMarker;
import com.klass.intellij.psi.KlassParameterDeclaration;
import com.klass.intellij.psi.KlassParameterName;
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

public class ReservedNameVisitor extends KlassVisitor
{
    protected static final ImmutableList<String> JAVA_KEYWORDS = Lists.immutable.with(
            "abstract",
            "assert",
            "boolean",
            "break",
            "byte",
            "case",
            "catch",
            "char",
            "class",
            "const",
            "continue",
            "default",
            "do",
            "double",
            "else",
            "enum",
            "extends",
            "final",
            "finally",
            "float",
            "for",
            "goto",
            "if",
            "implements",
            "import",
            "instanceof",
            "int",
            "interface",
            "long",
            "native",
            "new",
            "package",
            "private",
            "protected",
            "public",
            "return",
            "short",
            "static",
            "strictfp",
            "super",
            "switch",
            "synchronized",
            "this",
            "throw",
            "throws",
            "transient",
            "try",
            "var",
            "void",
            "volatile",
            "while");

    protected static final ImmutableList<String> JAVA_LITERALS = Lists.immutable.with("true", "false", "null");

    private final AnnotationHolder annotationHolder;

    public ReservedNameVisitor(AnnotationHolder annotationHolder)
    {
        this.annotationHolder = annotationHolder;
    }

    @Override
    public void visitNombre(@NotNull KlassNombre nombre)
    {
        if (JAVA_KEYWORDS.contains(nombre.getText()))
        {
            String message = "Reserved Java keyword";
            this.annotationHolder.createErrorAnnotation(nombre, message);
        }
        else if (JAVA_LITERALS.contains(nombre.getText()))
        {
            String message = "Reserved Java literal";
            this.annotationHolder.createErrorAnnotation(nombre, message);
        }
    }
}
