package wci.frontend.java;

import wci.frontend.TokenType;

import java.util.HashSet;
import java.util.Hashtable;

public enum JavaTokenType implements TokenType {

    // Java reserved words
    ABSTRACT, DOUBLE, LONG, INT, BREAK, ELSE, SWITCH,
    CASE, ENUM, NATIVE, SUPER, CHAR, EXTENDS, RETURN,
    THIS, CLASS, FLOAT, SHORT, THROW, CONST, FOR, PACKAGE,
    VOID, CONTINUE, GOTO, PROTECTED, VOLATILE, DO, IF,
    STATIC, WHILE,

    // Java special symbols
    PLUS("+"), MINUS("-"), STAR("*"), SLASH("/"), DOT("."),
    COMMA(","), SEMICOLON(";"), COLON(":"), SINGLE_QUOTE("'"),
    QUOTE("\""), EQUALS("="), NOT_EQUALS("!="),
    LESS_THAN("<"), LESS_EQUALS("<="), PLUS_EQUALS("+="),
    MINUS_EQUALS("-="), STAR_EQUALS("*="), SLASH_EQUALS("/="),
    QUESTION("?"), PIPE_EQUALS("|="), PERCENT_EQUALS("%="),
    AND_EQUALS("&="), UP_ARROW_EQUALS("^="), SHIFT_LEFT__EQUALS("<<="),
    SHIFT_RIGHT_EQUALS(">>="), GREATER_EQUALS(">="), GREATER_THAN(">"),
    LEFT_PAREN("("), RIGHT_PAREN(")"), LEFT_BRACKET("["),
    RIGHT_BRACKET("]"), LEFT_BRACE("{"), RIGHT_BRACE("}"),
    UP_ARROW("^"), AND("&"), AND_AND("&&"), OR_OR("||"),
    PLUS_PLUS("++"), MINUS_MINUS("--"), OR("|"), TILDE("~"),
    SLASH_STAR("/*"), STAR_SLASH("*/"), EQUAL_EQUALS("=="), NOT("!"), AT("@"),
    MODULO("%"), SLASH_SLASH("//"), SHIFT_LEFT("<<"), SHIFT_RIGHT(">>"),

    IDENTIFIER, CHARACTER, INTEGER, REAL, STRING,
    ERROR, END_OF_FILE;

    private static final int FIRST_RESERVED_INDEX = ABSTRACT.ordinal();
    private static final int LAST_RESERVED_INDEX  = WHILE.ordinal();

    private static final int FIRST_SPECIAL_INDEX = PLUS.ordinal();
    private static final int LAST_SPECIAL_INDEX  = SHIFT_RIGHT.ordinal();

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

    public static HashSet<String> RESERVED_WORDS = new HashSet<String>();
    static {
        JavaTokenType values[] = JavaTokenType.values();
        for (int i = FIRST_RESERVED_INDEX; i <= LAST_RESERVED_INDEX; ++i) {
            RESERVED_WORDS.add(values[i].getText().toLowerCase());
        }
    }

    // Hash table of Pascal special symbols.  Each special symbol's text
    // is the key to its Pascal token type.
    public static Hashtable<String, JavaTokenType> SPECIAL_SYMBOLS =
            new Hashtable<String, JavaTokenType>();
    static {
        JavaTokenType values[] = JavaTokenType.values();
        for (int i = FIRST_SPECIAL_INDEX; i <= LAST_SPECIAL_INDEX; ++i) {
            SPECIAL_SYMBOLS.put(values[i].getText(), values[i]);
        }
    }
}

