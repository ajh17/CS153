package wci.frontend.java;

public enum JavaErrorCode {

    CANT_INSTANTIATE_ABSTRACT("Can’t instantiate abstract class	"),
    CASE_CONSTANT_REUSED("CASE constant reused"),
    CLASS_NOT_PUBLIC("Class is not public"),
    CANNOT_DIVIDE_ZERO("Cannot divide by zero!"),
    IDENTIFIER_REDEFINED("Redefined identifier"),
    IDENTIFIER_UNDEFINED("Undefined identifier"),
    ILLEGAL_ESCAPE("Illegal escape character"),
    INCOMPATIBLE_ASSIGNMENT("Incompatible assignment"),
    INCOMPATIBLE_TYPES("Incompatible types"),
    INVALID_ASSIGNMENT("Invalid assignment statement"),
    INVALID_CHARACTER("Invalid character"),
    INVALID_CONSTANT("Invalid constant"),
    INVALID_EXPONENT("Invalid exponent"),
    INVALID_EXPRESSION("Invalid expression"),
    INVALID_FIELD("Invalid field"),
    INVALID_FRACTION("Invalid fraction"),
    INVALID_IDENTIFIER_USAGE("Invalid identifier usage"),
    INVALID_INDEX_TYPE("Invalid index type"),
    INVALID_INTEGER("Invalid integer"),
    INVALID_METHOD("Method not found"),
    INVALID_STATEMENT("Invalid statement"),
    MISSING_COLON("Missing :"),
    MISSING_COMMA("Missing ,"),
    MISSING_FOR_CONTROL("Invalid for control variable"),
    MISSING_IDENTIFIER("Missing identifier"),
    MISSING_LEFT_BRACE("Missing {"),
    MISSING_LEFT_BRACKET("Missing ["),
    MISSING_LEFT_PAREN("Missing ("),
    MISSING_PERIOD("Missing ."),
    MISSING_RIGHT_BRACE("Missing }"),
    MISSING_RIGHT_BRACKET("Missing ]"),
    MISSING_RIGHT_PAREN("Missing )"),
    MISSING_SEMICOLON("Missing ;"),
    MISSING_TYPE("Missing identifier type"),
    MISSING_WHILE("while missing after do loop."),
    MISSING_DO("do missing before bracket"),
    RANGE_INTEGER("Integer literal out of range"),
    RANGE_REAL("Real literal out of range"),
    RETURN_NOT_FOUND("Method did not return a value"),
    STACK_OVERFLOW("Stack overflow"),
    UNEXPECTED_EOF("Unexpected end of file"),
    WRONG_NUMBER_OF_PARMS("Wrong number of actual parameters"),
    VOID_METHOD("Method of void type; Cannot return a value"),

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
