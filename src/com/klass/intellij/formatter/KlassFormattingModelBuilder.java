package com.klass.intellij.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import com.klass.intellij.KlassLanguage;
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
                        settings),
                settings);
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings)
    {
        CommonCodeStyleSettings commonSettings = settings.getCommonSettings(KlassLanguage.INSTANCE);

        SpacingBuilder spacingBuilder = new SpacingBuilder(settings, KlassLanguage.INSTANCE)
                .before(KlassTypes.OPTIONAL_MARKER).none()
                .before(KlassTypes.COLON).none()
                .after(KlassTypes.COLON).spaces(1)
                .around(KlassTypes.DOTDOT).none()
                .withinPair(KlassTypes.LBRACKET, KlassTypes.RBRACKET).none()
                .around(TokenSet.create(KlassTypes.CLASS, KlassTypes.ASSOCIATION)).spacing(0, 0, 2, true, 1)
                .between(KlassTypes.ASSOCIATION_END_TYPE, KlassTypes.MULTIPLICITY).none()
                .withinPair(KlassTypes.LBRACE, KlassTypes.RBRACE).spacing(0, 0, 1, false, 0)
                .before(KlassTypes.LBRACE).lineBreakInCodeIf(commonSettings.BRACE_STYLE != CommonCodeStyleSettings.END_OF_LINE)
                .after(TokenSet.create(KlassTypes.DATA_TYPE_PROPERTY, KlassTypes.SOURCE_ASSOCIATION_END, KlassTypes.TARGET_ASSOCIATION_END)).spacing(0, 0, 1, false, 0);

        return spacingBuilder;
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset)
    {
        return null;
    }
}
