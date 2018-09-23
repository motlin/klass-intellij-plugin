package com.klass.intellij.reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassExpressionVariableName;
import com.klass.intellij.psi.KlassParameterDeclaration;
import com.klass.intellij.psi.KlassParameterizedProperty;
import com.klass.intellij.psi.KlassQueryParamPart;
import com.klass.intellij.psi.KlassQueryParams;
import com.klass.intellij.psi.KlassUrl;
import com.klass.intellij.psi.KlassUrlGroup;
import com.klass.intellij.psi.KlassUrlPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassExpressionVariableNameReference extends PsiPolyVariantReferenceBase<PsiElement>
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
            KlassQueryParams queryParams = url.getQueryParams();
            List<PsiElementResolveResult> queryParamResults = queryParams == null
                    ? Arrays.asList()
                    : queryParams
                            .getQueryParamPartList()
                            .stream()
                            .map(KlassQueryParamPart::getParameterDeclaration)
                            .filter(queryParameter -> queryParameter.getName().equals(this.expressionVariableName))
                            .map(PsiElementResolveResult::new)
                            .collect(Collectors.toList());
            List<PsiElementResolveResult> pathParameterResults = url
                    .getUrlPartList()
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
                    parameterizedProperty.getPropertyParameterDeclarationsParens().getParameterDeclarations().getParameterDeclarationList();
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

        if (klassUrlGroup == null)
        {
            return new Object[]{};
        }

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
                .getQueryParams()
                .getQueryParamPartList()
                .stream()
                .map(KlassQueryParamPart::getParameterDeclaration)
                .map(PsiNamedElement::getName)
                .map(LookupElementBuilder::create)
                .map(lookupElementBuilder -> lookupElementBuilder.withIcon(AllIcons.Nodes.Variable))
                .forEach(variants::add);

        return variants.toArray();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
    {
        ASTNode node = this.myElement.getNode();
        if (node != null)
        {
            KlassExpressionVariableName expressionVariableName = KlassElementFactory.createExpressionVariableName(
                    this.myElement.getProject(),
                    newElementName);

            ASTNode newNode = expressionVariableName.getNode();
            node.getTreeParent().replaceChild(node, newNode);
        }
        return this.myElement;
    }
}
