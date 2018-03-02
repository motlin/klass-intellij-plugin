package com.klass.intellij.highlighter;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.klass.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

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
        public void visitAssociationEnd(@NotNull KlassAssociationEnd klassAssociationEnd)
        {
            if (klassAssociationEnd.getMultiplicity() == null)
            {
                String message = String.format("Expected multiplicity", klassAssociationEnd.getText());

                this.annotationHolder.createErrorAnnotation(klassAssociationEnd.getNode(), message);
            }
        }

        @Override
        public void visitAssociation(@NotNull KlassAssociation klassAssociation)
        {
            int size = klassAssociation.getAssociationEndList().size();
            if (size == 0)
            {
                String message = "Expected association ends.";
                this.annotationHolder.createErrorAnnotation(klassAssociation, message);
            }

            if (size == 1)
            {
                String message = "Expected two association ends.";
                this.annotationHolder.createErrorAnnotation(klassAssociation.getAssociationEndList().get(0), message);
            }

            if (size > 2)
            {
                String message = "Expected two association ends.";
                this.annotationHolder.createErrorAnnotation(klassAssociation.getAssociationEndList().get(2), message);
            }
        }
    }
}
