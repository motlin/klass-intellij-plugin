package com.klass.intellij.formatter;

import com.intellij.formatting.*;
import com.intellij.formatting.SpacingBuilder.RuleBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import com.klass.intellij.KlassLanguage;
import com.klass.intellij.lexer.TokenSets;
import com.klass.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassFormattingModelBuilder implements FormattingModelBuilder
{
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
                        this.getChildIndent(element)),
                settings);
    }

    @Nullable
    private Indent getChildIndent(PsiElement element)
    {
        if (element instanceof KlassKlass
                || element instanceof KlassAssociation
                || element instanceof KlassEnumeration
                || element instanceof KlassProjection
                || element instanceof KlassProjectionInnerNode
                || element instanceof KlassService
                || element instanceof KlassServiceGroup
                || element instanceof KlassUrlGroup)
        {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings)
    {
        CommonCodeStyleSettings commonSettings = settings.getCommonSettings(KlassLanguage.INSTANCE);

        TokenSet keywords = TokenSet.orSet(TokenSets.KEYWORD_BIT_SET, TokenSet.create(KlassTypes.KLASS_KEYWORD));
        SpacingBuilder spacingBuilder = new SpacingBuilder(settings, KlassLanguage.INSTANCE)
                .before(TokenSet.create(KlassTypes.OPTIONAL_MARKER, KlassTypes.COMMA)).none()
                .around(TokenSet.create(KlassTypes.DOT, KlassTypes.DOTDOT)).none()
                .around(TokenSet.create(KlassTypes.LBRACKET, KlassTypes.LPAREN)).none()
                .before(TokenSet.create(KlassTypes.RBRACKET, KlassTypes.RPAREN)).none()
                .before(KlassTypes.MULTIPLICITY).none()
                .around(TokenSet.create(KlassTypes.URL_CONSTANT, KlassTypes.URL_PART, KlassTypes.PATH_PARAMETER)).none()
                .aroundInside(TokenSet.create(KlassTypes.SLASH, KlassTypes.LBRACE, KlassTypes.RBRACE), KlassTypes.URL_PART).none()
                .before(KlassTypes.NOMBRE).spaces(1)
                .aroundInside(KlassTypes.TICK, KlassTypes.NOMBRE).none()
                .aroundInside(TokenSet.create(KlassTypes.SLASH, KlassTypes.URL_CONSTANT, KlassTypes.PATH_PARAMETER, KlassTypes.NOMBRE), KlassTypes.URL).none()
                .around(TokenSet.create(KlassTypes.LBRACE, KlassTypes.RBRACE, KlassTypes.L_BRACE, KlassTypes.R_BRACE)).lineBreakInCode()
                .after(KlassTypes.COLON).spaces(1)
                .around(KlassTypes.OPERATOR).spaces(1)
                .after(TokenSet.create(KlassTypes.COMMA, KlassTypes.DATA_TYPE_PROPERTY, KlassTypes.ENUMERATION_PROPERTY, KlassTypes.ASSOCIATION_END, KlassTypes.PROJECTION_INNER_NODE, KlassTypes.PROJECTION_LEAF_NODE, KlassTypes.SERVICE_MULTIPLICITY_CLAUSE, KlassTypes.SERVICE_CRITERIA_CLAUSE, KlassTypes.SERVICE_PROJECTION_CLAUSE, KlassTypes.SERVICE, KlassTypes.URL, KlassTypes.URL_GROUP, KlassTokenType.END_OF_LINE_COMMENT)).lineBreakInCode()
                .around(TokenSet.create(KlassTypes.KLASS, KlassTypes.ENUMERATION, KlassTypes.ASSOCIATION, KlassTypes.PROJECTION, KlassTypes.SERVICE_GROUP)).blankLines(1)
                .between(keywords, keywords).spaces(1)
                .between(keywords, TokenSet.create(KlassTypes.NOMBRE, KlassTypes.KLASS_NAME)).spaces(1);

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
