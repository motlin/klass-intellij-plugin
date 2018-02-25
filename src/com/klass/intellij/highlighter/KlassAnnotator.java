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
        public void visitClassName(@NotNull KlassClassName klassClassName)
        {
            this.applyClassName(klassClassName);
        }

        @Override
        public void visitAssociationName(@NotNull KlassAssociationName klassAssociationName)
        {
            this.applyClassName(klassAssociationName);
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

        private void applyPropertyName(PsiElement psiElement)
        {
            Annotation infoAnnotation = this.annotationHolder.createInfoAnnotation(psiElement.getNode(), null);
            infoAnnotation.setTextAttributes(KlassHighlightingColors.INSTANCE_FINAL_FIELD_ATTRIBUTES);
        }

        @Override
        public void visitDataTypePropertyName(@NotNull KlassDataTypePropertyName klassDataTypePropertyName)
        {
            this.applyPropertyName(klassDataTypePropertyName);
        }

        @Override
        public void visitAssociationEndName(@NotNull KlassAssociationEndName klassAssociationEndName)
        {
            this.applyPropertyName(klassAssociationEndName);
        }
    }
}
