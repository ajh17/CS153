package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.Definition;
import wci.intermediate.SymTabEntry;
import wci.intermediate.TypeFactory;
import wci.intermediate.TypeSpec;
import wci.intermediate.symtabimpl.DefinitionImpl;

import java.util.EnumSet;

import static wci.frontend.pascal.PascalErrorCode.UNEXPECTED_TOKEN;
import static wci.frontend.pascal.PascalTokenType.*;
import static wci.frontend.pascal.PascalErrorCode.IDENTIFIER_UNDEFINED;
import static wci.intermediate.typeimpl.TypeFormImpl.SET;
import static wci.intermediate.typeimpl.TypeKeyImpl.*;

public class SetTypeParser extends TypeSpecificationParser {

    protected SetTypeParser(PascalParserTD parent) {
        super(parent);
    }

    // Synchronization set for starting a simple type specification.
    static final EnumSet<PascalTokenType> SET_TYPE_START_SET =
            ConstantDefinitionsParser.CONSTANT_START_SET.clone();
    static {
        SET_TYPE_START_SET.add(PascalTokenType.SET);
        SET_TYPE_START_SET.add(LEFT_BRACKET);
        SET_TYPE_START_SET.add(SEMICOLON);
    }

    public TypeSpec parse(Token token) throws Exception {
        // Synchronize at the start of a type specification.

        // TODO: Not sure if this is right
        token = synchronize(SET_TYPE_START_SET);

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
                            token = nextToken();  // consume the identifier
                            setType.setAttribute(BASE_TYPE, id.getTypeSpec());
                            //setType.setIdentifier(id);

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
                    // These are unnamed types so we add the values as attributes to the SET TypeSpec
                    SubrangeTypeParser subrangeTypeParser = new SubrangeTypeParser(this);
                    TypeSpec subrange = subrangeTypeParser.parse(token);
                    setType.setAttribute(UNNAMED_SET_VALUES, subrange);
                    return setType;
                case LEFT_PAREN:
                    // These are unnamed types so we add the values as attributes to the SET TypeSpec
                    EnumerationTypeParser enumerationTypeParser = new EnumerationTypeParser(this);
                    TypeSpec enumeration = enumerationTypeParser.parse(token);
                    setType.setAttribute(UNNAMED_SET_VALUES, enumeration);
                    return setType;
                default:
                    // If it reaches here, it is an error
                    token = synchronize(SET_TYPE_START_SET);
                    errorHandler.flag(token, UNEXPECTED_TOKEN, this);
                    return null;
            }
        }
        else {
            // Invalid syntax for SET
            return null; // Temporary to prevent build error
        }
    }
}
