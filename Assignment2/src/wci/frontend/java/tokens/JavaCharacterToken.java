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

        char currentChar = nextChar();  // consume initial quote
        textBuffer.append('\'');

        if (currentChar == '\\') {   // check for escape character
            currentChar = nextChar();  // consume escape character

            switch (currentChar) {
                case '\\':
                case '\'': textBuffer.append(currentChar); break;
                case 'n': textBuffer.append('\n'); break;
                case 't': textBuffer.append('\t'); break;

                // An error if not a character in the switch statement
                default:
                    type = ERROR;
                    value = INVALID_CHARACTER;
                    text = String.valueOf(currentChar);
            }
        }

        // Continue to parse only if an error was not caused by an invalid escape character sequence
        if (type != ERROR) {
            textBuffer.append(currentChar); // append the character between single quotes
            currentChar = nextChar();

            if (currentChar == '\'') {
                nextChar();  // consume final quote
                textBuffer.append('\'');

                type = CHARACTER;
                value = textBuffer.toString();
            }
            else {
                type = ERROR;
                value = UNEXPECTED_EOF;
            }

            text = textBuffer.toString();
        }
    }
}
