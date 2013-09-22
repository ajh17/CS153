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
        HashSet<Integer> operand1 = (HashSet<Integer>) this.execute(children.get(0));
        HashSet<Integer> operand2 = (HashSet<Integer>) this.execute(children.get(1));

        switch (nodeType) {
            case SET_UNION:
                return operand1.addAll(operand2);
            case SET_DIFFERENCE:
                return operand1.removeAll(operand2);
            case SET_INTERSECT:
                break;
            case SET_SUBSET:
                return operand2.containsAll(operand1);
            case SET_SUPERSET:
                return operand1.containsAll(operand2);
            case SET_EQUAL:
                return (operand1.containsAll(operand2) && operand2.containsAll(operand1));
            case SET_NOT_EQUAL:
                return (!operand1.containsAll(operand2) || !operand2.containsAll(operand1));
        }
        return 0;  // should never get here
    }
}
