package com.klass.intellij.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.klass.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

        if (element instanceof KlassKlass && KlassFoldingBuilder.spanMultipleLines(node, document))
        {
            KlassLBrace lBrace = ((KlassKlass) element).getLBrace();
            KlassRBrace rBrace = ((KlassKlass) element).getRBrace();
            TextRange textRange = new TextRange(lBrace.getTextOffset(), rBrace.getTextOffset() + 1);
            descriptors.add(new FoldingDescriptor(node, textRange));
        }
        else if (element instanceof KlassEnumeration && KlassFoldingBuilder.spanMultipleLines(node, document))
        {
            KlassLBrace lBrace = ((KlassEnumeration) element).getLBrace();
            KlassRBrace rBrace = ((KlassEnumeration) element).getRBrace();
            TextRange textRange = new TextRange(lBrace.getTextOffset(), rBrace.getTextOffset() + 1);
            descriptors.add(new FoldingDescriptor(node, textRange));
        }

        IElementType type = node.getElementType();
        if (type == KlassTokenType.C_STYLE_COMMENT)
        {
            descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
        }
        else if (type == KlassTokenType.END_OF_LINE_COMMENT)
        {
            Couple<PsiElement> commentRange = KlassFoldingBuilder.expandLineCommentsRange(element);
            int startOffset = commentRange.getFirst().getTextRange().getStartOffset();
            int endOffset = commentRange.getSecond().getTextRange().getEndOffset();
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

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node)
    {
        IElementType type = node.getElementType();
        if (type == KlassTypes.KLASS || type == KlassTypes.ENUMERATION)
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
    public static Couple<PsiElement> expandLineCommentsRange(@NotNull PsiElement anchor)
    {
        return Couple.of(KlassFoldingBuilder.findFurthestSiblingOfSameType(anchor, false), KlassFoldingBuilder.findFurthestSiblingOfSameType(anchor, true));
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
