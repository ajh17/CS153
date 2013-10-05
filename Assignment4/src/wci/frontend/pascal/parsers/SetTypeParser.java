package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.TypeFactory;
import wci.intermediate.TypeSpec;

import static wci.intermediate.typeimpl.TypeFormImpl.ENUMERATION;

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

        // TODO: Verify that ENUMBERATION is the correct type for SET
        TypeSpec setType = TypeFactory.createType(ENUMERATION);
        token = nextToken();  // consume SET

        if (token.getType() == PascalTokenType.OF)  {
            token = nextToken(); // consume OF

            switch ((PascalTokenType) token.getType()) {

            }
        }
        else {
            // Invalid syntax for SET
        }

        return null; // Temporary to prevent build error
    }
}
