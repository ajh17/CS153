package wci.frontend.java.tokens;

import wci.frontend.Source;
import wci.frontend.java.JavaToken;
import static wci.frontend.java.JavaErrorCode.*;
import static wci.frontend.pascal.PascalTokenType.*;

public class JavaSpecialSymbolToken extends JavaToken {

    /**
     * Constructor.
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public JavaSpecialSymbolToken(Source source)
            throws Exception
    {
        super(source);
    }

    protected void extract() throws Exception
    {
        char currentChar = currentChar();

        text = Character.toString(currentChar);
        type = null;

        switch (currentChar) {

            // Single-character special symbols.
            case ',':  case '@': case ';':  case '\'': case '(':
            case ')': case '[':  case ']':  case '{':  case '}':
            case '.':  case ':':  case '~': case '?': case '"':
            {
                nextChar();  // consume character
                break;
            }

            // TODO: Double check that I haven't missed anything. - akshay
            // < or <= or << or <<=
            case '<': {
                currentChar = nextChar();  // consume '<';

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar();  // consume '='
                }
                else if (currentChar == '<') {
                    text += currentChar;
                    nextChar();  // consume '<'
                    if (currentChar == '=') {
                        text += currentChar;
                        nextChar(); // consume '='
                    }
                }

                break;
            }

            // > or >= or >> or >>=
            case '>': {
                currentChar = nextChar();  // consume '>'

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar();  // consume '='
                }
                else if (currentChar == '>') {
                    text += currentChar;
                    nextChar(); // consume '>'
                    if (currentChar == '=') {
                        text += currentChar;
                        nextChar(); // consume '='
                    }
                }

                break;
            }

            // + or ++ or +=
            case '+': {
                currentChar = nextChar(); // consume '+'

                if (currentChar == '+') {
                    text += currentChar;
                    nextChar(); // consume another '+'
                }
                else if (currentChar == '=') {
                    text += currentChar;
                    nextChar(); // consume '='

                }
            }

            // - or -- or -=
            case '-': {
                currentChar = nextChar(); // consume '-'

                if (currentChar == '-') {
                    text += currentChar;
                    nextChar(); // consume another '-'
                }
                else if (currentChar == '=') {
                    text += currentChar;
                    nextChar(); // consume '='

                }
            }

            // * or *= or */
            case '*': {
                currentChar = nextChar(); // consume '*'

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar(); // consume '='
                }
                else if (currentChar == '/') {
                    text += currentChar;
                    nextChar(); // consume '/'
                }
            }


            // / or /= or /* or //
            case '/': {
                currentChar = nextChar(); // consume '/'

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar(); // consume '='
                }
                else if (currentChar == '*') {
                    text += currentChar;
                    nextChar(); // consume '*'
                }
                else if (currentChar == '/') {
                    text += currentChar;
                    nextChar(); // consume another '/'
                }
            }

            // | or |= or ||
            case '|': {
                currentChar = nextChar(); // consume '|'

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar(); // consume '='
                }
                else if (currentChar == '|') {
                    text += currentChar;
                    nextChar(); // consume another '|'
                }
            }

            // & or && or &=
            case '&': {
                currentChar = nextChar(); // consume '&'

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar(); // consume '='
                }
                else if (currentChar == '&') {
                    text += currentChar;
                    nextChar(); // consume another '&'
                }
            }

            // ! or !=
            case '!': {
                currentChar = nextChar(); // consume '!'

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar(); // consume '='
                }
            }

            // ^ or ^=
            case '^': {
                currentChar = nextChar(); // consume '^'

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar(); // consume '='
                }
            }

            // % or %=
            case '%': {
                currentChar = nextChar(); // consume '%'

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar(); // consume '='
                }
            }

            // = or ==
            case '=': {
                currentChar = nextChar(); // consume '='

                if (currentChar == '=') {
                    text += currentChar;
                    nextChar(); // consume another '='
                }
            }

            default: {
                nextChar();  // consume bad character
                type = ERROR;
                value = INVALID_CHARACTER;
            }
        }

        // Set the type if it wasn't an error.
        if (type == null) {
            type = SPECIAL_SYMBOLS.get(text);
        }
    }

}
