package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.Definition;
import wci.intermediate.SymTabEntry;
import wci.intermediate.TypeFactory;
import wci.intermediate.TypeSpec;
import wci.intermediate.symtabimpl.DefinitionImpl;

import static wci.frontend.pascal.PascalErrorCode.IDENTIFIER_UNDEFINED;
import static wci.intermediate.typeimpl.TypeFormImpl.SET;

public class SetTypeParser extends TypeSpecificationParser {

    protected SetTypeParser(PascalParserTD parent) {
        super(parent);
    }

    public TypeSpec parse(Token token) throws Exception {
        // Synchronize at the start of a type specification.

        // TODO: Where should we synchronize to???
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

                        if (definition == DefinitionImpl.TYPE) {
                            id.appendLineNumber(token.getLineNumber());
                            setType.setIdentifier(id);
                            token = nextToken();  // consume the identifier

                            return setType;
                        }
                        else {
                            // TODO: Not sure what to do here...
                            return null; // Temporary until we find out what to do.
                        }
                    }
                    else {
                        errorHandler.flag(token, IDENTIFIER_UNDEFINED, this);
                        token = nextToken();  // consume the identifier
                        return null;
                    }
                case INTEGER:
                    SubrangeTypeParser subrangeTypeParser = new SubrangeTypeParser(this);
                    return subrangeTypeParser.parse(token);
                case LEFT_PAREN:
                    EnumerationTypeParser enumerationTypeParser = new EnumerationTypeParser(this);
                    return enumerationTypeParser.parse(token);
                default:
                    // If it reaches here, it is an error
                    return null;
            }
        }
        else {
            // Invalid syntax for SET
            return null; // Temporary to prevent build error
        }
    }
}
