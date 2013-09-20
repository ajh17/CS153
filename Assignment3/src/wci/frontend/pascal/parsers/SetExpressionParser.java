package wci.frontend.pascal.parsers;

import wci.frontend.*;
import wci.frontend.pascal.*;
import wci.intermediate.*;

public class SetExpressionParser extends ExpressionParser {

    public SetExpressionParser(PascalParserTD parent) {
        super(parent);
    }

    public ICodeNode parse(Token token) throws Exception {
        return null; // Added so build doesn't give error
    }
}
