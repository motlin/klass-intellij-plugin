package com.klass.intellij.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*;
import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class KlassHighlightingColors
{
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
            createTextAttributesKey("KLASS_OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
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
    public static final TextAttributesKey DOC_COMMENT_TAG =
            createTextAttributesKey("KLASS_DOC_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
    public static final TextAttributesKey DOC_COMMENT_MARKUP =
            createTextAttributesKey("KLASS_DOC_MARKUP", DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP);
    public static final TextAttributesKey DOC_COMMENT_TAG_VALUE =
            createTextAttributesKey("DOC_COMMENT_TAG_VALUE", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG_VALUE);
    public static final TextAttributesKey VALID_STRING_ESCAPE =
            createTextAttributesKey("KLASS_VALID_STRING_ESCAPE", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE);
    public static final TextAttributesKey INVALID_STRING_ESCAPE =
            createTextAttributesKey("KLASS_INVALID_STRING_ESCAPE", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);

    public static final TextAttributesKey LOCAL_VARIABLE_ATTRIBUTES =
            createTextAttributesKey("LOCAL_VARIABLE_ATTRIBUTES", LOCAL_VARIABLE);
    public static final TextAttributesKey PARAMETER_ATTRIBUTES =
            createTextAttributesKey("PARAMETER_ATTRIBUTES", PARAMETER);
    public static final TextAttributesKey LAMBDA_PARAMETER_ATTRIBUTES =
            createTextAttributesKey("LAMBDA_PARAMETER_ATTRIBUTES", PARAMETER_ATTRIBUTES);
    public static final TextAttributesKey REASSIGNED_LOCAL_VARIABLE_ATTRIBUTES =
            createTextAttributesKey("REASSIGNED_LOCAL_VARIABLE_ATTRIBUTES", LOCAL_VARIABLE_ATTRIBUTES);
    public static final TextAttributesKey REASSIGNED_PARAMETER_ATTRIBUTES =
            createTextAttributesKey("REASSIGNED_PARAMETER_ATTRIBUTES", PARAMETER_ATTRIBUTES);
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
    public static final TextAttributesKey ANONYMOUS_CLASS_NAME_ATTRIBUTES =
            createTextAttributesKey("ANONYMOUS_CLASS_NAME_ATTRIBUTES", CLASS_NAME_ATTRIBUTES);
    public static final TextAttributesKey IMPLICIT_ANONYMOUS_CLASS_PARAMETER_ATTRIBUTES =
            createTextAttributesKey("IMPLICIT_ANONYMOUS_CLASS_PARAMETER_ATTRIBUTES", CLASS_NAME_ATTRIBUTES);
    public static final TextAttributesKey TYPE_PARAMETER_NAME_ATTRIBUTES =
            createTextAttributesKey("TYPE_PARAMETER_NAME_ATTRIBUTES", PARAMETER);
    public static final TextAttributesKey INTERFACE_NAME_ATTRIBUTES =
            createTextAttributesKey("INTERFACE_NAME_ATTRIBUTES", INTERFACE_NAME);
    public static final TextAttributesKey ENUM_NAME_ATTRIBUTES =
            createTextAttributesKey("ENUM_NAME_ATTRIBUTES", CLASS_NAME_ATTRIBUTES);
    public static final TextAttributesKey ENUM_LITERAL_ATTRIBUTES =
            createTextAttributesKey("ENUM_LITERAL_ATTRIBUTES", CONSTANT);
    public static final TextAttributesKey ABSTRACT_CLASS_NAME_ATTRIBUTES =
            createTextAttributesKey("ABSTRACT_CLASS_NAME_ATTRIBUTES", CLASS_NAME_ATTRIBUTES);
    public static final TextAttributesKey METHOD_CALL_ATTRIBUTES =
            createTextAttributesKey("METHOD_CALL_ATTRIBUTES", FUNCTION_CALL);
    public static final TextAttributesKey METHOD_DECLARATION_ATTRIBUTES =
            createTextAttributesKey("METHOD_DECLARATION_ATTRIBUTES", FUNCTION_DECLARATION);
    public static final TextAttributesKey STATIC_METHOD_ATTRIBUTES =
            createTextAttributesKey("STATIC_METHOD_ATTRIBUTES", STATIC_METHOD);
    public static final TextAttributesKey ABSTRACT_METHOD_ATTRIBUTES =
            createTextAttributesKey("ABSTRACT_METHOD_ATTRIBUTES", METHOD_CALL_ATTRIBUTES);
    public static final TextAttributesKey INHERITED_METHOD_ATTRIBUTES =
            createTextAttributesKey("INHERITED_METHOD_ATTRIBUTES", METHOD_CALL_ATTRIBUTES);
    public static final TextAttributesKey CONSTRUCTOR_CALL_ATTRIBUTES =
            createTextAttributesKey("CONSTRUCTOR_CALL_ATTRIBUTES", FUNCTION_CALL);
    public static final TextAttributesKey CONSTRUCTOR_DECLARATION_ATTRIBUTES =
            createTextAttributesKey("CONSTRUCTOR_DECLARATION_ATTRIBUTES", FUNCTION_DECLARATION);
    public static final TextAttributesKey ANNOTATION_NAME_ATTRIBUTES =
            createTextAttributesKey("ANNOTATION_NAME_ATTRIBUTES", METADATA);
    public static final TextAttributesKey ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES =
            createTextAttributesKey("ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES", METADATA);
    public static final TextAttributesKey ANNOTATION_ATTRIBUTE_VALUE_ATTRIBUTES =
            createTextAttributesKey("ANNOTATION_ATTRIBUTE_VALUE_ATTRIBUTES", METADATA);
}
