package wci.frontend.java.tokens;

import wci.frontend.Source;
import wci.frontend.java.JavaToken;
import static wci.frontend.java.JavaTokenType.*;
import static wci.frontend.java.JavaErrorCode.*;

public class JavaCharacterToken extends JavaToken {

    /**
     * Constructor.
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public JavaCharacterToken(Source source)
            throws Exception
    {
        super(source);
    }

    /**
     * Extract a Java character token from the source.
     * @throws Exception if an error occurred.
     */
    protected void extract() throws Exception
    {
        StringBuilder textBuffer = new StringBuilder();
        StringBuilder valueBuffer = new StringBuilder();

        char currentChar = nextChar();  // consume initial quote
        textBuffer.append('\'');
        valueBuffer.append('\'');

        if (currentChar == '\\') {   // check for escape character
            currentChar = nextChar();  // consume '\' character
            textBuffer.append('\\');
            valueBuffer.append('\\');

            switch (currentChar) {
                case '\\':
                case '\'':
                case 'n':
                case 't':
                    textBuffer.append(currentChar);
                    valueBuffer.append(currentChar);
                    break;
                default: // An error if not a character in the switch statement
                    type = ERROR;
                    value = INVALID_CHARACTER;
            }

            if (type != ERROR) {
                currentChar = nextChar();  // consume escape character

                if (currentChar == '\'') {
                    textBuffer.append('\'');
                    valueBuffer.append(currentChar);
                    nextChar(); // consume final quote
                    type = CHARACTER;
                    value = valueBuffer.toString();
                }
                else {
                    type = ERROR;
                    value = UNEXPECTED_EOF;
                }
            }
        }

        // Continue to parse only if an error was not caused by an invalid escape character sequence
        else {
            textBuffer.append(currentChar); // append the character between single quotes
            currentChar = nextChar(); // consume character

            if (currentChar == '\'') {
                nextChar();  // consume final quote
                textBuffer.append('\'');
                valueBuffer.append('\'');
                type = CHARACTER;
                value = textBuffer.toString();
            }
            else {
                type = ERROR;
                value = UNEXPECTED_EOF;
            }
        }

        text = textBuffer.toString();
    }
}
