package com.klass.intellij.reference;

import java.util.ArrayList;
import java.util.List;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
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
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassKlassName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassKlassReference extends PsiPolyVariantReferenceBase<PsiElement>
{
    private final String name;

    public KlassKlassReference(@NotNull PsiElement element, String name)
    {
        super(element, new TextRange(0, name.length()));
        this.name = name;
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
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        Project project = this.myElement.getProject();
        ResolveResult[] klassResolveResults = KlassUtil.findClasses(project)
                .stream()
                .filter(klass -> klass.getName().equals(this.name))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
        if (klassResolveResults.length > 0)
        {
            return klassResolveResults;
        }

        ResolveResult[] enumerationResolveResults = KlassUtil.findEnumerations(project)
                .stream()
                .filter(enumeration -> enumeration.getName().equals(this.name))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
        if (enumerationResolveResults.length > 0)
        {
            return enumerationResolveResults;
        }

        return new ResolveResult[]{};
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        Project               project       = this.myElement.getProject();
        List<KlassKlass>      klassKlasses  = KlassUtil.findClasses(project);
        List<LookupElement>   variants      = new ArrayList<>();
        BracketsInsertHandler insertHandler = new BracketsInsertHandler();
        for (KlassKlass klassKlass : klassKlasses)
        {
            if (klassKlass.getName() != null && !klassKlass.getName().isEmpty())
            {
                LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(klassKlass.getName())
                        .withIcon(AllIcons.Nodes.Class)
                        .withTypeText(klassKlass.getContainingFile().getName())
                        .withInsertHandler(insertHandler);
                variants.add(lookupElementBuilder);
            }
        }
        return variants.toArray();
    }

    private static class BracketsInsertHandler extends ParenthesesInsertHandler<LookupElement>
    {
        private BracketsInsertHandler()
        {
            super(false, false, true, false, '[', ']');
        }

        @Override
        protected boolean placeCaretInsideParentheses(InsertionContext context, LookupElement item)
        {
            return true;
        }
    }

    @Override
    public PsiElement handleElementRename(String newElementName)
    {
        ASTNode node = this.myElement.getNode();
        if (node != null)
        {
            KlassKlassName klassName = KlassElementFactory.createKlassName(
                    this.myElement.getProject(),
                    newElementName);

            ASTNode newNode = klassName.getNode();
            node.getTreeParent().replaceChild(node, newNode);
        }
        return this.myElement;
    }
}
