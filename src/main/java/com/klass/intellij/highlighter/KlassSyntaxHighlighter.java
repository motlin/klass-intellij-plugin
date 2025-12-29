package com.klass.intellij.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.StringEscapesTokenTypes;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlTokenType;
import com.klass.intellij.lexer.KlassLexerAdapter;
import com.klass.intellij.lexer.TokenSets;
import com.klass.intellij.psi.KlassTokenType;
import com.klass.intellij.psi.KlassTypes;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class KlassSyntaxHighlighter extends SyntaxHighlighterBase {
  private static final Map<IElementType, TextAttributesKey> ourMap1;
  private static final Map<IElementType, TextAttributesKey> ourMap2;

  static {
    ourMap1 = new HashMap<>();
    ourMap2 = new HashMap<>();

    SyntaxHighlighterBase.fillMap(
        ourMap1, TokenSets.KEYWORD_BIT_SET, KlassHighlightingColors.KEYWORD);
    // TODO: Separate color for primitive types?
    SyntaxHighlighterBase.fillMap(
        ourMap1, TokenSets.PRIMITIVE_TYPE_BIT_SET, KlassHighlightingColors.KEYWORD);
    SyntaxHighlighterBase.fillMap(
        ourMap1, TokenSets.LITERAL_BIT_SET, KlassHighlightingColors.KEYWORD);
    SyntaxHighlighterBase.fillMap(
        ourMap1, TokenSets.KEYWORD_OPERATION_BIT_SET, KlassHighlightingColors.KEYWORD);
    SyntaxHighlighterBase.fillMap(
        ourMap1, TokenSets.OPERATION_BIT_SET, KlassHighlightingColors.OPERATION_SIGN);

    ourMap1.put(XmlTokenType.XML_DATA_CHARACTERS, KlassHighlightingColors.DOC_COMMENT);
    ourMap1.put(XmlTokenType.XML_REAL_WHITE_SPACE, KlassHighlightingColors.DOC_COMMENT);
    ourMap1.put(XmlTokenType.TAG_WHITE_SPACE, KlassHighlightingColors.DOC_COMMENT);

    // TODO: ASTERISK is used for multiplicities, but maybe multiplicities should be different than
    // other numbers
    ourMap1.put(KlassTypes.ASTERISK, KlassHighlightingColors.NUMBER);
    ourMap1.put(KlassTypes.INTEGER_LITERAL, KlassHighlightingColors.NUMBER);
    ourMap1.put(KlassTypes.FLOAT_LITERAL, KlassHighlightingColors.NUMBER);
    ourMap1.put(KlassTypes.STRING_LITERAL, KlassHighlightingColors.STRING);
    ourMap1.put(
        StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN,
        KlassHighlightingColors.VALID_STRING_ESCAPE);
    ourMap1.put(
        StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN,
        KlassHighlightingColors.INVALID_STRING_ESCAPE);
    ourMap1.put(
        StringEscapesTokenTypes.INVALID_UNICODE_ESCAPE_TOKEN,
        KlassHighlightingColors.INVALID_STRING_ESCAPE);
    //        ourMap1.put(KlassTypes.CHARACTER_LITERAL, KlassHighlightingColors.STRING);

    ourMap1.put(KlassTypes.LPAREN, KlassHighlightingColors.PARENTHESES);
    ourMap1.put(KlassTypes.RPAREN, KlassHighlightingColors.PARENTHESES);

    ourMap1.put(KlassTypes.LBRACE, KlassHighlightingColors.BRACES);
    ourMap1.put(KlassTypes.RBRACE, KlassHighlightingColors.BRACES);

    ourMap1.put(KlassTypes.LBRACKET, KlassHighlightingColors.BRACKETS);
    ourMap1.put(KlassTypes.RBRACKET, KlassHighlightingColors.BRACKETS);

    ourMap1.put(KlassTypes.COMMA, KlassHighlightingColors.COMMA);
    ourMap1.put(KlassTypes.DOT, KlassHighlightingColors.DOT);
    ourMap1.put(KlassTypes.DOTDOT, KlassHighlightingColors.DOT);
    ourMap1.put(KlassTypes.SEMICOLON, KlassHighlightingColors.KLASS_SEMICOLON);
    ourMap1.put(KlassTypes.SLASH, KlassHighlightingColors.DOT);
    ourMap1.put(KlassTypes.URL_IDENTIFIER, KlassHighlightingColors.URL_CONSTANT);

    ourMap1.put(KlassTokenType.C_STYLE_COMMENT, KlassHighlightingColors.KLASS_BLOCK_COMMENT);
    ourMap1.put(KlassTokenType.END_OF_LINE_COMMENT, KlassHighlightingColors.LINE_COMMENT);
    ourMap1.put(TokenType.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);

    IElementType[] klassDocMarkup = {
      XmlTokenType.XML_START_TAG_START,
      XmlTokenType.XML_END_TAG_START,
      XmlTokenType.XML_TAG_END,
      XmlTokenType.XML_EMPTY_ELEMENT_END,
      XmlTokenType.TAG_WHITE_SPACE,
      XmlTokenType.XML_TAG_NAME,
      XmlTokenType.XML_NAME,
      XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN,
      XmlTokenType.XML_ATTRIBUTE_VALUE_START_DELIMITER,
      XmlTokenType.XML_ATTRIBUTE_VALUE_END_DELIMITER,
      XmlTokenType.XML_CHAR_ENTITY_REF,
      XmlTokenType.XML_ENTITY_REF_TOKEN,
      XmlTokenType.XML_EQ
    };
    for (IElementType idx : klassDocMarkup) {
      ourMap1.put(idx, KlassHighlightingColors.DOC_COMMENT);
      ourMap2.put(idx, KlassHighlightingColors.DOC_COMMENT_MARKUP);
    }
  }

  @NotNull @Override
  public Lexer getHighlightingLexer() {
    return new KlassLexerAdapter();
  }

  @NotNull @Override
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    return SyntaxHighlighterBase.pack(ourMap1.get(tokenType), ourMap2.get(tokenType));
  }
}
