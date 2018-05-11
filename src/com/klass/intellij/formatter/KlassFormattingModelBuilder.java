package com.klass.intellij.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.SpacingBuilder.RuleBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.klass.intellij.KlassLanguage;
import com.klass.intellij.psi.KlassTokenType;
import com.klass.intellij.psi.KlassTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassFormattingModelBuilder implements FormattingModelBuilder
{
    public static final TokenSet NONE_BEFORE = TokenSet.create(
            KlassTypes.OPTIONAL_MARKER,
            KlassTypes.COMMA,
            KlassTypes.RBRACKET,
            KlassTypes.RPAREN,
            KlassTypes.MULTIPLICITY,
            KlassTypes.COLON);
    public static final TokenSet NONE_AROUND = TokenSet.create(
            KlassTypes.URL_CONSTANT,
            KlassTypes.URL_PART,
            KlassTypes.DOT,
            KlassTypes.DOTDOT,
            KlassTypes.LBRACKET,
            KlassTypes.LPAREN);
    public static final TokenSet NONE_INSIDE = TokenSet.create(
            KlassTypes.URL,
            KlassTypes.URL_PART,
            KlassTypes.QUERY_PARAM_PART,
            KlassTypes.NOMBRE);

    public static final TokenSet BLANK_LINE_BEFORE = TokenSet.create(
            KlassTypes.RELATIONSHIP,
            KlassTokenType.END_OF_LINE_COMMENT);
    public static final TokenSet BLANK_LINE_AROUND = TokenSet.create(
            KlassTypes.KLASS,
            KlassTypes.ENUMERATION,
            KlassTypes.ASSOCIATION,
            KlassTypes.PROJECTION,
            KlassTypes.SERVICE_GROUP);

    public static final TokenSet LINE_BREAK_BEFORE = TokenSet.create(
            KlassTypes.ANDAND,
            KlassTypes.OROR);
    public static final TokenSet LINE_BREAK_AFTER = TokenSet.create(
            KlassTypes.COMMA,
            KlassTokenType.END_OF_LINE_COMMENT);

    public static final TokenSet ONE_SPACE_BEFORE = TokenSet.create(
            KlassTypes.NOMBRE,
            KlassTypes.ORDER_BY_DIRECTION);
    public static final TokenSet ONE_SPACE_AFTER = TokenSet.create(
            KlassTypes.COLON,
            KlassTypes.ANDAND,
            KlassTypes.OROR,
            KlassTypes.RELATIONSHIP_KEYWORD,
            KlassTypes.SERVICE_KEYWORD,
            KlassTypes.PROPERTY_MODIFIER);
    public static final TokenSet ONE_SPACE_AROUND = TokenSet.create(
            KlassTypes.OPERATOR,
            KlassTypes.ON_KEYWORD);
    public static final TokenSet BRACES = TokenSet.create(
            KlassTypes.LBRACE,
            KlassTypes.RBRACE,
            KlassTypes.L_BRACE,
            KlassTypes.R_BRACE);
    public static final TokenSet LINE_BREAK_AROUND_HIGH_PRIORITY = TokenSet.create(
            KlassTypes.CLASS_MODIFIER,
            KlassTypes.SERVICE_PROJECTION,
            KlassTypes.SERVICE_CATEGORY,
            KlassTypes.VERB);
    public static final TokenSet LINE_BREAK_AROUND = TokenSet.create(
            KlassTypes.RELATIONSHIP,
            KlassTypes.ORDER_BY_CLAUSE,
            KlassTypes.CLASS_MODIFIER,
            KlassTypes.PRIMITIVE_TYPE_PROPERTY,
            KlassTypes.ENUMERATION_PROPERTY,
            KlassTypes.ASSOCIATION_END,
            KlassTypes.PROJECTION_LEAF_NODE,
            KlassTypes.PROJECTION_ASSOCIATION_END_NODE,
            KlassTypes.PROJECTION_PARAMETERIZED_PROPERTY_NODE,
            KlassTypes.SERVICE_MULTIPLICITY_CLAUSE,
            KlassTypes.SERVICE_CRITERIA_CLAUSE,
            KlassTypes.SERVICE_PROJECTION_CLAUSE,
            KlassTypes.SERVICE,
            KlassTypes.URL,
            KlassTypes.URL_GROUP);

    public static final TokenSet INDENT_CHILDREN = TokenSet.create(
            KlassTypes.KLASS,
            KlassTypes.ASSOCIATION,
            KlassTypes.ENUMERATION,
            KlassTypes.PROJECTION,
            KlassTypes.PROJECTION_ASSOCIATION_END_NODE,
            KlassTypes.PROJECTION_PARAMETERIZED_PROPERTY_NODE,
            KlassTypes.SERVICE,
            KlassTypes.SERVICE_GROUP,
            KlassTypes.URL_GROUP,
            KlassTypes.PARAMETERIZED_PROPERTY);

    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings)
    {
        return FormattingModelProvider.createFormattingModelForPsiFile(
                element.getContainingFile(),
                new KlassBlock(
                        element.getNode(),
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        KlassFormattingModelBuilder.createSpaceBuilder(settings),
                        settings,
                        this.getChildIndent(element),
                        null,
                        null,
                        null),
                settings);
    }

    @Nullable
    private Indent getChildIndent(PsiElement element)
    {
        IElementType elementType = element.getNode().getElementType();

        if (INDENT_CHILDREN.contains(elementType))
        {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings)
    {
        CommonCodeStyleSettings commonSettings = settings.getCommonSettings(KlassLanguage.INSTANCE);

        // TODO: KLASS_KEYWORD and KLASS_KLASS_KEYWORD conflict
        SpacingBuilder spacingBuilder = new SpacingBuilder(settings, KlassLanguage.INSTANCE)
                .before(NONE_BEFORE).none()
                .around(NONE_AROUND).none()
                .aroundInside(TokenSet.ANY, NONE_INSIDE).none()

                .after(TokenSet.create(
                        KlassTokenType.END_OF_LINE_COMMENT,
                        KlassTypes.LBRACE,
                        KlassTypes.L_BRACE)).lineBreakInCode()
                .before(TokenSet.create(KlassTypes.RBRACE, KlassTypes.R_BRACE)).lineBreakInCode()

                .around(BLANK_LINE_AROUND).blankLines(1)
                .before(BLANK_LINE_BEFORE).blankLines(1)

                .before(LINE_BREAK_BEFORE).lineBreakInCode()
                .afterInside(KlassTypes.COMMA, KlassTypes.EXPRESSION_LITERAL_LIST).spaces(1)
                .after(LINE_BREAK_AFTER).lineBreakInCode()
                .around(BRACES).lineBreakInCode()
                .around(LINE_BREAK_AROUND_HIGH_PRIORITY).lineBreakInCode()

                .before(KlassTokenType.END_OF_LINE_COMMENT).blankLines(1)
                .around(KlassTypes.PARAMETERIZED_PROPERTY).blankLines(1)

                .around(LINE_BREAK_AROUND).lineBreakInCode()

                .before(ONE_SPACE_BEFORE).spaces(1)
                .after(ONE_SPACE_AFTER).spaces(1)
                .around(ONE_SPACE_AROUND).spaces(1);

        RuleBuilder colonRuleBuilder = spacingBuilder.before(KlassTypes.COLON);
        if (commonSettings.ALIGN_GROUP_FIELD_DECLARATIONS)
        {
            colonRuleBuilder.spacing(0, Integer.MAX_VALUE, 0, false, 0);
        }
        else
        {
            colonRuleBuilder.none();
        }

        return spacingBuilder;
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset)
    {
        return null;
    }
}
