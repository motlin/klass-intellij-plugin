package com.klass.intellij.reference;

import java.util.ArrayList;
import java.util.List;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassAssociationEndName;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassKlassName;
import com.klass.intellij.psi.KlassTypedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassAssociationEndReference extends PsiPolyVariantReferenceBase<PsiElement>
{
    private final String associationEndName;

    public KlassAssociationEndReference(@NotNull PsiElement element, String associationEndName)
    {
        super(element, new TextRange(0, associationEndName.length()));
        this.associationEndName = associationEndName;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        PsiElement innerNode = this.myElement.getParent();
        KlassTypedElement klassTypedElement = (KlassTypedElement) innerNode.getParent();
        PsiElement type = klassTypedElement.getType();
        PsiReference reference = type.getReference();
        KlassKlass klassKlass = (KlassKlass) reference.resolve();

        if (klassKlass == null)
        {
            return new ResolveResult[]{};
        }

        List<KlassAssociation> associations = KlassUtil.findAssociations(klassKlass.getProject());
        for (KlassAssociation association : associations)
        {
            List<KlassAssociationEnd> associationEndList = association.getAssociationEndList();
            KlassAssociationEnd sourceEnd = associationEndList.get(0);
            KlassAssociationEnd targetEnd = associationEndList.get(1);

            String sourceName = sourceEnd.getName();
            String targetName = targetEnd.getName();

            KlassKlassName sourceTypeName = sourceEnd.getKlassName();
            KlassKlassName targetTypeName = targetEnd.getKlassName();

            KlassKlass sourceKlass = (KlassKlass) sourceTypeName.getReference().resolve();
            KlassKlass targetKlass = (KlassKlass) targetTypeName.getReference().resolve();

            if (sourceKlass == klassKlass && targetName.equals(this.associationEndName))
            {
                return new ResolveResult[]{new PsiElementResolveResult(targetEnd)};
            }

            if (targetKlass == klassKlass && sourceName.equals(this.associationEndName))
            {
                return new ResolveResult[]{new PsiElementResolveResult(sourceEnd)};
            }
        }

        // TODO: Handle inferred associationEnd 'version'

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
        List<KlassKlass> klassKlasses = KlassUtil.findClasses(project);
        List<LookupElement> variants = new ArrayList<>();
        for (KlassKlass klassKlass : klassKlasses)
        {
            if (klassKlass.getName() != null && !klassKlass.getName().isEmpty())
            {
                LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(klassKlass.getName())
                        .withIcon(AllIcons.Nodes.Class)
                        .withTypeText(klassKlass.getContainingFile().getName());
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
            KlassAssociationEndName associationEndName = KlassElementFactory.createAssociationEndName(
                    this.myElement.getProject(),
                    newElementName);

            ASTNode newNode = associationEndName.getNode();
            node.getTreeParent().replaceChild(node, newNode);
        }
        return this.myElement;
    }
}
