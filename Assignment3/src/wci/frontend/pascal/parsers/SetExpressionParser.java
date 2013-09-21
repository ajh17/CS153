package wci.frontend.pascal.parsers;

import wci.frontend.*;
import wci.frontend.pascal.*;
import wci.intermediate.*;
import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

import java.util.HashSet;

import static wci.frontend.pascal.PascalTokenType.*;
import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;

public class SetExpressionParser extends ExpressionParser {

    public SetExpressionParser(PascalParserTD parent) {
        super(parent);
    }

    public ICodeNode parse(Token token)
            throws Exception
    {
        token = nextToken(); // Consume the [

        // This SET is different from PascalTokenType
        ICodeNode rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.SET);
        HashSet<Integer> values = new HashSet<Integer>();

        // Note: NOT SURE IF THIS IS CORRECT. SHOULD WE ADD MULTIPLE CHILDREN WITH CONSTANT INTEGERS?
        rootNode.setAttribute(VALUE, values);

        // Crude way of extracting the set of numbers
        do {
            switch ((PascalTokenType) (token.getType())) {
                // Identifiers and Integers should be treated the same
                case IDENTIFIER: // DID NOT CHECK FOR ERRORS WHEN IDENTIFIER IS NOT ACTUALLY AN INTEGER TYPE
                case INTEGER:
                    Integer leftRange = (Integer) token.getValue(); // getValue() returns Object type. Need to recast.
                    token = nextToken(); // Consume the number or left side subrange

                    switch ((PascalTokenType) (token.getType())) {
                        case COMMA: // The comma should be consumed in the outside switch statement
                        case RIGHT_BRACKET: // The right bracket should be consumed outside the do-while loop
                            break;
                        case DOT_DOT:
                            token = nextToken(); // Consume the ..
                            if (token.getType() == INTEGER || token.getType() == IDENTIFIER) {
                                Integer upto = (Integer) token.getValue();
                                token = nextToken(); // Consume the right subrange

                                if (upto >= 0 && upto <= 50 && upto >= leftRange) {
                                    while (leftRange < upto) {  // Add the range of numbers
                                        values.add(leftRange++);
                                    }
                                }
                                else {
                                    errorHandler.flag(token, RANGE_INTEGER, this);
                                }

                                if (token.getType() == COMMA) {
                                    token = nextToken(); // Consume the , to prepare next iteration of loop
                                }
                                else {
                                    errorHandler.flag(token, UNEXPECTED_TOKEN, this);
                                }
                            }
                            else {
                                errorHandler.flag(token, UNEXPECTED_TOKEN, this);
                            }
                            break;
                        default:
                            // Found an unexpected token in set expression.
                            errorHandler.flag(token, UNEXPECTED_TOKEN, this);
                            break;
                    }
                    if (token.getType() == COMMA) {  // Add as a single number only if succeeding token is a comma
                        if (leftRange >= 0 && leftRange <= 50) {
                            token = nextToken(); // Consume the ,
                            values.add(leftRange);
                        }
                        else {
                            errorHandler.flag(token, RANGE_INTEGER, this); // Report integer being out of range
                        }
                    }
                    break;
                default:
                    // Found an unexpected token in set expression.
                    errorHandler.flag(token, UNEXPECTED_TOKEN, this);
                    break;
            }
        } while (token.getType() != RIGHT_BRACKET && token.getType() != ERROR);

        if (token.getType() == ERROR) {
            errorHandler.flag(token, UNEXPECTED_EOF, this);
        }
        else if (token.getType() == RIGHT_BRACKET) {
            token = nextToken();  // consume the ]
        }
        else {
            errorHandler.flag(token, MISSING_RIGHT_BRACKET, this);
        }
        return rootNode;
    }
}
