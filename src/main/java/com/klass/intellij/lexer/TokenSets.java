package com.klass.intellij.lexer;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.klass.intellij.psi.KlassTokenType.C_STYLE_COMMENT;
import static com.klass.intellij.psi.KlassTokenType.END_OF_LINE_COMMENT;
import static com.klass.intellij.psi.KlassTypes.ABSTRACT_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ALL_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ANDAND;
import static com.klass.intellij.psi.KlassTypes.ASCENDING_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ASSOCIATION;
import static com.klass.intellij.psi.KlassTypes.ASSOCIATION_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.AUDITED_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.AUTHORIZE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.BITEMPORAL_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.BOOLEAN_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.CLASS_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.COLON;
import static com.klass.intellij.psi.KlassTypes.CONFLICT_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.CONTAINS_OPERATOR;
import static com.klass.intellij.psi.KlassTypes.CREATED_BY_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.CREATED_ON_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.CREATE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.CRITERIA_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.DELETE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.DERIVED_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.DESCENDING_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.DOUBLE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ENDS_WITH_OPERATOR;
import static com.klass.intellij.psi.KlassTypes.ENUMERATION_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.EQEQ;
import static com.klass.intellij.psi.KlassTypes.EQUALS_EDGEPOINT_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.EXTENDS_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.FALSE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.FINAL_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.FLOAT_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.FROM_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.GE;
import static com.klass.intellij.psi.KlassTypes.GT;
import static com.klass.intellij.psi.KlassTypes.ID_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.IMPLEMENTS_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.INSTANT_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.INTEGER_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.INTERFACE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.IN_OPERATOR;
import static com.klass.intellij.psi.KlassTypes.KEY_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.KLASS;
import static com.klass.intellij.psi.KlassTypes.LAST_UPDATED_BY_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.LE;
import static com.klass.intellij.psi.KlassTypes.LOCAL_DATE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.LONG_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.LT;
import static com.klass.intellij.psi.KlassTypes.MANY_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.MAXIMUM_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.MAXIMUM_LENGTH_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.MAX_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.MAX_LENGTH_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.MINIMUM_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.MINIMUM_LENGTH_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.MIN_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.MIN_LENGTH_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.MULTIPLICITY_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.NE;
import static com.klass.intellij.psi.KlassTypes.NULL_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ONE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.ON_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.OPTIMISTICALLY_LOCKED_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.ORDER_BY_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.OROR;
import static com.klass.intellij.psi.KlassTypes.OWNED_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.PACKAGE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.PERCENT;
import static com.klass.intellij.psi.KlassTypes.PRIVATE_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.PROJECTION_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.QUESTION_MARK;
import static com.klass.intellij.psi.KlassTypes.READ_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.RELATIONSHIP_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.SERVICE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.STARTS_WITH_OPERATOR;
import static com.klass.intellij.psi.KlassTypes.STRING_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.SYSTEM_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.SYSTEM_TEMPORAL_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.TABLE_FOR_ALL_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.TABLE_PER_CLASS_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.TABLE_PER_SUBCLASS_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.TEMPORAL_INSTANT_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.TEMPORAL_RANGE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.THIS_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.TO_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.TRANSIENT_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.TRUE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.UPDATE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.USER_ID_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.USER_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.VALIDATE_KEYWORD;
import static com.klass.intellij.psi.KlassTypes.VALID_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.VALID_TEMPORAL_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.VERSIONED_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.VERSION_MODIFIER;
import static com.klass.intellij.psi.KlassTypes.WRITE_KEYWORD;

import com.intellij.psi.tree.TokenSet;

public interface TokenSets {
  TokenSet KLASS_WHITESPACE_BIT_SET = TokenSet.create(WHITE_SPACE);

  TokenSet KLASS_PLAIN_COMMENT_BIT_SET = TokenSet.create(END_OF_LINE_COMMENT, C_STYLE_COMMENT);

  TokenSet KLASS_COMMENT_OR_WHITESPACE_BIT_SET =
      TokenSet.orSet(KLASS_WHITESPACE_BIT_SET, KLASS_PLAIN_COMMENT_BIT_SET);

  TokenSet KEYWORD_BIT_SET =
      TokenSet.create(
          PACKAGE_KEYWORD,
          INTERFACE_KEYWORD,
          CLASS_KEYWORD,
          ENUMERATION_KEYWORD,
          ASSOCIATION_KEYWORD,
          PROJECTION_KEYWORD,
          SERVICE_KEYWORD,
          USER_KEYWORD,
          EXTENDS_KEYWORD,
          IMPLEMENTS_KEYWORD,
          ABSTRACT_KEYWORD,
          TABLE_PER_SUBCLASS_KEYWORD,
          TABLE_PER_CLASS_KEYWORD,
          TABLE_FOR_ALL_KEYWORD,
          MIN_LENGTH_KEYWORD,
          MAX_LENGTH_KEYWORD,
          MINIMUM_LENGTH_KEYWORD,
          MAXIMUM_LENGTH_KEYWORD,
          MIN_KEYWORD,
          MAX_KEYWORD,
          MINIMUM_KEYWORD,
          MAXIMUM_KEYWORD,
          SYSTEM_TEMPORAL_MODIFIER,
          VALID_TEMPORAL_MODIFIER,
          BITEMPORAL_MODIFIER,
          VERSIONED_MODIFIER,
          AUDITED_MODIFIER,
          OPTIMISTICALLY_LOCKED_MODIFIER,
          TRANSIENT_MODIFIER,
          VERSION_MODIFIER,
          READ_KEYWORD,
          WRITE_KEYWORD,
          CREATE_KEYWORD,
          UPDATE_KEYWORD,
          DELETE_KEYWORD,
          KEY_MODIFIER,
          PRIVATE_MODIFIER,
          OWNED_MODIFIER,
          FINAL_MODIFIER,
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
          ALL_KEYWORD,
          ON_KEYWORD,
          RELATIONSHIP_KEYWORD,
          USER_ID_MODIFIER,
          ID_MODIFIER,
          VALID_MODIFIER,
          SYSTEM_MODIFIER,
          FROM_MODIFIER,
          TO_MODIFIER,
          CREATED_BY_MODIFIER,
          CREATED_ON_MODIFIER,
          LAST_UPDATED_BY_MODIFIER,
          DERIVED_MODIFIER);

  TokenSet LITERAL_BIT_SET = TokenSet.create(TRUE_KEYWORD, FALSE_KEYWORD, NULL_KEYWORD);

  TokenSet OPERATION_BIT_SET =
      TokenSet.create(EQEQ, GT, GE, LT, LE, NE, ANDAND, OROR, PERCENT, QUESTION_MARK, COLON);

  TokenSet KEYWORD_OPERATION_BIT_SET =
      TokenSet.create(
          CONTAINS_OPERATOR,
          STARTS_WITH_OPERATOR,
          ENDS_WITH_OPERATOR,
          IN_OPERATOR,
          EQUALS_EDGEPOINT_KEYWORD);

  TokenSet PRIMITIVE_TYPE_BIT_SET =
      TokenSet.create(
          ID_MODIFIER,
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

  TokenSet CLASS_KEYWORD_BIT_SET = TokenSet.create(CLASS_KEYWORD, ASSOCIATION_KEYWORD);
  TokenSet MEMBER_BIT_SET = TokenSet.create(KLASS, ASSOCIATION);
}
