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
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassTypes;
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
                || element instanceof KlassEnumeration)
        {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings)
    {
        CommonCodeStyleSettings commonSettings = settings.getCommonSettings(KlassLanguage.INSTANCE);

        SpacingBuilder spacingBuilder = new SpacingBuilder(settings, KlassLanguage.INSTANCE)
                .before(TokenSet.create(KlassTypes.OPTIONAL_MARKER, KlassTypes.COMMA)).none()
                .around(KlassTypes.DOTDOT).none()
                .around(TokenSet.create(KlassTypes.LBRACKET, KlassTypes.LPAREN)).none()
                .before(TokenSet.create(KlassTypes.RBRACKET, KlassTypes.RPAREN)).none()
                .before(KlassTypes.MULTIPLICITY).none()
                .before(KlassTypes.NOMBRE).spaces(1)
                .around(TokenSet.create(KlassTypes.LBRACE, KlassTypes.RBRACE, KlassTypes.L_BRACE, KlassTypes.R_BRACE)).lineBreakInCode()
                .after(KlassTypes.COLON).spaces(1)
                .after(TokenSet.create(KlassTypes.COMMA, KlassTypes.DATA_TYPE_PROPERTY, KlassTypes.ENUMERATION_PROPERTY, KlassTypes.ASSOCIATION_END, KlassTypes.PROJECTION_INNER_NODE, KlassTypes.PROJECTION_LEAF_NODE)).lineBreakInCode()
                .around(TokenSet.create(KlassTypes.KLASS, KlassTypes.ENUMERATION, KlassTypes.ASSOCIATION, KlassTypes.PROJECTION)).blankLines(1);

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
