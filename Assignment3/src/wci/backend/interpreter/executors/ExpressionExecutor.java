package wci.backend.interpreter.executors;

import wci.backend.interpreter.*;
import wci.intermediate.*;
import wci.intermediate.icodeimpl.*;

import java.util.*;

import static wci.backend.interpreter.RuntimeErrorCode.*;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;

/**
 * <h1>ExpressionExecutor</h1>
 *
 * <p>Execute an expression.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only. No warranties.</p>
 */
public class ExpressionExecutor extends StatementExecutor {

    /**
     * Constructor.
     *
     * @param parent the parent executor.
     */
    public ExpressionExecutor(Executor parent) {
        super(parent);
    }

    /**
     * Execute an expression.
     *
     * @param node the root intermediate code node of the compound statement.
     * @return the computed value of the expression.
     */
    public Object execute(ICodeNode node) {
        ICodeNodeTypeImpl nodeType = (ICodeNodeTypeImpl) node.getType();
        ArrayList<ICodeNode> children;

        switch (nodeType) {
            case SET:
                HashSet<Integer> values = (HashSet<Integer>) node.getAttribute(VALUE);
                values.clear(); // Must clear the state and repopulate the values in case it depends on a variable
                children = node.getChildren();

                if (!children.isEmpty()) {
                    for (ICodeNode childNode : children) {
                        switch ((ICodeNodeTypeImpl) childNode.getType()) {
                            case SUBRANGE:
                                ArrayList<ICodeNode> subrangeChildren = childNode.getChildren();
                                Integer leftRange = (Integer) execute(subrangeChildren.get(0));
                                Integer rightRange = (Integer) execute(subrangeChildren.get(1));
                                while (leftRange <= rightRange) {
                                    values.add(leftRange++);
                                }
                                break;
                            case ADD:
                            case SUBTRACT:
                            case MULTIPLY:
                                values.add((Integer) executeBinaryOperator(childNode, (ICodeNodeTypeImpl) childNode.getType()));
                            case INTEGER_CONSTANT:
                            case VARIABLE:
                                values.add((Integer) execute(childNode));
                                break;
                        }
                    }
                }

                return values;

            case IN_SET:
                return executeBinaryOperator(node, nodeType);

            case VARIABLE: {

                // Get the variable's symbol table entry and return its value.
                SymTabEntry entry = (SymTabEntry) node.getAttribute(ID);
                return entry.getAttribute(DATA_VALUE);
            }

            case INTEGER_CONSTANT: {

                // Return the integer value.
                return (Integer) node.getAttribute(VALUE);
            }

            case REAL_CONSTANT: {

                // Return the float value.
                return (Float) node.getAttribute(VALUE);
            }

            case STRING_CONSTANT: {

                // Return the string value.
                return (String) node.getAttribute(VALUE);
            }

            case NEGATE: {

                // Get the NEGATE node's expression node child.
                children = node.getChildren();
                ICodeNode expressionNode = children.get(0);

                // Execute the expression and return the negative of its value.
                Object value = execute(expressionNode);
                if (value instanceof Integer) {
                    return -((Integer) value);
                } else {
                    return -((Float) value);
                }
            }

            case NOT: {

                // Get the NOT node's expression node child.
                children = node.getChildren();
                ICodeNode expressionNode = children.get(0);

                // Execute the expression and return the "not" of its value.
                boolean value = (Boolean) execute(expressionNode);
                return !value;
            }

            // Must be a binary operator.
            default:
                return executeBinaryOperator(node, nodeType);
        }
    }
    // Set of arithmetic and set operator node types.
    private static final EnumSet<ICodeNodeTypeImpl> ARITH_OPS =
            EnumSet.of(ADD, SUBTRACT, MULTIPLY, FLOAT_DIVIDE, INTEGER_DIVIDE, MOD, IN_SET, EQ, LE, NE, GE);

    /**
     * Execute a binary operator.
     *
     * @param node the root node of the expression.
     * @param nodeType the node type.
     * @return the computed value of the expression.
     */
    private Object executeBinaryOperator(ICodeNode node,
            ICodeNodeTypeImpl nodeType) {
        // Get the two operand children of the operator node.
        ArrayList<ICodeNode> children = node.getChildren();
        ICodeNode operandNode1 = children.get(0);
        ICodeNode operandNode2 = children.get(1);

        // Operands.
        Object operand1 = execute(operandNode1);
        Object operand2 = execute(operandNode2);

        boolean integerMode = (operand1 instanceof Integer)
                && (operand2 instanceof Integer);

        boolean setMode =
                (operand1 instanceof HashSet || operand1 instanceof Integer) && (operand2 instanceof HashSet);

        // Use these only if performing set operations
        HashSet<Integer> set1 = operand1 instanceof HashSet ? (HashSet<Integer>) operand1 : null;
        HashSet<Integer> set2 = operand2 instanceof HashSet ? (HashSet<Integer>) operand2 : null;

        // ====================
        // Arithmetic operators
        // ====================

        if (ARITH_OPS.contains(nodeType)) {
            if (integerMode) {
                int value1 = (Integer) operand1;
                int value2 = (Integer) operand2;

                // Integer operations.
                switch (nodeType) {
                    case ADD:
                        return value1 + value2;
                    case SUBTRACT:
                        return value1 - value2;
                    case MULTIPLY:
                        return value1 * value2;
                    case FLOAT_DIVIDE: {

                        // Check for division by zero.
                        if (value2 != 0) {
                            return ((float) value1) / ((float) value2);
                        } else {
                            errorHandler.flag(node, DIVISION_BY_ZERO, this);
                            return 0;
                        }
                    }

                    case INTEGER_DIVIDE: {

                        // Check for division by zero.
                        if (value2 != 0) {
                            return value1 / value2;
                        } else {
                            errorHandler.flag(node, DIVISION_BY_ZERO, this);
                            return 0;
                        }
                    }

                    case MOD: {

                        // Check for division by zero.
                        if (value2 != 0) {
                            return value1 % value2;
                        } else {
                            errorHandler.flag(node, DIVISION_BY_ZERO, this);
                            return 0;
                        }
                    }
                case EQ:
                    return value1 == value2;
                case NE:
                    return value1 != value2;
                case LT:
                    return value1 < value2;
                case LE:
                    return value1 <= value2;
                case GT:
                    return value1 > value2;
                case GE:
                    return value1 >= value2;
                }
            } else if (setMode) {
                HashSet<Integer> tempSet;

                switch (nodeType) {
                    case ADD: // S1 + S2
                        tempSet = makeHashSetCopy(set1);
                        (tempSet).addAll(set2);
                        return tempSet;
                    case SUBTRACT: // S1 - S2
                        tempSet = makeHashSetCopy(set1);
                        (tempSet).removeAll(set2);
                        return tempSet;
                    case MULTIPLY:  // S1 * S2
                        tempSet = makeHashSetCopy(set1);
                        tempSet.retainAll(set2);
                        return tempSet;
                    default:
                        errorHandler.flag(node, INVALID_OPERATOR, this);
                        break;
                }
            } else {
                float value1 = operand1 instanceof Integer ? (Integer) operand1 : (Float) operand1;
                float value2 = operand2 instanceof Integer ? (Integer) operand2 : (Float) operand2;
                // Float operations.
                switch (nodeType) {
                    case ADD:
                        return value1 + value2;
                    case SUBTRACT:
                        return value1 - value2;
                    case MULTIPLY:
                        return value1 * value2;

                    case FLOAT_DIVIDE: {

                        // Check for division by zero.
                        if (value2 != 0.0f) {
                            return value1 / value2;
                        } else {
                            errorHandler.flag(node, DIVISION_BY_ZERO, this);
                            return 0.0f;
                        }
                    }
                }
            }
        } // ==========
        // AND and OR
        // ==========
        else if ((nodeType == AND) || (nodeType == OR)) {
            boolean value1 = (Boolean) operand1;
            boolean value2 = (Boolean) operand2;

            switch (nodeType) {
                case AND:
                    return value1 && value2;
                case OR:
                    return value1 || value2;
            }
        } // ====================
        // Relational operators
        // ====================
        else if (integerMode) {
            int value1 = (Integer) operand1;
            int value2 = (Integer) operand2;

            // Integer operands.
            switch (nodeType) {
                case EQ:
                    return value1 == value2;
                case NE:
                    return value1 != value2;
                case LT:
                    return value1 < value2;
                case LE:
                    return value1 <= value2;
                case GT:
                    return value1 > value2;
                case GE:
                    return value1 >= value2;
            }
        }
        else if (setMode) {
            switch (nodeType) {
                case LE:  // S1 <= S2
                    return set2.containsAll(set1);
                case GE:  // S1 >= S2
                    return set1.containsAll(set2);
                case EQ: // S1 = S2
                    return set1.containsAll(set2) && set2.containsAll(set1);
                case NE:  // S1 <> S2
                    return !set1.containsAll(set2) || !set2.containsAll(set1);
                case IN_SET:  // s in S1
                    // TODO: Figure out why this is not flagging error8 & error9 as errors.
                    if (operand1 instanceof Integer) {
                        return set2.contains(operand1);
                    }
                    errorHandler.flag(node, INVALID_OPERATOR, this);
                    return 0;
                default:
                    errorHandler.flag(node, INVALID_OPERATOR, this);
                    return 0;
            }
        }
        else {
            float value1 = operand1 instanceof Integer
                    ? (Integer) operand1 : (Float) operand1;
            float value2 = operand2 instanceof Integer
                    ? (Integer) operand2 : (Float) operand2;

            // Float operands.
            switch (nodeType) {
                case EQ:
                    return value1 == value2;
                case NE:
                    return value1 != value2;
                case LT:
                    return value1 < value2;
                case LE:
                    return value1 <= value2;
                case GT:
                    return value1 > value2;
                case GE:
                    return value1 >= value2;
            }
        }

        return 0;  // should never get here
    }

    HashSet<Integer> makeHashSetCopy(HashSet<Integer> set) {
        HashSet<Integer> newSet = new HashSet<Integer>();
        // make a deep copy of HashSet
        for (Integer i : set) {
            newSet.add(i.intValue());
        }

        return newSet;
    }
}
