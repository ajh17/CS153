package wci.backend.interpreter.executors;

import wci.backend.interpreter.*;
import wci.intermediate.*;
import wci.intermediate.icodeimpl.*;

import java.util.*;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;

public class SetExpressionExecutor extends ExpressionExecutor {

    public SetExpressionExecutor(Executor parent)
    {
        super(parent);
    }

    // Set of set operator node types.
    private static final EnumSet<ICodeNodeTypeImpl> SET_OPS =
            EnumSet.of (
                    SET_UNION, SET_DIFFERENCE, SET_INTERSECT, SET_SUBSET, SET_SUPERSET,
                    SET_EQUAL, SET_NOT_EQUAL
            );

    public Object execute(ICodeNode node)
    {
        ICodeNodeTypeImpl nodeType = (ICodeNodeTypeImpl) node.getType();

        switch (nodeType) {
            case SET: {
                return node.getAttribute(VALUE);
            }
            default: return executeBinaryOperator(node, nodeType);
        }
    }

    private Object executeBinaryOperator(ICodeNode node,
        ICodeNodeTypeImpl nodeType)
    {
        // Get the two operand children
        ArrayList<ICodeNode> children = node.getChildren();
        Object operand1 = this.execute(children.get(0));
        Object operand2 = this.execute(children.get(1));

        switch (nodeType) {
            case SET_UNION:
                break;
            case SET_DIFFERENCE:
                break;
            case SET_INTERSECT:
                break;
            case SET_SUBSET:
                break;
            case SET_SUPERSET:
                break;
            case SET_EQUAL:
                break;
            case SET_NOT_EQUAL:
                break;
        }
        return 0;  // should never get here
    }
}
