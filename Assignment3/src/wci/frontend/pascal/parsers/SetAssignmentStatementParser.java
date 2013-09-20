package wci.frontend.pascal.parsers;

import wci.frontend.*;
import wci.frontend.pascal.*;
import wci.intermediate.*;

import java.util.EnumSet;

import static wci.frontend.pascal.PascalTokenType.*;
import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;

public class SetAssignmentStatementParser extends AssignmentStatementParser {

    private static final EnumSet<PascalTokenType> COLON_EQUALS_SET = ExpressionParser.EXPR_START_SET.clone();

    static {
        COLON_EQUALS_SET.add(COLON_EQUALS);
        COLON_EQUALS_SET.addAll(AssignmentStatementParser.STMT_FOLLOW_SET);
    }

    public SetAssignmentStatementParser(PascalParserTD parent)
    {
        super(parent);
    }

    /**
     * Parse the set assignment statement.
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occured
     */
    public ICodeNode parse(Token token) throws Exception
    {
        // Create the ASSIGN node
        ICodeNode assignNode = ICodeFactory.createICodeNode(ASSIGN);

        // Look up target identifier and add it to the stack if not found
        String targetName = token.getText().toLowerCase();
        SymTabEntry targetId = symTabStack.lookup(targetName);

        if (targetId == null) {
            targetId = symTabStack.enterLocal(targetName);
        }
        targetId.appendLineNumber(token.getLineNumber());

        token = nextToken(); // Consume identifier

        // Create the variable node and set its name attribute.
        ICodeNode variableNode = ICodeFactory.createICodeNode(VARIABLE);
        variableNode.setAttribute(ID, targetId);

        assignNode.addChild(variableNode);

        // Synchronization set for the = token.
        token = synchronize(EQUALS_SET);
        if (token.getType() == EQUALS) {
            token = nextToken(); // consume the =
        }
        else {
            errorHandler.flag(token, MISSING_EQUALS, this);
        }

        AssignmentStatementParser assignmentParser = new AssignmentStatementParser(this);
        assignNode.addChild(assignmentParser.parse(token));

        return assignNode;
    }
}
