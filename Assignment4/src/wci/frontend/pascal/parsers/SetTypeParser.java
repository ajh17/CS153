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

import static wci.frontend.pascal.PascalErrorCode.MISSING_OF;
import static wci.frontend.pascal.PascalTokenType.*;
import static wci.intermediate.typeimpl.TypeFormImpl.SET;
import static wci.intermediate.typeimpl.TypeKeyImpl.*;

public class SetTypeParser extends TypeSpecificationParser {

    protected SetTypeParser(PascalParserTD parent) {
        super(parent);
    }

    // Synchronization set for SET statement.
    static final EnumSet<PascalTokenType> SET_TYPE_START_SET =  EnumSet.of(SEMICOLON);

    public TypeSpec parse(Token token) throws Exception {
        TypeSpec setType = TypeFactory.createType(SET);
        token = nextToken();  // consume SET

        // Look for OF
        if (token.getType() == PascalTokenType.OF)  {
            token = nextToken(); // consume OF

            // Look for identifier, subrange, or enumeration after OF
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

                            return setType;
                        }
                        else {
                            token = synchronize(SET_TYPE_START_SET);
                            return null;
                        }
                    }
                    else {
                        token = synchronize(SET_TYPE_START_SET);
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
                    // If it reaches here, it is an error so synchronize to the next semicolon
                    token = synchronize(SET_TYPE_START_SET);
                    return null;
            }
        }
        else {
            // MISSING OF, skip to semicolon
            token = synchronize(SET_TYPE_START_SET);
            errorHandler.flag(token, MISSING_OF, this);
            return null;
        }
    }
}
