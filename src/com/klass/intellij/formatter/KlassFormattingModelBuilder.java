package com.klass.intellij.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import com.klass.intellij.KlassLanguage;
import com.klass.intellij.psi.KlassElementType;
import com.klass.intellij.psi.KlassTokenType;
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
        return new SpacingBuilder(settings, KlassLanguage.INSTANCE)
                .before(TokenSet.create(KlassTypes.COLON, KlassTypes.OPTIONAL_MARKER)).spacing(0, 0, 0, false, 0)
                .after(KlassTypes.COLON).spacing(1, 1, 0, false, 0)
                .around(KlassTypes.DOTDOT).spacing(0, 0, 0, false, 0)
                .withinPair(KlassTypes.LBRACKET, KlassTypes.RBRACKET).spacing(0, 0, 0, false, 0)
                .around(TokenSet.create(KlassTypes.CLASS, KlassTypes.ASSOCIATION)).spacing(0, 0, 2, true, 1)
                .between(KlassTypes.ASSOCIATION_END_TYPE, KlassTypes.MULTIPLICITY).spacing(0, 0, 0, false, 0)
                .withinPair(KlassTypes.LBRACE, KlassTypes.RBRACE).spacing(0, 0, 1, false, 0)
                .before(KlassTypes.LBRACE).spacing(0, 0, 1, false, 0)
                .after(TokenSet.create(KlassTypes.DATA_TYPE_PROPERTY, KlassTypes.SOURCE_ASSOCIATION_END, KlassTypes.TARGET_ASSOCIATION_END)).spacing(0, 0, 1, false, 0);

    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset)
    {
        return null;
    }
}
