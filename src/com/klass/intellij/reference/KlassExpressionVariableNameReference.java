package com.klass.intellij.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.psi.KlassPathParameter;
import com.klass.intellij.psi.KlassUrl;
import com.klass.intellij.psi.KlassUrlGroup;
import com.klass.intellij.psi.KlassUrlPart;
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

        KlassUrlGroup klassUrlGroup = PsiTreeUtil.getParentOfType(this.myElement, KlassUrlGroup.class);
        KlassUrl url = klassUrlGroup.getUrl();
        List<KlassPathParameter> pathParameters = url.getUrlPartList()
                .stream()
                .map(KlassUrlPart::getPathParameter)
                .filter(Objects::nonNull)
                .filter(pathParameter -> pathParameter.getName().equals(this.expressionVariableName))
                .collect(Collectors.toList());
        if (pathParameters.size() == 1)
        {
            this.pathParameter = pathParameters.get(0);
            return new PsiElementResolveResult[]{new PsiElementResolveResult(this.pathParameter)};
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
        Project project = this.myElement.getProject();
        // List<KlassPathParameter> pathParameters = KlassUtil.findPathParameters(project);
        List<LookupElement> variants = new ArrayList<>();
        /*
        for (KlassPathParameter pathParameter : pathParameters)
        {
            if (pathParameter.getName() != null && !pathParameter.getName().isEmpty())
            {
                LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(pathParameter.getName())
                        .withIcon(AllIcons.Hierarchy.Subtypes)
                        .withTypeText(pathParameter.getContainingFile().getName());
                variants.add(lookupElementBuilder);
            }
        }
        */
        return variants.toArray();
    }
}
