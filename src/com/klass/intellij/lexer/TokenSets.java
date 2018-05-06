package com.klass.intellij.lexer;

import com.intellij.psi.tree.TokenSet;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.klass.intellij.psi.KlassTokenType.C_STYLE_COMMENT;
import static com.klass.intellij.psi.KlassTokenType.END_OF_LINE_COMMENT;
import static com.klass.intellij.psi.KlassTypes.ANDAND;
import static com.klass.intellij.psi.KlassTypes.ASCENDING_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ASSOCIATION;
import static com.klass.intellij.psi.KlassTypes.ASSOCIATION_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.AUDITED_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.AUTHORIZE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.BITEMPORAL_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.BOOLEAN_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.CLASS_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.COLON;
import static com.klass.intellij.psi.KlassTypes.CONFLICT_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.CONTAINS_OPERATOR;
import static com.klass.intellij.psi.KlassTypes.CREATE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.CRITERIA_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.DELETE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.DESCENDING_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.DOUBLE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ENDS_WITH_OPERATOR;
import static com.klass.intellij.psi.KlassTypes.ENUMERATION_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.EQEQ;
import static com.klass.intellij.psi.KlassTypes.FALSE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.FLOAT_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.GE;
import static com.klass.intellij.psi.KlassTypes.GT;
import static com.klass.intellij.psi.KlassTypes.ID_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.INSTANT_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.INTEGER_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.IN_OPERATOR;
import static com.klass.intellij.psi.KlassTypes.KEY_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.KLASS;
import static com.klass.intellij.psi.KlassTypes.LE;
import static com.klass.intellij.psi.KlassTypes.LOCAL_DATE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.LONG_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.LT;
import static com.klass.intellij.psi.KlassTypes.MANY_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.MULTIPLICITY_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.NE;
import static com.klass.intellij.psi.KlassTypes.NULL_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ONE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ON_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.OPTIMISTICALLY_LOCKED_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ORDER_BY_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.OROR;
import static com.klass.intellij.psi.KlassTypes.OWNED_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.PACKAGE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.PERCENT;
import static com.klass.intellij.psi.KlassTypes.PRIVATE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.PROJECTION_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.QUESTION_MARK;
import static com.klass.intellij.psi.KlassTypes.READ_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.RELATIONSHIP_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.SERVICE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.STARTS_WITH_OPERATOR;
import static com.klass.intellij.psi.KlassTypes.STRING_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.SYSTEM_TEMPORAL_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.TEMPORAL_INSTANT_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.TEMPORAL_RANGE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.THIS_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.TRUE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.UPDATE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.USER_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.VALIDATE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.VALID_TEMPORAL_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.VERSIONED_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.VERSIONS_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.WRITE_KEYWORD;

public interface TokenSets
{
    TokenSet KLASS_WHITESPACE_BIT_SET = TokenSet.create(WHITE_SPACE);

    TokenSet KLASS_PLAIN_COMMENT_BIT_SET = TokenSet.create(END_OF_LINE_COMMENT, C_STYLE_COMMENT);

    TokenSet KLASS_COMMENT_OR_WHITESPACE_BIT_SET =
            TokenSet.orSet(KLASS_WHITESPACE_BIT_SET, KLASS_PLAIN_COMMENT_BIT_SET);

    TokenSet KEYWORD_BIT_SET = TokenSet.create(
            PACKAGE_KEYWORD,
            CLASS_KEYWORD,
            ENUMERATION_KEYWORD,
            ASSOCIATION_KEYWORD,
            PROJECTION_KEYWORD,
            SERVICE_KEYWORD,
            USER_KEYWORD,
            VALID_TEMPORAL_KEYWORD,
            SYSTEM_TEMPORAL_KEYWORD,
            BITEMPORAL_KEYWORD,
            VERSIONED_KEYWORD,
            VERSIONS_KEYWORD,
            AUDITED_KEYWORD,
            OPTIMISTICALLY_LOCKED_KEYWORD,
            READ_KEYWORD,
            WRITE_KEYWORD,
            CREATE_KEYWORD,
            UPDATE_KEYWORD,
            DELETE_KEYWORD,
            KEY_KEYWORD,
            PRIVATE_KEYWORD,
            OWNED_KEYWORD,
            CRITERIA_KEYWORD,
            VALIDATE_KEYWORD,
            AUTHORIZE_KEYWORD,
            CONFLICT_KEYWORD,
            MULTIPLICITY_KEYWORD,
            ORDER_BY_KEYWORD,
            ASCENDING_KEYWORD,
            DESCENDING_KEYWORD,
            ONE_KEYWORD,
            MANY_KEYWORD,
            THIS_KEYWORD,
            ON_KEYWORD,
            RELATIONSHIP_KEYWORD);

    TokenSet LITERAL_BIT_SET = TokenSet.create(TRUE_KEYWORD, FALSE_KEYWORD, NULL_KEYWORD);

    TokenSet OPERATION_BIT_SET = TokenSet.create(
            EQEQ, GT, GE, LT, LE, NE, ANDAND, OROR, PERCENT,
            /*EQ, GT, LT, EXCL, TILDE,*/ QUESTION_MARK, COLON, /*, PLUS, MINUS, ASTERISK, DIV, AND, OR, XOR,
               PERC, EQEQ, LE, GE, NE, ANDAND, OROR, PLUSPLUS, MINUSMINUS, LTLT, GTGT, GTGTGT,
               PLUSEQ, MINUSEQ, ASTERISKEQ, DIVEQ, ANDEQ, OREQ, XOREQ, PERCEQ, LTLTEQ, GTGTEQ, GTGTGTEQ*/
            CONTAINS_OPERATOR, STARTS_WITH_OPERATOR, ENDS_WITH_OPERATOR, IN_OPERATOR);
    //
    //    TokenSet MODIFIER_BIT_SET = TokenSet.create(
    //            PUBLIC_KEYWORD, PROTECTED_KEYWORD, PRIVATE_KEYWORD, STATIC_KEYWORD, ABSTRACT_KEYWORD, FINAL_KEYWORD, NATIVE_KEYWORD,
    //            SYNCHRONIZED_KEYWORD, STRICTFP_KEYWORD, TRANSIENT_KEYWORD, VOLATILE_KEYWORD, DEFAULT_KEYWORD);
    //
    TokenSet PRIMITIVE_TYPE_BIT_SET = TokenSet.create(
            ID_KEYWORD,
            BOOLEAN_KEYWORD,
            INTEGER_KEYWORD,
            LONG_KEYWORD,
            FLOAT_KEYWORD,
            DOUBLE_KEYWORD,
            STRING_KEYWORD,
            INSTANT_KEYWORD,
            LOCAL_DATE_KEYWORD,
            TEMPORAL_INSTANT_KEYWORD,
            TEMPORAL_RANGE_KEYWORD);

    //    TokenSet EXPRESSION_BIT_SET = TokenSet.create(
    //            REFERENCE_EXPRESSION, LITERAL_EXPRESSION, THIS_EXPRESSION, SUPER_EXPRESSION, PARENTH_EXPRESSION, METHOD_CALL_EXPRESSION,
    //            TYPE_CAST_EXPRESSION, PREFIX_EXPRESSION, POSTFIX_EXPRESSION, BINARY_EXPRESSION, POLYADIC_EXPRESSION, CONDITIONAL_EXPRESSION,
    //            ASSIGNMENT_EXPRESSION, NEW_EXPRESSION, ARRAY_ACCESS_EXPRESSION, ARRAY_INITIALIZER_EXPRESSION, INSTANCE_OF_EXPRESSION,
    //            CLASS_OBJECT_ACCESS_EXPRESSION, METHOD_REF_EXPRESSION, LAMBDA_EXPRESSION, EMPTY_EXPRESSION);
    //
    //    TokenSet ANNOTATION_MEMBER_VALUE_BIT_SET = TokenSet.orSet(EXPRESSION_BIT_SET, TokenSet.create(ANNOTATION, ANNOTATION_ARRAY_INITIALIZER));
    //
    //    TokenSet ARRAY_DIMENSION_BIT_SET = TokenSet.create(
    //            REFERENCE_EXPRESSION, LITERAL_EXPRESSION, THIS_EXPRESSION, SUPER_EXPRESSION, PARENTH_EXPRESSION, METHOD_CALL_EXPRESSION,
    //            TYPE_CAST_EXPRESSION, PREFIX_EXPRESSION, POSTFIX_EXPRESSION, BINARY_EXPRESSION, POLYADIC_EXPRESSION, CONDITIONAL_EXPRESSION,
    //            ASSIGNMENT_EXPRESSION, NEW_EXPRESSION, ARRAY_ACCESS_EXPRESSION, INSTANCE_OF_EXPRESSION, CLASS_OBJECT_ACCESS_EXPRESSION,
    //            EMPTY_EXPRESSION);
    //
    //    TokenSet KLASS_STATEMENT_BIT_SET = TokenSet.create(
    //            EMPTY_STATEMENT, BLOCK_STATEMENT, EXPRESSION_STATEMENT, EXPRESSION_LIST_STATEMENT, DECLARATION_STATEMENT, IF_STATEMENT,
    //            WHILE_STATEMENT, FOR_STATEMENT, FOREACH_STATEMENT, DO_WHILE_STATEMENT, SWITCH_STATEMENT, SWITCH_LABEL_STATEMENT, BREAK_STATEMENT,
    //            CONTINUE_STATEMENT, RETURN_STATEMENT, THROW_STATEMENT, SYNCHRONIZED_STATEMENT, TRY_STATEMENT, LABELED_STATEMENT, ASSERT_STATEMENT);
    //
    //    TokenSet KLASS_MODULE_STATEMENT_BIT_SET = TokenSet.create(
    //            REQUIRES_STATEMENT, EXPORTS_STATEMENT, OPENS_STATEMENT, USES_STATEMENT, PROVIDES_STATEMENT);
    //
    //    TokenSet IMPORT_STATEMENT_BASE_BIT_SET = TokenSet.create(IMPORT_STATEMENT, IMPORT_STATIC_STATEMENT);
    TokenSet CLASS_KEYWORD_BIT_SET = TokenSet.create(CLASS_KEYWORD, ASSOCIATION_KEYWORD);
    TokenSet MEMBER_BIT_SET = TokenSet.create(KLASS, ASSOCIATION/*, FIELD, ENUM_CONSTANT, METHOD, ANNOTATION_METHOD*/);
    //    TokenSet FULL_MEMBER_BIT_SET = TokenSet.orSet(MEMBER_BIT_SET, TokenSet.create(CLASS_INITIALIZER));
}
