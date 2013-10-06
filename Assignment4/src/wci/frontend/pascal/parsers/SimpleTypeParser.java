package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.Definition;
import wci.intermediate.SymTabEntry;
import wci.intermediate.TypeForm;
import wci.intermediate.TypeSpec;
import wci.intermediate.symtabimpl.DefinitionImpl;

import java.util.EnumSet;

import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.frontend.pascal.PascalTokenType.*;
import static wci.intermediate.symtabimpl.DefinitionImpl.CONSTANT;
import static wci.intermediate.symtabimpl.DefinitionImpl.ENUMERATION_CONSTANT;

/**
 * <h1>SimpleTypeParser</h1>
 *
 * <p>Parse a simple Pascal type (identifier, subrange, enumeration)
 * specification.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
class SimpleTypeParser extends TypeSpecificationParser
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    protected SimpleTypeParser(PascalParserTD parent)
    {
        super(parent);
    }

    // Synchronization set for starting a simple type specification.
    static final EnumSet<PascalTokenType> SIMPLE_TYPE_START_SET =
        ConstantDefinitionsParser.CONSTANT_START_SET.clone();
    static {
        SIMPLE_TYPE_START_SET.add(LEFT_PAREN);
        SIMPLE_TYPE_START_SET.add(SET);
        SIMPLE_TYPE_START_SET.add(COMMA);
        SIMPLE_TYPE_START_SET.add(SEMICOLON);
    }

    /**
     * Parse a simple Pascal type specification.
     * @param token the current token.
     * @return the simple type specification.
     * @throws Exception if an error occurred.
     */
    public TypeSpec parse(Token token)
        throws Exception
    {
        // Synchronize at the start of a simple type specification.
        token = synchronize(SIMPLE_TYPE_START_SET);

        switch ((PascalTokenType) token.getType()) {

            case IDENTIFIER: {
                String name = token.getText().toLowerCase();
                SymTabEntry id = symTabStack.lookup(name);

                if (id != null) {
                    Definition definition = id.getDefinition();

                    // It's either a type identifier
                    // or the start of a subrange type.
                    if (definition == DefinitionImpl.TYPE) {
                        id.appendLineNumber(token.getLineNumber());
                        token = nextToken();  // consume the identifier

                        // Return the type of the referent type.
                        return id.getTypeSpec();
                    }
                    else if ((definition != CONSTANT) &&
                             (definition != ENUMERATION_CONSTANT)) {
                        errorHandler.flag(token, NOT_TYPE_IDENTIFIER, this);
                        token = nextToken();  // consume the identifier
                        return null;
                    }
                    else {
                        SubrangeTypeParser subrangeTypeParser =
                            new SubrangeTypeParser(this);
                        return subrangeTypeParser.parse(token);
                    }
                }
                else {
                    errorHandler.flag(token, IDENTIFIER_UNDEFINED, this);
                    token = nextToken();  // consume the identifier
                    return null;
                }
            }

            case SET:
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

                                    // Return the type of the referent type.
                                    return (TypeSpec) id.getTypeSpec().clone();
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

            case LEFT_PAREN: {
                EnumerationTypeParser enumerationTypeParser = new EnumerationTypeParser(this);
                return enumerationTypeParser.parse(token);
            }

            case COMMA:
            case SEMICOLON: {
                errorHandler.flag(token, INVALID_TYPE, this);
                return null;
            }

            default: {
                SubrangeTypeParser subrangeTypeParser = new SubrangeTypeParser(this);
                return subrangeTypeParser.parse(token);
            }
        }
    }
}
