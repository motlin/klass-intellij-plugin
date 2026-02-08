package com.klass.intellij.highlighter;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.BLOCK_COMMENT;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.CLASS_NAME;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.CONSTANT;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.INSTANCE_FIELD;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.LOCAL_VARIABLE;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.PARAMETER;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.SEMICOLON;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.STATIC_FIELD;
import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

public class KlassHighlightingColors {
  public static final TextAttributesKey LINE_COMMENT =
      createTextAttributesKey("KLASS_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
  public static final TextAttributesKey KLASS_BLOCK_COMMENT =
      createTextAttributesKey("KLASS_BLOCK_COMMENT", BLOCK_COMMENT);
  public static final TextAttributesKey DOC_COMMENT =
      createTextAttributesKey("KLASS_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT);
  public static final TextAttributesKey KEYWORD =
      createTextAttributesKey("KLASS_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey NUMBER =
      createTextAttributesKey("KLASS_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey STRING =
      createTextAttributesKey("KLASS_STRING", DefaultLanguageHighlighterColors.STRING);
  public static final TextAttributesKey OPERATION_SIGN =
      createTextAttributesKey(
          "KLASS_OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
  public static final TextAttributesKey PARENTHESES =
      createTextAttributesKey("KLASS_PARENTH", DefaultLanguageHighlighterColors.PARENTHESES);
  public static final TextAttributesKey BRACKETS =
      createTextAttributesKey("KLASS_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
  public static final TextAttributesKey BRACES =
      createTextAttributesKey("KLASS_BRACES", DefaultLanguageHighlighterColors.BRACES);
  public static final TextAttributesKey COMMA =
      createTextAttributesKey("KLASS_COMMA", DefaultLanguageHighlighterColors.COMMA);
  public static final TextAttributesKey DOT =
      createTextAttributesKey("KLASS_DOT", DefaultLanguageHighlighterColors.DOT);
  public static final TextAttributesKey KLASS_SEMICOLON =
      createTextAttributesKey("KLASS_SEMICOLON", SEMICOLON);
  public static final TextAttributesKey DOC_COMMENT_MARKUP =
      createTextAttributesKey(
          "KLASS_DOC_MARKUP", DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP);
  public static final TextAttributesKey VALID_STRING_ESCAPE =
      createTextAttributesKey(
          "KLASS_VALID_STRING_ESCAPE", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE);
  public static final TextAttributesKey INVALID_STRING_ESCAPE =
      createTextAttributesKey(
          "KLASS_INVALID_STRING_ESCAPE", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);

  public static final TextAttributesKey LOCAL_VARIABLE_ATTRIBUTES =
      createTextAttributesKey("LOCAL_VARIABLE_ATTRIBUTES", LOCAL_VARIABLE);
  public static final TextAttributesKey PARAMETER_ATTRIBUTES =
      createTextAttributesKey("PARAMETER_ATTRIBUTES", PARAMETER);
  public static final TextAttributesKey INSTANCE_FIELD_ATTRIBUTES =
      createTextAttributesKey("INSTANCE_FIELD_ATTRIBUTES", INSTANCE_FIELD);
  public static final TextAttributesKey INSTANCE_FINAL_FIELD_ATTRIBUTES =
      createTextAttributesKey("INSTANCE_FINAL_FIELD_ATTRIBUTES", INSTANCE_FIELD_ATTRIBUTES);
  public static final TextAttributesKey STATIC_FIELD_ATTRIBUTES =
      createTextAttributesKey("STATIC_FIELD_ATTRIBUTES", STATIC_FIELD);
  public static final TextAttributesKey STATIC_FINAL_FIELD_ATTRIBUTES =
      createTextAttributesKey("STATIC_FINAL_FIELD_ATTRIBUTES", STATIC_FIELD_ATTRIBUTES);
  public static final TextAttributesKey CLASS_NAME_ATTRIBUTES =
      createTextAttributesKey("CLASS_NAME_ATTRIBUTES", CLASS_NAME);
  public static final TextAttributesKey PROJECTION_NAME_ATTRIBUTES =
      createTextAttributesKey("PROJECTION_NAME_ATTRIBUTES", CLASS_NAME);
  public static final TextAttributesKey ENUM_NAME_ATTRIBUTES =
      createTextAttributesKey("ENUM_NAME_ATTRIBUTES", CLASS_NAME_ATTRIBUTES);
  public static final TextAttributesKey ENUM_LITERAL_ATTRIBUTES =
      createTextAttributesKey("ENUM_LITERAL_ATTRIBUTES", CONSTANT);

  public static final TextAttributesKey VERB = createTextAttributesKey("VERB", KEYWORD);
  public static final TextAttributesKey URL_CONSTANT =
      createTextAttributesKey("URL_CONSTANT", NUMBER);
}
