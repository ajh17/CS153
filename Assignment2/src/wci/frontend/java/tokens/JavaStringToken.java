package wci.frontend.java.tokens;

import wci.frontend.Source;
import wci.frontend.java.JavaToken;
import static wci.frontend.Source.EOL;
import static wci.frontend.Source.EOF;
import static wci.frontend.java.JavaTokenType.*;
import static wci.frontend.java.JavaErrorCode.*;

public class JavaStringToken extends JavaToken {

    /**
     * Constructor.
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public JavaStringToken(Source source)
            throws Exception
    {
        super(source);
    }

    /**
     * Extract a Java string token from the source.
     * @throws Exception if an error occurred.
     */
    protected void extract() throws Exception
    {
        StringBuilder textBuffer = new StringBuilder();
        StringBuilder valueBuffer = new StringBuilder();

        char currentChar = nextChar();  // consume initial quote
        textBuffer.append('"');

        // Get string characters.
        do {
            if (currentChar == '\\') { // Check '\' for escape characters
                currentChar = nextChar();  // consume '\' character
                textBuffer.append('\\');
                valueBuffer.append('\\');

                switch (currentChar) {
                    case '\\':
                    case '\'':
                    case 'n':
                    case 't':
                    case '"':
                        textBuffer.append(currentChar);
                        valueBuffer.append(currentChar);
                        break;
                    default: // An error if not a character in the switch statement
                        type = ERROR;
                        value = INVALID_CHARACTER;
                }

                currentChar = nextChar(); // consume the escape character
            }
            else if ((currentChar != '"') && (currentChar != EOF)) {
                textBuffer.append(currentChar);
                valueBuffer.append(currentChar);
                currentChar = nextChar();  // consume character
            }
        } while ((currentChar != '"') && (currentChar != EOF));

        if (currentChar == '"') {
            nextChar();  // consume final quote
            textBuffer.append('"');

            type = STRING;
            value = valueBuffer.toString();
        }
        else {
            type = ERROR;
            value = UNEXPECTED_EOF;
        }

        text = textBuffer.toString();
    }
}
