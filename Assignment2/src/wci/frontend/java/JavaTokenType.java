package wci.frontend.java;

import wci.frontend.TokenType;

public enum JavaTokenType implements TokenType {

    // Java reserved words
    ABSTRACT, DOUBLE, LONG, INT, BREAK, ELSE, SWITCH,
    CASE, ENUM, NATIVE, SUPER, CHAR, EXTENDS, RETURN,
    THIS, CLASS, FLOAT, SHORT, THROW, CONST, FOR, PACKAGE,
    VOID, CONTINUE, GOTO, PROTECTED, VOLATILE, DO, IF,
    STATIC, WHILE,

    // Java special symbols
    PLUS("+"), MINUS("-"), STAR("*"), SLASH("/"), DOT("."),
    COMMA(","), SEMICOLON(";"), COLON(":"), QUOTE("'"),
    DOUBLE_QUOTE("\"\""), EQUALS("="), NOT_EQUALS("!="),
    LESS_THAN("<"), LESS_EQUALS("<="), PLUS_EQUALS("+="),
    MINUS_EQUALS("-="), STAR_EQUALS("*="), SLASH_EQUALS("/="),
    QUESTION("?"), PIPE_EQUALS("|="), PERCENT_EQUALS("%="),
    AND_EQUALS("&="), UP_ARROW_EQUALS("^="), SHIFT_LEFT__EQUALS("<<="),
    SHIFT_RIGHT_EQUALS(">>="), GREATER_EQUALS(">="), GREATER_THAN(">"),
    LEFT_PAREN("("), RIGHT_PAREN(")"), LEFT_BRACKET("["),
    RIGHT_BRACKET("]"), LEFT_BRACE("{"), RIGHT_BRACE("}"),
    UP_ARROW("^"), AND("$"), AND_AND("&&"), OR_OR("||"),
    PLUS_PLUS("++"), MINUS_MINUS("--"),
    SLASH_SLAR("/*"), STAR_SLASH("*/"),

    IDENTIFIER, CHARACTER, NUMBER, STRING;

    private static final int FIRST_RESERVED_INDEX = ABSTRACT.ordinal();
    private static final int LAST_RESERVED_INDEX  = WHILE.ordinal();

    private static final int FIRST_SPECIAL_INDEX = PLUS.ordinal();
    private static final int LAST_SPECIAL_INDEX  = STAR_SLASH.ordinal();

    private String text;  // token text

    /**
     * Constructor.
     */
    JavaTokenType()
    {
        this.text = this.toString().toLowerCase();
    }

    /**
     * Constructor.
     * @param text the token text.
     */
    JavaTokenType(String text)
    {
        this.text = text;
    }

    /**
     * Getter.
     * @return the token text.
     */
    public String getText()
    {
        return text;
    }
}

