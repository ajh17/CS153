package wci.frontend.java;

import wci.frontend.*;
import wci.frontend.java.tokens.*;

import static wci.frontend.Source.EOF;
import static wci.frontend.java.JavaTokenType.*;
import static wci.frontend.java.JavaErrorCode.*;

public class JavaScanner extends Scanner
{

    public JavaScanner(Source source)
    {
        super(source);
    }

    // TODO: complete extract token
    @Override
    protected Token extractToken() throws Exception
    {
        skipWhiteSpace();

        Token token;
        char currentChar = currentChar();

        if (currentChar == EOF)
        {
            token = new EofToken(source);
        } // bunch of else ifs here for other java tokens.
        // TODO: modify frontend JavaToken
        else
        {
            token = new JavaErrorToken(source, INVALID_CHARACTER,
                    Character.toString(currentChar));
        }
        return token;
    }

    /**
     * Skip whitespace characters by consuming them. A comment is whitespace.
     *
     * @throws Exception if an error occurred.
     */
    private void skipWhiteSpace()
            throws Exception
    {
        char currentChar = currentChar();

        // TODO: Make comments whitespace for Java
        while (Character.isWhitespace(currentChar) || (currentChar == '/'))
        {
            // Start of a Java comment?
            if (currentChar == '/')
            {
                currentChar = nextChar();
                // Comment of type /* */
                if (currentChar == '*')
                {
                    do
                    {
                        currentChar = nextChar();  // consume comment characters
                    } while ((!(currentChar == '*' && nextChar() == '/')) && (currentChar != EOF));

                    // consume the */ 
                    if (currentChar == '*')
                    {
                        currentChar = nextChar();

                        if (currentChar == '/' || currentChar == '\n')
                        {
                            currentChar = nextChar();
                        }
                    }
                } // Single line comment
                else if (currentChar == '/')
                {
                    currentChar = nextChar();
                    do
                    {
                        currentChar = nextChar();
                    } while (currentChar != '\n' && currentChar != EOF);
                    currentChar = nextChar(); // consume end of line
                }
            } // Not a comment.
            else
            {
                currentChar = nextChar();  // consume whitespace character
            }

        }
    }
}
