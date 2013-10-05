package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.TypeSpec;

public class SetTypeParser extends TypeSpecificationParser {
    protected SetTypeParser(PascalParserTD parent)
    {
        super(parent);
    }

    public TypeSpec parse(Token token)
            throws Exception
    {
        // Synchronize at the start of a type specification.

        // Synchronize to what?
        // token = synchronize();

        switch ((PascalTokenType) token.getType()) {

        }

        return null; // Temporary to prevent build error
    }
}
