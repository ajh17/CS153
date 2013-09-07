package wci.frontend.java;

public enum JavaErrorCode {

    // TODO: Add java error codes here

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
}
