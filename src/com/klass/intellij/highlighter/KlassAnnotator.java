package com.klass.intellij.highlighter;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KlassAnnotator implements Annotator
{
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
            Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(psiElement.getNode(), null);
            infoAnnotation.setTextAttributes(KlassHighlightingColors.CLASS_NAME_ATTRIBUTES);
        }

        @Override
        public void visitNombre(@NotNull KlassNombre klassNombre)
        {
            Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(klassNombre.getNode(), null);
            if (klassNombre.getParent() instanceof KlassEnumeration)
            {
                infoAnnotation.setTextAttributes(KlassHighlightingColors.ENUM_NAME_ATTRIBUTES);
            }
            else if (klassNombre.getParent() instanceof KlassProperty
                    || klassNombre.getParent() instanceof KlassAssociationEnd)
            {
                infoAnnotation.setTextAttributes(KlassHighlightingColors.INSTANCE_FINAL_FIELD_ATTRIBUTES);
            }
            else
            {
                infoAnnotation.setTextAttributes(KlassHighlightingColors.CLASS_NAME_ATTRIBUTES);
            }
        }

        @Override
        public void visitKlass(@NotNull KlassKlass klassKlass)
        {
            List<KlassProperty> propertyList = klassKlass.getPropertyList();
            Map<String, Long> propertyCountByName = propertyList.stream()
                    .map(PsiNamedElement::getName)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            for (KlassProperty klassProperty : propertyList)
            {
                String propertyName = klassProperty.getName();
                Long occurrences = propertyCountByName.get(propertyName);
                if (occurrences > 1)
                {
                    String message = String.format("Duplicate property '%s'", propertyName);
                    this.annotationHolder.createErrorAnnotation(klassProperty.getNombre(), message);
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
                this.annotationHolder.createErrorAnnotation(klassEnumerationType.getNode(), message);
            }
            this.applyClassName(klassEnumerationType);
        }

        @Override
        public void visitEnumerationLiteral(@NotNull KlassEnumerationLiteral klassEnumerationLiteral)
        {
            Annotation infoAnnotation =
                    this.annotationHolder.createInfoAnnotation(klassEnumerationLiteral.getNode(), null);
            infoAnnotation.setTextAttributes(KlassHighlightingColors.ENUM_LITERAL_ATTRIBUTES);
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
                String message = String.format("Expected multiplicity", klassAssociationEnd.getText());

                this.annotationHolder.createErrorAnnotation(klassAssociationEnd.getNode(), message);
            }
        }

        @Override
        public void visitAssociationEndType(@NotNull KlassAssociationEndType klassAssociationEndType)
        {
            PsiReference reference = klassAssociationEndType.getReference();
            if (reference != null && reference.resolve() == null)
            {
                String message = String.format("Cannot resolve symbol '%s'", klassAssociationEndType.getText());
                this.annotationHolder.createErrorAnnotation(klassAssociationEndType.getNode(), message);
            }
            this.applyClassName(klassAssociationEndType);
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
    }
}
