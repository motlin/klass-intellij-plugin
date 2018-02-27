package com.klass.intellij.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.klass.intellij.psi.KlassTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KlassBlock extends AbstractBlock
{
    private static final Alignment COLON_ALIGNMENT = Alignment.createAlignment(true);

    private final SpacingBuilder spacingBuilder;

    protected KlassBlock(
            @NotNull ASTNode node,
            @Nullable Wrap wrap,
            @Nullable Alignment alignment,
            SpacingBuilder spacingBuilder)
    {
        super(node, wrap, alignment);
        this.spacingBuilder = spacingBuilder;
    }

    @Override
    protected List<Block> buildChildren()
    {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = this.myNode.getFirstChildNode();
        while (child != null)
        {
            IElementType elementType = child.getElementType();
            if (elementType != TokenType.WHITE_SPACE)
            {
                Wrap wrap = Wrap.createWrap(WrapType.NONE, false);
                Block block = new KlassBlock(child, wrap, this.getAlignment(elementType), this.spacingBuilder);
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    private Alignment getAlignment(IElementType elementType)
    {
        if (elementType == KlassTypes.COLON)
        {
            return COLON_ALIGNMENT;
        }
        return Alignment.createAlignment();
    }

    @Override
    public Indent getIndent()
    {
        IElementType elementType = this.getNode().getElementType();
        if (elementType == KlassTypes.CLASS)
        {
            return Indent.getNoneIndent();
        }
        if (elementType == KlassTypes.DATA_TYPE_PROPERTY)
        {
            return Indent.getNormalIndent();
        }
        if (elementType == KlassTypes.ASSOCIATION_END)
        {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2)
    {
        Spacing spacing = this.spacingBuilder.getSpacing(this, child1, child2);
        return spacing;
    }

    @Override
    public boolean isLeaf()
    {
        return this.myNode.getFirstChildNode() == null;
    }
}
