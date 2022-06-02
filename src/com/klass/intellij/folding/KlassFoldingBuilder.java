package com.klass.intellij.folding;

import java.util.ArrayList;
import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.klass.intellij.psi.KlassAssociationBlock;
import com.klass.intellij.psi.KlassClassBlock;
import com.klass.intellij.psi.KlassEnumerationBlock;
import com.klass.intellij.psi.KlassInterfaceBlock;
import com.klass.intellij.psi.KlassLBrace;
import com.klass.intellij.psi.KlassProjectionBlock;
import com.klass.intellij.psi.KlassRBrace;
import com.klass.intellij.psi.KlassServiceBlock;
import com.klass.intellij.psi.KlassServiceGroupBlock;
import com.klass.intellij.psi.KlassTokenType;
import com.klass.intellij.psi.KlassTypes;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassFoldingBuilder implements FoldingBuilder, DumbAware
{
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document)
    {
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        KlassFoldingBuilder.collectDescriptorsRecursively(node, document, descriptors);
        return descriptors.toArray(FoldingDescriptor.EMPTY);
    }

    private static void collectDescriptorsRecursively(
            @NotNull ASTNode node,
            @NotNull Document document,
            @NotNull List<FoldingDescriptor> descriptors)
    {
        PsiElement element = node.getPsi();

        if (hasBlock(element) && KlassFoldingBuilder.spanMultipleLines(node, document))
        {
            TextRange textRange = getTextRange(element);
            descriptors.add(new FoldingDescriptor(node, textRange));
        }

        IElementType type = node.getElementType();
        if (type == KlassTokenType.C_STYLE_COMMENT)
        {
            descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
        }
        else if (type == KlassTokenType.END_OF_LINE_COMMENT)
        {
            Pair<PsiElement, PsiElement> commentRange = KlassFoldingBuilder.expandLineCommentsRange(element);

            int startOffset = commentRange.getOne().getTextRange().getStartOffset();
            int endOffset = commentRange.getTwo().getTextRange().getEndOffset();
            if (document.getLineNumber(startOffset) != document.getLineNumber(endOffset))
            {
                descriptors.add(new FoldingDescriptor(node, new TextRange(startOffset, endOffset)));
            }
        }

        for (ASTNode child : node.getChildren(null))
        {
            KlassFoldingBuilder.collectDescriptorsRecursively(child, document, descriptors);
        }
    }

    private static boolean hasBlock(PsiElement psiElement)
    {
        return psiElement instanceof KlassClassBlock
                || psiElement instanceof KlassInterfaceBlock
                || psiElement instanceof KlassEnumerationBlock
                || psiElement instanceof KlassAssociationBlock
                || psiElement instanceof KlassProjectionBlock
                || psiElement instanceof KlassServiceGroupBlock
                || psiElement instanceof KlassServiceBlock;
    }

    @NotNull
    private static TextRange getTextRange(@NotNull PsiElement psiElement)
    {
        if (psiElement instanceof KlassClassBlock)
        {
            KlassLBrace lBrace    = ((KlassClassBlock) psiElement).getLBrace();
            KlassRBrace rBrace    = ((KlassClassBlock) psiElement).getRBrace();
            return new TextRange(lBrace.getTextOffset(), rBrace.getTextOffset() + 1);
        }

        if (psiElement instanceof KlassInterfaceBlock)
        {
            KlassLBrace lBrace    = ((KlassInterfaceBlock) psiElement).getLBrace();
            KlassRBrace rBrace    = ((KlassInterfaceBlock) psiElement).getRBrace();
            return new TextRange(lBrace.getTextOffset(), rBrace.getTextOffset() + 1);
        }

        if (psiElement instanceof KlassEnumerationBlock)
        {
            KlassLBrace lBrace    = ((KlassEnumerationBlock) psiElement).getLBrace();
            KlassRBrace rBrace    = ((KlassEnumerationBlock) psiElement).getRBrace();
            return new TextRange(lBrace.getTextOffset(), rBrace.getTextOffset() + 1);
        }

        if (psiElement instanceof KlassAssociationBlock)
        {
            KlassLBrace lBrace    = ((KlassAssociationBlock) psiElement).getLBrace();
            KlassRBrace rBrace    = ((KlassAssociationBlock) psiElement).getRBrace();
            return new TextRange(lBrace.getTextOffset(), rBrace.getTextOffset() + 1);
        }

        if (psiElement instanceof KlassProjectionBlock)
        {
            KlassLBrace lBrace    = ((KlassProjectionBlock) psiElement).getLBrace();
            KlassRBrace rBrace    = ((KlassProjectionBlock) psiElement).getRBrace();
            return new TextRange(lBrace.getTextOffset(), rBrace.getTextOffset() + 1);
        }

        if (psiElement instanceof KlassServiceGroupBlock)
        {
            KlassLBrace lBrace    = ((KlassServiceGroupBlock) psiElement).getLBrace();
            KlassRBrace rBrace    = ((KlassServiceGroupBlock) psiElement).getRBrace();
            return new TextRange(lBrace.getTextOffset(), rBrace.getTextOffset() + 1);
        }

        if (psiElement instanceof KlassServiceBlock)
        {
            KlassLBrace lBrace    = ((KlassServiceBlock) psiElement).getLBrace();
            KlassRBrace rBrace    = ((KlassServiceBlock) psiElement).getRBrace();
            return new TextRange(lBrace.getTextOffset(), rBrace.getTextOffset() + 1);
        }

        throw new AssertionError(psiElement);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node)
    {
        IElementType type = node.getElementType();
        if (type == KlassTypes.CLASS_BLOCK
                || type == KlassTypes.INTERFACE_BLOCK
                || type == KlassTypes.ENUMERATION_BLOCK
                || type == KlassTypes.ASSOCIATION_BLOCK
                || type == KlassTypes.PROJECTION_BLOCK
                || type == KlassTypes.SERVICE_GROUP_BLOCK
                || type == KlassTypes.SERVICE_BLOCK)
        {
            return "{...}";
        }
        if (type == KlassTokenType.END_OF_LINE_COMMENT)
        {
            return "//...";
        }
        if (type == KlassTokenType.C_STYLE_COMMENT)
        {
            return "/*...*/";
        }
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node)
    {
        return false;
    }

    @NotNull
    public static Pair<PsiElement, PsiElement> expandLineCommentsRange(@NotNull PsiElement anchor)
    {
        return Tuples.pair(KlassFoldingBuilder.findFurthestSiblingOfSameType(anchor, false), KlassFoldingBuilder.findFurthestSiblingOfSameType(anchor, true));
    }

    @NotNull
    public static PsiElement findFurthestSiblingOfSameType(@NotNull PsiElement anchor, boolean after)
    {
        ASTNode node = anchor.getNode();
        // Compare by node type to distinguish between different types of comments
        IElementType expectedType = node.getElementType();
        ASTNode lastSeen = node;
        while (node != null)
        {
            IElementType elementType = node.getElementType();
            if (elementType == expectedType)
            {
                lastSeen = node;
            }
            else if (elementType == TokenType.WHITE_SPACE)
            {
                if (expectedType == KlassTokenType.END_OF_LINE_COMMENT && node.getText().indexOf('\n', 1) != -1)
                {
                    break;
                }
            }
            node = after ? node.getTreeNext() : node.getTreePrev();
        }
        return lastSeen.getPsi();
    }

    private static boolean spanMultipleLines(@NotNull ASTNode node, @NotNull Document document)
    {
        TextRange range = node.getTextRange();
        return document.getLineNumber(range.getStartOffset()) < document.getLineNumber(range.getEndOffset());
    }
}
