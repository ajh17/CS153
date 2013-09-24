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

    private Object executeBinaryOperator(ICodeNode node, ICodeNodeTypeImpl nodeType)
    {
        // Get the two operand children
        ArrayList<ICodeNode> children = node.getChildren();
        HashSet<Integer> operand1 = (HashSet<Integer>) execute(children.get(0));
        HashSet<Integer> operand2 = (HashSet<Integer>) execute(children.get(1));

        switch (nodeType) {
            case SET_UNION: // S1 + S2
                return operand1.addAll(operand2);
            case SET_DIFFERENCE: // S1 - S2
                return operand1.removeAll(operand2);
            case SET_INTERSECT:  // S1 * S2
                return operand1.retainAll(operand2);
            case SET_SUBSET:  // S1 <= S2
                return operand2.containsAll(operand1);
            case SET_SUPERSET:  // S1 >= S2
                return operand1.containsAll(operand2);
            case SET_EQUAL: // S1 = S2
                return (operand1.containsAll(operand2) && operand2.containsAll(operand1));
            case SET_NOT_EQUAL:  // S1 <> S2
                return (!operand1.containsAll(operand2) || !operand2.containsAll(operand1));
            case CONTAINED_IN_SET:  // s in S1
                int value = (Integer) execute(children.get(0));
                return operand2.contains(value);
        }
        return 0;  // should never get here
    }
}
