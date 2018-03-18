package com.klass.intellij.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.klass.intellij.KlassLanguage;
import com.klass.intellij.psi.KlassTokenType;
import com.klass.intellij.psi.KlassTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KlassBlock extends AbstractBlock
{
    private static final Alignment COLON_ALIGNMENT = Alignment.createAlignment(true);

    private final SpacingBuilder spacingBuilder;
    private final CodeStyleSettings settings;
    private final CommonCodeStyleSettings commonSettings;
    private final Indent childIndent;

    protected KlassBlock(
            @NotNull ASTNode node,
            @Nullable Wrap wrap,
            @Nullable Alignment alignment,
            SpacingBuilder spacingBuilder,
            CodeStyleSettings settings,
            Indent childIndent)
    {
        super(node, wrap, alignment);
        this.spacingBuilder = spacingBuilder;
        this.settings = settings;
        this.commonSettings = settings.getCommonSettings(KlassLanguage.INSTANCE);
        this.childIndent = childIndent;
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
                Block block =
                        new KlassBlock(child, wrap, this.getAlignment(elementType), this.spacingBuilder, this.settings, this.getIndentForChildren(child));
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    private Alignment getAlignment(IElementType elementType)
    {
        if (elementType == KlassTypes.COLON && this.commonSettings.ALIGN_GROUP_FIELD_DECLARATIONS)
        {
            return COLON_ALIGNMENT;
        }
        return null;
    }

    @Override
    public Indent getIndent()
    {
        IElementType elementType = this.getNode().getElementType();
        if (elementType == KlassTypes.DATA_TYPE_PROPERTY
                || elementType == KlassTypes.ENUMERATION_PROPERTY
                || elementType == KlassTypes.PARAMETERIZED_PROPERTY
                || elementType == KlassTypes.ASSOCIATION_END
                || elementType == KlassTypes.ENUMERATION_LITERAL
                || elementType == KlassTypes.PROJECTION_INNER_NODE
                || elementType == KlassTypes.PROJECTION_LEAF_NODE
                || elementType == KlassTypes.URL_GROUP
                || elementType == KlassTypes.SERVICE
                || elementType == KlassTypes.SERVICE_MULTIPLICITY_CLAUSE
                || elementType == KlassTypes.SERVICE_CRITERIA_CLAUSE
                || elementType == KlassTypes.SERVICE_PROJECTION_CLAUSE
                || elementType == KlassTypes.CRITERIA_OPERATOR)
        {
            return Indent.getNormalIndent();
        }
        if (elementType == KlassTokenType.END_OF_LINE_COMMENT
                || elementType == KlassTokenType.C_STYLE_COMMENT)
        {
            return this.getIndentForChildren(this.getNode().getTreeParent());
        }
        return Indent.getNoneIndent();
    }

    private Indent getIndentForChildren(ASTNode astNode)
    {
        IElementType elementType = astNode.getElementType();
        if (elementType == KlassTypes.KLASS
                || elementType == KlassTypes.ASSOCIATION
                || elementType == KlassTypes.ENUMERATION
                || elementType == KlassTypes.PROJECTION
                || elementType == KlassTypes.PROJECTION_INNER_NODE
                || elementType == KlassTypes.SERVICE_GROUP
                || elementType == KlassTypes.URL_GROUP
                || elementType == KlassTypes.SERVICE
                || elementType == KlassTypes.PARAMETERIZED_PROPERTY)
        {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }

    @Nullable
    @Override
    public Indent getChildIndent()
    {
        return this.childIndent;
    }

    @NotNull
    @Override
    public ChildAttributes getChildAttributes(int newChildIndex)
    {
        return new ChildAttributes(this.childIndent, null);
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2)
    {
        Spacing spacing = this.spacingBuilder.getSpacing(this, child1, child2);
        // return Spacing.createSpacing(3, 3, 3, false, 3, 3);
        return spacing;
    }

    @Override
    public boolean isLeaf()
    {
        return this.myNode.getFirstChildNode() == null;
    }
}
