package com.klass.intellij.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
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

    private static final TokenSet NORMAL_INDENT = TokenSet.create(
            KlassTypes.DATA_TYPE_PROPERTY,
            KlassTypes.ENUMERATION_PROPERTY,
            KlassTypes.PARAMETERIZED_PROPERTY,
            KlassTypes.ASSOCIATION_END,
            KlassTypes.ENUMERATION_LITERAL,
            KlassTypes.PROJECTION_INNER_NODE,
            KlassTypes.PROJECTION_LEAF_NODE,
            KlassTypes.URL_GROUP,
            KlassTypes.SERVICE,
            KlassTypes.SERVICE_MULTIPLICITY_CLAUSE,
            KlassTypes.SERVICE_CRITERIA_CLAUSE,
            KlassTypes.SERVICE_PROJECTION_CLAUSE,
            KlassTypes.SERVICE_VALIDATE_CLAUSE,
            KlassTypes.SERVICE_AUTHORIZE_CLAUSE,
            KlassTypes.CRITERIA_OPERATOR,
            KlassTypes.SERVICE_PROJECTION,
            KlassTypes.ORDER_BY_CLAUSE,
            KlassTypes.KEYWORD_ON_CLASS,
            KlassTypes.RELATIONSHIP);

    public static final TokenSet NORMAL_INDENT_CHILDREN = TokenSet.create(
            KlassTypes.KLASS,
            KlassTypes.ASSOCIATION,
            KlassTypes.ENUMERATION,
            KlassTypes.PROJECTION,
            KlassTypes.PROJECTION_INNER_NODE,
            KlassTypes.SERVICE_GROUP,
            KlassTypes.URL_GROUP,
            KlassTypes.SERVICE,
            KlassTypes.PARAMETERIZED_PROPERTY,
            KlassTypes.CRITERIA_AND,
            KlassTypes.CRITERIA_OR);
    public static final Wrap WRAP = Wrap.createWrap(WrapType.NONE, false);

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
                Block block =
                        new KlassBlock(
                                child,
                                WRAP,
                                this.getAlignment(elementType),
                                this.spacingBuilder,
                                this.settings,
                                this.getIndentForChildren(child));
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    private Alignment getAlignment(IElementType elementType)
    {
        if (elementType != KlassTypes.COLON)
        {
            return null;
        }

        IElementType parentElementType = this.myNode.getElementType();
        if ((parentElementType == KlassTypes.DATA_TYPE_PROPERTY
                || parentElementType == KlassTypes.ENUMERATION_PROPERTY)
                && this.commonSettings.ALIGN_GROUP_FIELD_DECLARATIONS)
        {
            return COLON_ALIGNMENT;
        }

        return null;
    }

    @Override
    public Indent getIndent()
    {
        IElementType elementType = this.getNode().getElementType();
        if (NORMAL_INDENT.contains(elementType))
        {
            return Indent.getNormalIndent();
        }
        if (elementType == KlassTypes.ANDAND
                || elementType == KlassTypes.OROR)
        {
            return Indent.getContinuationIndent();
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
        if (NORMAL_INDENT_CHILDREN.contains(elementType))
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
        /*
        if (child1 instanceof KlassBlock)
        {
            if (((KlassBlock) child1).myNode.getElementType() == KlassTokenType.END_OF_LINE_COMMENT)
            {
                return Spacing.createSpacing(3, 3, 3, false, 3, 3);
            }
        }
        return Spacing.createSpacing(3, 3, 0, false, 0);
        */
        // return Spacing.createSpacing(3, 3, 3, false, 3, 3);
        return spacing;
    }

    @Override
    public boolean isLeaf()
    {
        return this.myNode.getFirstChildNode() == null;
    }
}
