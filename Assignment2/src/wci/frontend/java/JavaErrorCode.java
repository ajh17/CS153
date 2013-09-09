package wci.frontend.java;

public enum JavaErrorCode {

    // TODO: Add java error codes here; still incomplete list!
    CASE_CONSTANT_REUSED("CASE constant reused"),
    MISSING_WHILE("while missing after do loop."),
    MISSING_IDENTIFIER("Missing identifier"),
    MISSING_LEFT_BRACE("Missing {"),
    MISSING_RIGHT_BRACE("Missing }"),
    MISSING_LEFT_BRACKET("Missing ["),
    MISSING_RIGHT_BRACKET("Missing ]"),
    MISSING_RIGHT_PAREN("Missing )"),
    MISSING_SEMICOLON("Missing ;"),
    MISSING_COMMA("Missing ,"),
    MISSING_COLON("Missing :"),
    MISSING_PERIOD("Missing ."),
    MISSING_TYPE("Missing identifier type"),
    IDENTIFIER_REDEFINED("Redefined identifier"),
    IDENTIFIER_UNDEFINED("Undefined identifier"),
    INCOMPATIBLE_ASSIGNMENT("Incompatible assignment"),
    INCOMPATIBLE_TYPES("Incompatible types"),
    INVALID_ASSIGNMENT("Invalid assignment statement"),
    INVALID_CHARACTER("Invalid character"),
    INVALID_CONSTANT("Invalid constant"),
    INVALID_EXPONENT("Invalid exponent"),
    INVALID_EXPRESSION("Invalid expression"),
    INVALID_FIELD("Invalid field"),
    MISSING_FOR_CONTROL("Invalid for control variable"),
    INVALID_FRACTION("Invalid fraction"),
    INVALID_IDENTIFIER_USAGE("Invalid identifier usage"),
    INVALID_INDEX_TYPE("Invalid index type"),
    INVALID_INTEGER("Invalid integer"),
    INVALID_STATEMENT("Invalid statement"),
    RANGE_INTEGER("Integer literal out of range"),
    RANGE_REAL("Real literal out of range"),
    STACK_OVERFLOW("Stack overflow"),
    UNEXPECTED_EOF("Unexpected end of file"),
    WRONG_NUMBER_OF_PARMS("Wrong number of actual parameters"),

    // fatal errors
    IO_ERROR(-101, "Object I/O error"),
    TOO_MANY_ERRORS(-102, "Too many syntax errors");

    private int status;      // exit status
    private String message;  // error message

    /**
     * Constructor.
     * @param message the error message.
     */
    JavaErrorCode(String message)
    {
        this.status = 0;
        this.message = message;
    }

    /**
     * Constructor.
     * @param status the exit status.
     * @param message the error message.
     */
    JavaErrorCode(int status, String message)
    {
        this.status = status;
        this.message = message;
    }

    /**
     * Getter.
     * @return the exit status.
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @return the message.
     */
    public String toString()
    {
        return message;
    }
}
