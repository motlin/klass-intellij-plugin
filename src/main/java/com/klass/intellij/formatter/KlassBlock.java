package com.klass.intellij.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
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
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassBlock extends AbstractBlock {
  private static final Alignment COLON_ALIGNMENT = Alignment.createAlignment(true);

  private static final TokenSet NORMAL_INDENT =
      TokenSet.create(
          KlassTypes.PRIMITIVE_TYPE_PROPERTY,
          KlassTypes.ENUMERATION_PROPERTY,
          KlassTypes.PARAMETERIZED_PROPERTY,
          KlassTypes.PARAMETERIZED_PROPERTY_SIGNATURE,
          KlassTypes.ASSOCIATION_END,
          KlassTypes.ASSOCIATION_END_SIGNATURE,
          KlassTypes.ENUMERATION_LITERAL,
          KlassTypes.PROJECTION_ASSOCIATION_END_NODE,
          KlassTypes.PROJECTION_PARAMETERIZED_PROPERTY_NODE,
          KlassTypes.PROJECTION_LEAF_NODE,
          KlassTypes.PROJECTION_PROJECTION_NODE,
          KlassTypes.URL_GROUP,
          KlassTypes.SERVICE,
          KlassTypes.SERVICE_MULTIPLICITY_CLAUSE,
          KlassTypes.SERVICE_CRITERIA_CLAUSE,
          KlassTypes.SERVICE_PROJECTION_CLAUSE,
          KlassTypes.SERVICE_VALIDATE_CLAUSE,
          KlassTypes.SERVICE_AUTHORIZE_CLAUSE,
          KlassTypes.SERVICE_CONFLICT_CLAUSE,
          KlassTypes.CRITERIA_OPERATOR,
          KlassTypes.SERVICE_PROJECTION,
          KlassTypes.ORDER_BY_CLAUSE,
          KlassTypes.CLASS_MODIFIER,
          KlassTypes.RELATIONSHIP,
          KlassTypes.ABSTRACT_CLAUSE,
          KlassTypes.IMPLEMENTS_CLAUSE,
          KlassTypes.EXTENDS_CLAUSE);

  private static final TokenSet NORMAL_INDENT_CHILDREN =
      TokenSet.create(
          KlassTypes.INTERFACE,
          KlassTypes.KLASS,
          KlassTypes.ASSOCIATION,
          KlassTypes.ENUMERATION,
          KlassTypes.PROJECTION,
          KlassTypes.SERVICE,
          KlassTypes.SERVICE_GROUP,
          KlassTypes.INTERFACE_BLOCK,
          KlassTypes.CLASS_BLOCK,
          KlassTypes.ASSOCIATION_BLOCK,
          KlassTypes.ENUMERATION_BLOCK,
          KlassTypes.PROJECTION_BLOCK,
          KlassTypes.SERVICE_BLOCK,
          KlassTypes.SERVICE_GROUP_BLOCK,
          KlassTypes.INTERFACE_BODY,
          KlassTypes.CLASS_BODY,
          KlassTypes.ASSOCIATION_BODY,
          KlassTypes.ENUMERATION_BODY,
          KlassTypes.PROJECTION_BODY,
          KlassTypes.SERVICE_BODY,
          KlassTypes.SERVICE_GROUP_BODY,
          KlassTypes.PROJECTION_ASSOCIATION_END_NODE,
          KlassTypes.PROJECTION_PARAMETERIZED_PROPERTY_NODE,
          KlassTypes.URL_GROUP,
          KlassTypes.PARAMETERIZED_PROPERTY,
          KlassTypes.CRITERIA_AND,
          KlassTypes.CRITERIA_OR);

  private static final Wrap WRAP = Wrap.createWrap(WrapType.NONE, false);

  private final SpacingBuilder spacingBuilder;
  private final CodeStyleSettings settings;
  private final CommonCodeStyleSettings commonSettings;
  private final Indent childIndent;
  private final Alignment serviceColonAlignment;
  private final Alignment parentProjectionColonAlignment;
  private final Alignment projectionColonAlignment;

  protected KlassBlock(
      @NotNull ASTNode node,
      @Nullable Wrap wrap,
      @Nullable Alignment alignment,
      SpacingBuilder spacingBuilder,
      CodeStyleSettings settings,
      Indent childIndent,
      Alignment serviceColonAlignment,
      Alignment parentProjectionColonAlignment,
      Alignment projectionColonAlignment) {
    super(node, wrap, alignment);
    this.spacingBuilder = spacingBuilder;
    this.settings = settings;
    this.commonSettings = settings.getCommonSettings(KlassLanguage.INSTANCE);
    this.childIndent = childIndent;
    this.serviceColonAlignment = serviceColonAlignment;
    this.parentProjectionColonAlignment = parentProjectionColonAlignment;
    this.projectionColonAlignment = projectionColonAlignment;
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    ASTNode child = this.myNode.getFirstChildNode();
    while (child != null) {
      IElementType elementType = child.getElementType();
      if (elementType != TokenType.WHITE_SPACE) {
        Block block =
            new KlassBlock(
                child,
                WRAP,
                this.getAlignment(elementType),
                this.spacingBuilder,
                this.settings,
                this.getIndentForChildren(child),
                this.getChildColonAlignment(elementType),
                this.getProjectionColonAlignment(elementType),
                this.parentProjectionColonAlignment);
        blocks.add(block);
      }
      child = child.getTreeNext();
    }
    return blocks;
  }

  private Alignment getAlignment(IElementType elementType) {
    if (elementType != KlassTypes.COLON) {
      return null;
    }

    IElementType parentElementType = this.myNode.getElementType();
    if (parentElementType == KlassTypes.PRIMITIVE_TYPE_PROPERTY
        || parentElementType == KlassTypes.ENUMERATION_PROPERTY
        || parentElementType == KlassTypes.ASSOCIATION_END_SIGNATURE
        || parentElementType == KlassTypes.PARAMETERIZED_PROPERTY_SIGNATURE
        || parentElementType == KlassTypes.ASSOCIATION_END) {
      return this.commonSettings.ALIGN_GROUP_FIELD_DECLARATIONS ? COLON_ALIGNMENT : null;
    }

    ASTNode treeParent = this.myNode.getTreeParent();
    if (treeParent == null) {
      return null;
    }

    IElementType grandparentElementType = treeParent.getElementType();
    if (grandparentElementType == KlassTypes.SERVICE_BODY) {
      return this.commonSettings.ALIGN_GROUP_FIELD_DECLARATIONS ? this.serviceColonAlignment : null;
    }

    if (parentElementType == KlassTypes.PROJECTION
        || parentElementType == KlassTypes.PROJECTION_ASSOCIATION_END_NODE
        || parentElementType == KlassTypes.PROJECTION_PARAMETERIZED_PROPERTY_NODE
        || parentElementType == KlassTypes.PROJECTION_LEAF_NODE
        || parentElementType == KlassTypes.PROJECTION_PROJECTION_NODE) {
      return this.commonSettings.ALIGN_GROUP_FIELD_DECLARATIONS
          ? this.projectionColonAlignment
          : null;
    }

    return null;
  }

  private Indent getIndentForChildren(ASTNode astNode) {
    IElementType elementType = astNode.getElementType();
    if (NORMAL_INDENT_CHILDREN.contains(elementType)) {
      return Indent.getNormalIndent();
    }
    return Indent.getNoneIndent();
  }

  private Alignment getChildColonAlignment(IElementType elementType) {
    if (elementType == KlassTypes.SERVICE_BODY) {
      return Alignment.createAlignment(true);
    }
    return this.serviceColonAlignment;
  }

  private Alignment getProjectionColonAlignment(IElementType elementType) {
    if (elementType == KlassTypes.PROJECTION_BODY) {
      return Alignment.createAlignment(true);
    }
    return null;
  }

  @Override
  public Indent getIndent() {
    IElementType elementType = this.getNode().getElementType();
    if (NORMAL_INDENT.contains(elementType)) {
      return Indent.getNormalIndent();
    }
    if (elementType == KlassTypes.ANDAND || elementType == KlassTypes.OROR) {
      return Indent.getContinuationIndent();
    }
    if (elementType == KlassTokenType.END_OF_LINE_COMMENT
        || elementType == KlassTokenType.C_STYLE_COMMENT) {
      return this.getIndentForChildren(this.getNode().getTreeParent());
    }
    return Indent.getNoneIndent();
  }

  @NotNull @Override
  public ChildAttributes getChildAttributes(int newChildIndex) {
    return new ChildAttributes(this.childIndent, null);
  }

  @Nullable @Override
  public Indent getChildIndent() {
    return this.childIndent;
  }

  @Nullable @Override
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
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
  public boolean isLeaf() {
    return this.myNode.getFirstChildNode() == null;
  }
}
