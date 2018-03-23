package com.klass.intellij.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassExpressionNativeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KlassExpressionNativeValueReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    @NotNull
    private final String expressionNativeValueText;

    public KlassExpressionNativeValueReference(
            KlassExpressionNativeValue element,
            String expressionNativeValueText)
    {
        super(element, new TextRange(0, expressionNativeValueText.length()));

        this.expressionNativeValueText = expressionNativeValueText;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        Project project = this.myElement.getProject();
        return KlassUtil.findClasses(project)
                .stream()
                .filter(klassKlass -> klassKlass.getFirstChild().getText().equals("user"))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Nullable
    @Override
    public PsiElement resolve()
    {
        ResolveResult[] resolveResults = this.multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        List<LookupElement> variants = new ArrayList<>();
        return variants.toArray();
    }
}
