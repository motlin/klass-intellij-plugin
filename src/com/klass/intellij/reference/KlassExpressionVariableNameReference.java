package com.klass.intellij.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KlassExpressionVariableNameReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    private final String expressionVariableName;

    private KlassPathParameter pathParameter;
    private KlassParameterDeclaration parameterDeclaration;

    public KlassExpressionVariableNameReference(@NotNull PsiElement element, String expressionVariableName)
    {
        super(element, new TextRange(0, expressionVariableName.length()));
        this.expressionVariableName = expressionVariableName;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        if (this.pathParameter != null)
        {
            return new PsiElementResolveResult[]{new PsiElementResolveResult(this.pathParameter)};
        }

        if (this.parameterDeclaration != null)
        {
            return new PsiElementResolveResult[]{new PsiElementResolveResult(this.parameterDeclaration)};
        }

        KlassUrlGroup klassUrlGroup = PsiTreeUtil.getParentOfType(this.myElement, KlassUrlGroup.class);
        KlassParameterizedProperty parameterizedProperty =
                PsiTreeUtil.getParentOfType(this.myElement, KlassParameterizedProperty.class);

        if (klassUrlGroup != null)
        {
            KlassUrl url = klassUrlGroup.getUrl();
            ResolveResult[] resolveResults = url.getUrlPartList()
                    .stream()
                    .map(KlassUrlPart::getPathParameter)
                    .filter(Objects::nonNull)
                    .filter(pathParameter -> pathParameter.getName().equals(this.expressionVariableName))
                    .map(PsiElementResolveResult::new)
                    .toArray(ResolveResult[]::new);
            return resolveResults;
        }

        if (parameterizedProperty != null)
        {
            List<KlassParameterDeclaration> parameterDeclarationList =
                    parameterizedProperty.getParameterDeclarationList();
            ResolveResult[] resolveResults = parameterDeclarationList.stream()
                    .filter(parameterDeclaration -> parameterDeclaration.getName().equals(this.expressionVariableName))
                    .map(PsiElementResolveResult::new)
                    .toArray(ResolveResult[]::new);
            return resolveResults;
        }
        return new ResolveResult[]{};
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
        KlassUrlGroup klassUrlGroup = PsiTreeUtil.getParentOfType(this.myElement, KlassUrlGroup.class);
        KlassUrl url = klassUrlGroup.getUrl();
        List<KlassPathParameter> pathParameters = url.getUrlPartList()
                .stream()
                .map(KlassUrlPart::getPathParameter)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        for (KlassPathParameter pathParameter : pathParameters)
        {
            LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(pathParameter.getName())
                    .withIcon(AllIcons.Nodes.Variable);
            variants.add(lookupElementBuilder);
        }
        return variants.toArray();
    }
}
