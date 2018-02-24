package com.klass.intellij.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageUtil;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.klass.intellij.KlassLanguage;
import com.klass.intellij.lexer.KlassLexerAdapter;
import com.klass.intellij.psi.KlassFile;
import com.klass.intellij.psi.KlassTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassParserDefinition implements ParserDefinition
{
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(KlassTypes.COMMENT);

    public static final IFileElementType FILE = new IFileElementType(KlassLanguage.INSTANCE);

    @Override
    @NotNull
    public Lexer createLexer(@Nullable Project project)
    {
        return new KlassLexerAdapter();
    }

    @Override
    public IFileElementType getFileNodeType()
    {
        return FILE;
    }

    @Override
    @NotNull
    public TokenSet getWhitespaceTokens()
    {
        return WHITE_SPACES;
    }

    @Override
    @NotNull
    public TokenSet getCommentTokens()
    {
        return COMMENTS;
    }

    @Override
    @NotNull
    public TokenSet getStringLiteralElements()
    {
        return TokenSet.EMPTY;
    }

    @Override
    @NotNull
    public PsiParser createParser(Project project)
    {
        return new KlassParser();
    }

    @Override
    @NotNull
    public PsiElement createElement(ASTNode node)
    {
        return KlassTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider)
    {
        return new KlassFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
    {
        Lexer lexer = new KlassLexerAdapter();
        return LanguageUtil.canStickTokensTogetherByLexer(left, right, lexer);
    }
}
