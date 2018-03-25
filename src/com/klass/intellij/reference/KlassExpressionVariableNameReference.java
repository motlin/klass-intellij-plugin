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

    public KlassExpressionVariableNameReference(@NotNull PsiElement element, String expressionVariableName)
    {
        super(element, new TextRange(0, expressionVariableName.length()));
        this.expressionVariableName = expressionVariableName;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        KlassUrlGroup klassUrlGroup = PsiTreeUtil.getParentOfType(this.myElement, KlassUrlGroup.class);
        if (klassUrlGroup != null)
        {
            KlassUrl url = klassUrlGroup.getUrl();
            List<PsiElementResolveResult> queryParamResults = url.getQueryParamPartList()
                    .stream()
                    .map(KlassQueryParamPart::getParameterDeclaration)
                    .filter(queryParameter -> queryParameter.getName().equals(this.expressionVariableName))
                    .map(PsiElementResolveResult::new)
                    .collect(Collectors.toList());
            List<PsiElementResolveResult> pathParameterResults = url.getUrlPartList()
                    .stream()
                    .map(KlassUrlPart::getParameterDeclaration)
                    .filter(Objects::nonNull)
                    .filter(pathParameter -> pathParameter.getName().equals(this.expressionVariableName))
                    .map(PsiElementResolveResult::new)
                    .collect(Collectors.toList());

            List<PsiElementResolveResult> results = new ArrayList<>();
            results.addAll(queryParamResults);
            results.addAll(pathParameterResults);
            return results.toArray(new ResolveResult[results.size()]);
        }

        KlassParameterizedProperty parameterizedProperty =
                PsiTreeUtil.getParentOfType(this.myElement, KlassParameterizedProperty.class);
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

        klassUrlGroup.getUrl()
                .getUrlPartList()
                .stream()
                .map(KlassUrlPart::getParameterDeclaration)
                .filter(Objects::nonNull)
                .map(PsiNamedElement::getName)
                .map(LookupElementBuilder::create)
                .map(lookupElementBuilder -> lookupElementBuilder.withIcon(AllIcons.Nodes.Variable))
                .forEach(variants::add);

        klassUrlGroup.getUrl()
                .getQueryParamPartList()
                .stream()
                .map(KlassQueryParamPart::getParameterDeclaration)
                .map(PsiNamedElement::getName)
                .map(LookupElementBuilder::create)
                .map(lookupElementBuilder -> lookupElementBuilder.withIcon(AllIcons.Nodes.Variable))
                .forEach(variants::add);

        return variants.toArray();
    }
}
