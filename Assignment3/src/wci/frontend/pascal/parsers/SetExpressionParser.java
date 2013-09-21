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
        ICodeNode rootNode = null;
        token = nextToken(); // Consume the [

        rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.SET); // This SET is different from PascalTokenType
        HashSet<Integer> values = new HashSet<Integer>();

        // Note: NOT SURE IF THIS IS CORRECT. SHOULD WE ADD MULTIPLE CHILDREN WITH CONSTANT INTEGERS?
        rootNode.setAttribute(VALUE, values);

        // Crude way of extracting the set of numbers
        do {
            if (token.getType() == INTEGER) {
                Integer number = (Integer) token.getValue(); // getValue() returns Object type. Need to recast.

                if (number >= 0 && number <= 50) {
                    values.add(number);
                }
                else {
                    errorHandler.flag(token, RANGE_INTEGER, this); // Report integer being out of range
                }
            }

            token = nextToken();
        } while (token.getType() != RIGHT_BRACKET && token.getType() != ERROR);

        if (token.getType() == ERROR) {
            errorHandler.flag(token, UNEXPECTED_EOF, this);
        }
        else if (token.getType() == RIGHT_PAREN) {
            token = nextToken();  // consume the ]
        }
        else {
            errorHandler.flag(token, MISSING_RIGHT_BRACKET, this);
        }
        return rootNode;
    }
}
