package com.klass.intellij.lexer;

import com.klass.intellij.psi.KlassTokenType;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.lexer.FlexLexer;
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

DIGIT = [0-9]
DIGIT_OR_UNDERSCORE = [_0-9]
DIGITS = {DIGIT} | {DIGIT} {DIGIT_OR_UNDERSCORE}*
HEX_DIGIT_OR_UNDERSCORE = [_0-9A-Fa-f]

INTEGER_LITERAL = {DIGITS} | {HEX_INTEGER_LITERAL} | {BIN_INTEGER_LITERAL}
LONG_LITERAL = {INTEGER_LITERAL} [Ll]
HEX_INTEGER_LITERAL = 0 [Xx] {HEX_DIGIT_OR_UNDERSCORE}*
BIN_INTEGER_LITERAL = 0 [Bb] {DIGIT_OR_UNDERSCORE}*

FLOAT_LITERAL = ({DEC_FP_LITERAL} | {HEX_FP_LITERAL}) [Ff] | {DIGITS} [Ff]
DOUBLE_LITERAL = ({DEC_FP_LITERAL} | {HEX_FP_LITERAL}) [Dd]? | {DIGITS} [Dd]
DEC_FP_LITERAL = {DIGITS} {DEC_EXPONENT} | {DEC_SIGNIFICAND} {DEC_EXPONENT}?
DEC_SIGNIFICAND = "." {DIGITS} | {DIGITS} "." {DIGIT_OR_UNDERSCORE}*
DEC_EXPONENT = [Ee] [+-]? {DIGIT_OR_UNDERSCORE}*
HEX_FP_LITERAL = {HEX_SIGNIFICAND} {HEX_EXPONENT}
HEX_SIGNIFICAND = 0 [Xx] ({HEX_DIGIT_OR_UNDERSCORE}+ "."? | {HEX_DIGIT_OR_UNDERSCORE}* "." {HEX_DIGIT_OR_UNDERSCORE}+)
HEX_EXPONENT = [Pp] [+-]? {DIGIT_OR_UNDERSCORE}*

ESCAPE_SEQUENCE = \\[^\r\n]
CHARACTER_LITERAL = "'" ([^\\\'\r\n] | {ESCAPE_SEQUENCE})* ("'"|\\)?
STRING_LITERAL = \" ([^\\\"\r\n] | {ESCAPE_SEQUENCE})* (\"|\\)?

%%

<YYINITIAL> {

  {WHITE_SPACE_CHAR}+ { return KlassTokenType.WHITE_SPACE; }

  {C_STYLE_COMMENT} { return KlassTokenType.C_STYLE_COMMENT; }
  {END_OF_LINE_COMMENT} { return KlassTokenType.END_OF_LINE_COMMENT; }
  {DOC_COMMENT} { return JavaDocElementType.DOC_COMMENT; }

  {LONG_LITERAL} { return KlassTokenType.LONG_LITERAL; }
  {INTEGER_LITERAL} { return KlassTokenType.INTEGER_LITERAL; }
  {FLOAT_LITERAL} { return KlassTokenType.FLOAT_LITERAL; }
  {DOUBLE_LITERAL} { return KlassTokenType.DOUBLE_LITERAL; }
  {CHARACTER_LITERAL} { return KlassTokenType.CHARACTER_LITERAL; }
  {STRING_LITERAL} { return KlassTokenType.STRING_LITERAL; }

  "true" { return KlassTokenType.TRUE_KEYWORD; }
  "false" { return KlassTokenType.FALSE_KEYWORD; }
  "null" { return KlassTokenType.NULL_KEYWORD; }

  "association" { return KlassTokenType.ASSOCIATION_KEYWORD; }
  "projection" { return KlassTokenType.PROJECTION_KEYWORD; }
  "enumeration" { return KlassTokenType.ENUMERATION_KEYWORD; }

  "abstract" { return KlassTokenType.ABSTRACT_KEYWORD; }
  "boolean" { return KlassTokenType.BOOLEAN_KEYWORD; }
  "break" { return KlassTokenType.BREAK_KEYWORD; }
  "byte" { return KlassTokenType.BYTE_KEYWORD; }
  "case" { return KlassTokenType.CASE_KEYWORD; }
  "catch" { return KlassTokenType.CATCH_KEYWORD; }
  "char" { return KlassTokenType.CHAR_KEYWORD; }
  "class" { return KlassTokenType.CLASS_KEYWORD; }
  "const" { return KlassTokenType.CONST_KEYWORD; }
  "continue" { return KlassTokenType.CONTINUE_KEYWORD; }
  "default" { return KlassTokenType.DEFAULT_KEYWORD; }
  "do" { return KlassTokenType.DO_KEYWORD; }
  "double" { return KlassTokenType.DOUBLE_KEYWORD; }
  "else" { return KlassTokenType.ELSE_KEYWORD; }
  "extends" { return KlassTokenType.EXTENDS_KEYWORD; }
  "final" { return KlassTokenType.FINAL_KEYWORD; }
  "finally" { return KlassTokenType.FINALLY_KEYWORD; }
  "float" { return KlassTokenType.FLOAT_KEYWORD; }
  "for" { return KlassTokenType.FOR_KEYWORD; }
  "goto" { return KlassTokenType.GOTO_KEYWORD; }
  "if" { return KlassTokenType.IF_KEYWORD; }
  "implements" { return KlassTokenType.IMPLEMENTS_KEYWORD; }
  "import" { return KlassTokenType.IMPORT_KEYWORD; }
  "instanceof" { return KlassTokenType.INSTANCEOF_KEYWORD; }
  "int" { return KlassTokenType.INT_KEYWORD; }
  "interface" { return KlassTokenType.INTERFACE_KEYWORD; }
  "long" { return KlassTokenType.LONG_KEYWORD; }
  "native" { return KlassTokenType.NATIVE_KEYWORD; }
  "new" { return KlassTokenType.NEW_KEYWORD; }
  "package" { return KlassTokenType.PACKAGE_KEYWORD; }
  "private" { return KlassTokenType.PRIVATE_KEYWORD; }
  "public" { return KlassTokenType.PUBLIC_KEYWORD; }
  "short" { return KlassTokenType.SHORT_KEYWORD; }
  "super" { return KlassTokenType.SUPER_KEYWORD; }
  "switch" { return KlassTokenType.SWITCH_KEYWORD; }
  "synchronized" { return KlassTokenType.SYNCHRONIZED_KEYWORD; }
  "this" { return KlassTokenType.THIS_KEYWORD; }
  "throw" { return KlassTokenType.THROW_KEYWORD; }
  "protected" { return KlassTokenType.PROTECTED_KEYWORD; }
  "transient" { return KlassTokenType.TRANSIENT_KEYWORD; }
  "return" { return KlassTokenType.RETURN_KEYWORD; }
  "void" { return KlassTokenType.VOID_KEYWORD; }
  "static" { return KlassTokenType.STATIC_KEYWORD; }
  "strictfp" { return KlassTokenType.STRICTFP_KEYWORD; }
  "while" { return KlassTokenType.WHILE_KEYWORD; }
  "try" { return KlassTokenType.TRY_KEYWORD; }
  "volatile" { return KlassTokenType.VOLATILE_KEYWORD; }
  "throws" { return KlassTokenType.THROWS_KEYWORD; }

  {IDENTIFIER} { return KlassTokenType.IDENTIFIER; }

  "==" { return KlassTokenType.EQEQ; }
  "!=" { return KlassTokenType.NE; }
  "||" { return KlassTokenType.OROR; }
  "++" { return KlassTokenType.PLUSPLUS; }
  "--" { return KlassTokenType.MINUSMINUS; }

  "<" { return KlassTokenType.LT; }
  "<=" { return KlassTokenType.LE; }
  "<<=" { return KlassTokenType.LTLTEQ; }
  "<<" { return KlassTokenType.LTLT; }
  ">" { return KlassTokenType.GT; }
  "&" { return KlassTokenType.AND; }
  "&&" { return KlassTokenType.ANDAND; }

  "+=" { return KlassTokenType.PLUSEQ; }
  "-=" { return KlassTokenType.MINUSEQ; }
  "*=" { return KlassTokenType.ASTERISKEQ; }
  "/=" { return KlassTokenType.DIVEQ; }
  "&=" { return KlassTokenType.ANDEQ; }
  "|=" { return KlassTokenType.OREQ; }
  "^=" { return KlassTokenType.XOREQ; }
  "%=" { return KlassTokenType.PERCEQ; }

  "("   { return KlassTokenType.LPARENTH; }
  ")"   { return KlassTokenType.RPARENTH; }
  "{"   { return KlassTokenType.LBRACE; }
  "}"   { return KlassTokenType.RBRACE; }
  "["   { return KlassTokenType.LBRACKET; }
  "]"   { return KlassTokenType.RBRACKET; }
  ";"   { return KlassTokenType.SEMICOLON; }
  ","   { return KlassTokenType.COMMA; }
  "..." { return KlassTokenType.ELLIPSIS; }
  ".."   { return KlassTokenType.DOTDOT; }
  "."   { return KlassTokenType.DOT; }

  "=" { return KlassTokenType.EQ; }
  "!" { return KlassTokenType.EXCL; }
  "~" { return KlassTokenType.TILDE; }
  "?" { return KlassTokenType.QUEST; }
  ":" { return KlassTokenType.COLON; }
  "+" { return KlassTokenType.PLUS; }
  "-" { return KlassTokenType.MINUS; }
  "*" { return KlassTokenType.ASTERISK; }
  "/" { return KlassTokenType.DIV; }
  "|" { return KlassTokenType.OR; }
  "^" { return KlassTokenType.XOR; }
  "%" { return KlassTokenType.PERC; }
  "@" { return KlassTokenType.AT; }

  "::" { return KlassTokenType.DOUBLE_COLON; }
  "->" { return KlassTokenType.ARROW; }
}

[^]  { return KlassTokenType.BAD_CHARACTER; }
