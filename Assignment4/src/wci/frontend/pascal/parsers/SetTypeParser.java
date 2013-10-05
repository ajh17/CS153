package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.Definition;
import wci.intermediate.SymTabEntry;
import wci.intermediate.TypeFactory;
import wci.intermediate.TypeSpec;

import static wci.frontend.pascal.PascalErrorCode.IDENTIFIER_UNDEFINED;
import static wci.intermediate.typeimpl.TypeFormImpl.SET;

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

        TypeSpec setType = TypeFactory.createType(SET);
        token = nextToken();  // consume SET

        if (token.getType() == PascalTokenType.OF)  {
            token = nextToken(); // consume OF

            switch ((PascalTokenType) token.getType()) {
                case IDENTIFIER:
                    String name = token.getText().toLowerCase();
                    SymTabEntry id = symTabStack.lookup(name);

                    if (id != null) {
                        Definition definition = id.getDefinition();

                        // Do stuff here?
                    }
                    else {
                        errorHandler.flag(token, IDENTIFIER_UNDEFINED, this);
                        token = nextToken();  // consume the identifier
                        return null;
                    }
                default:
                    // a = SET OF 4..6;    is considered valid
                    SubrangeTypeParser subrangeTypeParser = new SubrangeTypeParser(this);
                    return subrangeTypeParser.parse(token);
            }
        }
        else {
            // Invalid syntax for SET
        }

        return null; // Temporary to prevent build error
    }
}
