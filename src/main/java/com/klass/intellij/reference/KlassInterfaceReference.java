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
import com.klass.intellij.psi.KlassInterfaceName;
import com.klass.intellij.psi.KlassKlass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassInterfaceReference extends PsiPolyVariantReferenceBase<PsiElement>
{
    private final String name;

    public KlassInterfaceReference(@NotNull PsiElement element, String name)
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
        ResolveResult[] interfaceResolveResults = KlassUtil.findInterfaces(project)
                .stream()
                .filter(klassInterface -> klassInterface.getName().equals(this.name))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
        if (interfaceResolveResults.length > 0)
        {
            return interfaceResolveResults;
        }

        return new ResolveResult[]{};
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        // TODO: Interfaces
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
            KlassInterfaceName interfaceName = KlassElementFactory.createInterfaceName(
                    this.myElement.getProject(),
                    newElementName);

            ASTNode newNode = interfaceName.getNode();
            node.getTreeParent().replaceChild(node, newNode);
        }
        return this.myElement;
    }
}
