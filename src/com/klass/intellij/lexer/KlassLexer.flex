package com.klass.intellij.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.klass.intellij.psi.KlassTokenType;
import com.klass.intellij.psi.KlassTypes;

@SuppressWarnings({"ALL"})
%%

%{
  public void goTo(int offset) {
    zzCurrentPos = zzMarkedPos = zzStartRead = offset;
    zzAtEOF = false;
  }
%}

%unicode
%class KlassLexer
%implements FlexLexer
%function advance
%type IElementType

WHITE_SPACE_CHAR = [\ \n\r\t\f]

IDENTIFIER = [:jletter:] [:jletterdigit:]*

C_STYLE_COMMENT=("/*"[^"*"]{COMMENT_TAIL})|"/*"
DOC_COMMENT="/*""*"+("/"|([^"/""*"]{COMMENT_TAIL}))?
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?
END_OF_LINE_COMMENT="/""/"[^\r\n]*

DOTDOT = ".."

DIGIT = [0-9]
DIGIT_OR_UNDERSCORE = [_0-9]
DIGITS = {DIGIT} | ({DIGIT} {DIGIT_OR_UNDERSCORE}*)

INTEGER_LITERAL = {DIGITS}
FLOAT_LITERAL = ({DIGITS} {DEC_EXPONENT}) | ({DEC_SIGNIFICAND} {DEC_EXPONENT}?)
DEC_SIGNIFICAND = ("." {DIGITS}) | ({DIGITS} "." {DIGIT_OR_UNDERSCORE}+)
DEC_EXPONENT = [Ee] [+-]? {DIGIT_OR_UNDERSCORE}*

ESCAPE_SEQUENCE = \\[^\r\n]
//CHARACTER_LITERAL = "'" ([^\\\'\r\n] | {ESCAPE_SEQUENCE})* ("'"|\\)?
STRING_LITERAL = \" ([^\\\"\r\n] | {ESCAPE_SEQUENCE})* (\"|\\)?

%%

<YYINITIAL> {

  {WHITE_SPACE_CHAR}+ { return TokenType.WHITE_SPACE; }

  {C_STYLE_COMMENT} { return KlassTokenType.C_STYLE_COMMENT; }
  {END_OF_LINE_COMMENT} { return KlassTokenType.END_OF_LINE_COMMENT; }
//  {DOC_COMMENT} { return JavaDocElementType.DOC_COMMENT; }

  {INTEGER_LITERAL} { return KlassTypes.INTEGER_LITERAL; }
  {FLOAT_LITERAL} { return KlassTypes.FLOAT_LITERAL; }
//  {CHARACTER_LITERAL} { return KlassTypes.CHARACTER_LITERAL; }
  {STRING_LITERAL} { return KlassTypes.STRING_LITERAL; }

  "true" { return KlassTypes.TRUE_KEYWORD; }
  "false" { return KlassTypes.FALSE_KEYWORD; }
  "null" { return KlassTypes.NULL_KEYWORD; }
  "userPrincipal" { return KlassTypes.USER_PRINCIPAL_KEYWORD; }

  "class" { return KlassTypes.CLASS_KEYWORD; }
  "association" { return KlassTypes.ASSOCIATION_KEYWORD; }
  "enumeration" { return KlassTypes.ENUMERATION_KEYWORD; }
  "projection" { return KlassTypes.PROJECTION_KEYWORD; }
  "service" { return KlassTypes.SERVICE_KEYWORD; }
  "user" { return KlassTypes.USER_KEYWORD; }

  "systemTemporal" { return KlassTypes.SYSTEM_TEMPORAL_KEYWORD; }
  "validTemporal" { return KlassTypes.VALID_TEMPORAL_KEYWORD; }
  "bitemporal" { return KlassTypes.BITEMPORAL_KEYWORD; }
  "versioned" { return KlassTypes.VERSIONED_KEYWORD; }
  "audited" { return KlassTypes.AUDITED_KEYWORD; }
  "optimisticallyLocked" { return KlassTypes.OPTIMISTICALLY_LOCKED_KEYWORD; }
  "read" { return KlassTypes.READ_KEYWORD; }
  "write" { return KlassTypes.WRITE_KEYWORD; }
  "create" { return KlassTypes.CREATE_KEYWORD; }
  "update" { return KlassTypes.UPDATE_KEYWORD; }
  "delete" { return KlassTypes.DELETE_KEYWORD; }

  "in" { return KlassTypes.IN_OPERATOR; }
  "contains" { return KlassTypes.CONTAINS_OPERATOR; }
  "startsWith" { return KlassTypes.STARTS_WITH_OPERATOR; }
  "endsWith" { return KlassTypes.ENDS_WITH_OPERATOR; }

  "key" { return KlassTypes.KEY_KEYWORD; }
  "on" { return KlassTypes.ON_KEYWORD; }
  "relationship" { return KlassTypes.RELATIONSHIP_KEYWORD; }

  "multiplicity" { return KlassTypes.MULTIPLICITY_KEYWORD; }
  "criteria" { return KlassTypes.CRITERIA_KEYWORD; }
  "validate" { return KlassTypes.VALIDATE_KEYWORD; }
  "authorize" { return KlassTypes.AUTHORIZE_KEYWORD; }
  "native" { return KlassTypes.NATIVE_KEYWORD; }
  "orderBy" { return KlassTypes.ORDER_BY_KEYWORD; }
  "ascending" { return KlassTypes.ASCENDING_KEYWORD; }
  "descending" { return KlassTypes.DESCENDING_KEYWORD; }

  "one" { return KlassTypes.ONE_KEYWORD; }
  "many" { return KlassTypes.MANY_KEYWORD; }

  "ID" { return KlassTypes.ID_KEYWORD; }
  "Boolean" { return KlassTypes.BOOLEAN_KEYWORD; }
  "Integer" { return KlassTypes.INTEGER_KEYWORD; }
  "Double" { return KlassTypes.DOUBLE_KEYWORD; }
  "Float" { return KlassTypes.FLOAT_KEYWORD; }
  "Long" { return KlassTypes.LONG_KEYWORD; }
  "String" { return KlassTypes.STRING_KEYWORD; }
  "Instant" { return KlassTypes.INSTANT_KEYWORD; }
  "LocalDate" { return KlassTypes.LOCAL_DATE_KEYWORD; }

  "GET" { return KlassTypes.GET_VERB; }
  "POST" { return KlassTypes.POST_VERB; }
  "PUT" { return KlassTypes.PUT_VERB; }
  "PATCH" { return KlassTypes.PATCH_VERB; }
  "DELETE" { return KlassTypes.DELETE_VERB; }

//  "abstract" { return KlassTypes.ABSTRACT_KEYWORD; }
//  "break" { return KlassTypes.BREAK_KEYWORD; }
//  "byte" { return KlassTypes.BYTE_KEYWORD; }
//  "case" { return KlassTypes.CASE_KEYWORD; }
//  "catch" { return KlassTypes.CATCH_KEYWORD; }
//  "char" { return KlassTypes.CHAR_KEYWORD; }
//  "const" { return KlassTypes.CONST_KEYWORD; }
//  "continue" { return KlassTypes.CONTINUE_KEYWORD; }
//  "default" { return KlassTypes.DEFAULT_KEYWORD; }
//  "do" { return KlassTypes.DO_KEYWORD; }
//  "else" { return KlassTypes.ELSE_KEYWORD; }
//  "extends" { return KlassTypes.EXTENDS_KEYWORD; }
//  "final" { return KlassTypes.FINAL_KEYWORD; }
//  "finally" { return KlassTypes.FINALLY_KEYWORD; }
//  "for" { return KlassTypes.FOR_KEYWORD; }
//  "goto" { return KlassTypes.GOTO_KEYWORD; }
//  "if" { return KlassTypes.IF_KEYWORD; }
//  "implements" { return KlassTypes.IMPLEMENTS_KEYWORD; }
//  "import" { return KlassTypes.IMPORT_KEYWORD; }
//  "instanceof" { return KlassTypes.INSTANCEOF_KEYWORD; }
//  "interface" { return KlassTypes.INTERFACE_KEYWORD; }
//  "native" { return KlassTypes.NATIVE_KEYWORD; }
//  "new" { return KlassTypes.NEW_KEYWORD; }
  "package" { return KlassTypes.PACKAGE_KEYWORD; }
  "private" { return KlassTypes.PRIVATE_KEYWORD; }
//  "public" { return KlassTypes.PUBLIC_KEYWORD; }
//  "short" { return KlassTypes.SHORT_KEYWORD; }
//  "super" { return KlassTypes.SUPER_KEYWORD; }
//  "switch" { return KlassTypes.SWITCH_KEYWORD; }
//  "synchronized" { return KlassTypes.SYNCHRONIZED_KEYWORD; }
  "this" { return KlassTypes.THIS_KEYWORD; }
//  "throw" { return KlassTypes.THROW_KEYWORD; }
//  "protected" { return KlassTypes.PROTECTED_KEYWORD; }
//  "transient" { return KlassTypes.TRANSIENT_KEYWORD; }
//  "return" { return KlassTypes.RETURN_KEYWORD; }
//  "void" { return KlassTypes.VOID_KEYWORD; }
//  "static" { return KlassTypes.STATIC_KEYWORD; }
//  "strictfp" { return KlassTypes.STRICTFP_KEYWORD; }
//  "while" { return KlassTypes.WHILE_KEYWORD; }
//  "try" { return KlassTypes.TRY_KEYWORD; }
//  "volatile" { return KlassTypes.VOLATILE_KEYWORD; }
//  "throws" { return KlassTypes.THROWS_KEYWORD; }

  {IDENTIFIER} { return KlassTypes.IDENTIFIER; }

  "==" { return KlassTypes.EQEQ; }
  "!=" { return KlassTypes.NE; }
  "||" { return KlassTypes.OROR; }
//  "++" { return KlassTypes.PLUSPLUS; }
//  "--" { return KlassTypes.MINUSMINUS; }
//
  "<" { return KlassTypes.LT; }
  "<=" { return KlassTypes.LE; }
//  "<<=" { return KlassTypes.LTLTEQ; }
//  "<<" { return KlassTypes.LTLT; }
  ">" { return KlassTypes.GT; }
  ">=" { return KlassTypes.GE; }
  "&" { return KlassTypes.AND; }
  "&&" { return KlassTypes.ANDAND; }
//
//  "+=" { return KlassTypes.PLUSEQ; }
//  "-=" { return KlassTypes.MINUSEQ; }
//  "*=" { return KlassTypes.ASTERISKEQ; }
//  "/=" { return KlassTypes.DIVEQ; }
//  "&=" { return KlassTypes.ANDEQ; }
//  "|=" { return KlassTypes.OREQ; }
//  "^=" { return KlassTypes.XOREQ; }
//  "%=" { return KlassTypes.PERCEQ; }
//
  "("   { return KlassTypes.LPAREN; }
  ")"   { return KlassTypes.RPAREN; }
  "{"   { return KlassTypes.LBRACE; }
  "}"   { return KlassTypes.RBRACE; }
  "["   { return KlassTypes.LBRACKET; }
  "]"   { return KlassTypes.RBRACKET; }
//  ";"   { return KlassTypes.SEMICOLON; }
  ","   { return KlassTypes.COMMA; }
//  "..." { return KlassTypes.ELLIPSIS; }
  ".."   { return KlassTypes.DOTDOT; }
  "."   { return KlassTypes.DOT; }
  "`"   { return KlassTypes.TICK; }

  "=" { return KlassTypes.EQ; }
  "!" { return KlassTypes.EXCL; }
//  "~" { return KlassTypes.TILDE; }
  "?" { return KlassTypes.QUESTION_MARK; }
  ":" { return KlassTypes.COLON; }
  "+" { return KlassTypes.PLUS; }
  "-" { return KlassTypes.MINUS; }
  "*" { return KlassTypes.ASTERISK; }
//  "|" { return KlassTypes.OR; }
//  "^" { return KlassTypes.XOR; }
  "%" { return KlassTypes.PERCENT; }
//  "@" { return KlassTypes.AT; }
//
//  "::" { return KlassTypes.DOUBLE_COLON; }
//  "->" { return KlassTypes.ARROW; }

  "/" { return KlassTypes.SLASH; }

}

[^]  { return TokenType.BAD_CHARACTER; }
