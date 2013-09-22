package wci.backend.interpreter.executors;

import wci.intermediate.*;
import wci.intermediate.icodeimpl.*;
import wci.backend.interpreter.*;

import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;
import static wci.backend.interpreter.RuntimeErrorCode.*;

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

    private Object executeBinaryOperator(ICodeNode node,
        ICodeNodeTypeImpl nodeType)
    {
        return 0;  // should never get here
    }
}
