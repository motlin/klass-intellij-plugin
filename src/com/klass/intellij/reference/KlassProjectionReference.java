package com.klass.intellij.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassProjectionName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassProjectionReference extends PsiPolyVariantReferenceBase<PsiElement>
{
    private final String projectionName;

    private KlassProjection projection;

    public KlassProjectionReference(@NotNull PsiElement element, String projectionName)
    {
        super(element, new TextRange(0, projectionName.length()));
        this.projectionName = projectionName;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        if (this.projection != null)
        {
            return new PsiElementResolveResult[]{new PsiElementResolveResult(this.projection)};
        }

        Project project = this.myElement.getProject();
        List<KlassProjection> projections = KlassUtil.findProjections(project)
                .stream()
                .filter(projection -> projection.getName().equals(this.projectionName))
                .collect(Collectors.toList());
        if (projections.size() == 2)
        {
            throw new AssertionError();
        }
        if (projections.size() == 1)
        {
            this.projection = projections.get(0);
            return new PsiElementResolveResult[]{new PsiElementResolveResult(this.projection)};
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
        List<KlassProjection> projections = KlassUtil.findProjections(project);
        List<LookupElement> variants = new ArrayList<>();
        for (KlassProjection projection : projections)
        {
            if (projection.getName() != null && !projection.getName().isEmpty())
            {
                LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(projection.getName())
                        .withIcon(AllIcons.Hierarchy.Subtypes)
                        .withTypeText(projection.getContainingFile().getName());
                variants.add(lookupElementBuilder);
            }
        }
        return variants.toArray();
    }

    @Override
    public PsiElement handleElementRename(String newElementName)
    {
        ASTNode node = this.myElement.getNode();
        if (node != null)
        {
            KlassProjectionName projectionReference = KlassElementFactory.createProjectionName(
                    this.myElement.getProject(),
                    newElementName);

            ASTNode newNode = projectionReference.getNode();
            node.getTreeParent().replaceChild(node, newNode);
        }
        return this.myElement;
    }
}
