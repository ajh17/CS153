package wci.frontend.java;

import wci.frontend.*;

public class JavaScanner extends Scanner {

    public JavaScanner(Source source)
    {
        super(source);
    }

    @Override
    protected Token extractToken() throws Exception
    {
        skipWhiteSpace();

        Token token;
        char currentChar = currentChar();

        if (currentChar == EOF) {
            token = new EofToken(source);
        }
        // bunch of else ifs here for other java tokens.
        // TODO: modify frontend JavaToken
        else {
            token = new JavaErrorToken();
        }
        return token;
    }

    /**
     * Skip whitespace characters by consuming them.  A comment is whitespace.
     * @throws Exception if an error occurred.
     */
    private void skipWhiteSpace()
            throws Exception
    {
        char currentChar = currentChar();

        // TODO: Make comments whitespace for Java
        while (Character.isWhitespace(currentChar) || (currentChar == '/')) {

            // Start of a Java comment?
            if (currentChar == '/') {
                do {
                    currentChar = nextChar();  // consume comment characters
                } while ((currentChar != '}') && (currentChar != EOF));

                // Found closing '}'?
                if (currentChar == '}') {
                    currentChar = nextChar();  // consume the '}'
                }
            }

            // Not a comment.
            else {
                currentChar = nextChar();  // consume whitespace character
            }
        }
    }
}

