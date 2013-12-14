package wci.backend.compiler;

import wci.frontend.*;
import wci.intermediate.*;
import wci.intermediate.icodeimpl.ICodeKeyImpl;
import wci.intermediate.symtabimpl.Predefined;
import wci.intermediate.symtabimpl.SymTabImpl;
import wci.intermediate.symtabimpl.SymTabKeyImpl;

import java.util.ArrayList;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;

public class CodeGeneratorVisitor extends GoParserVisitorAdapter implements GoParserTreeConstants
{
    private String programName = null;
    private int tagNumber = 0;

    public String getCurrentLabel() { return "label" + tagNumber; }
    public String getNextLabel() { return "label" + ++tagNumber; }

    public void emitComparisonCode(SimpleNode node, Object data) {
        SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);
        SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);

        TypeSpec lhsType = lhs.getTypeSpec();
        TypeSpec rhsType = rhs.getTypeSpec();

        lhs.jjtAccept(this, data);
        if (lhsType == Predefined.integerType) {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        rhs.jjtAccept(this, data);
        if (rhsType == Predefined.integerType) {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        CodeGenerator.objectFile.println("    fcmpg");
    }

    public Object visit(ASTstatementList node, Object data) {
        if (this.programName == null) {
            this.programName = (String) data;
        }

        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTassignmentStatement node, Object data)
    {
        String programName        = (String) data;
        SimpleNode variableNode   = (SimpleNode) node.jjtGetChild(0);
        SimpleNode expressionNode = (SimpleNode) node.jjtGetChild(1);

        // Emit code for the expression.
        expressionNode.jjtAccept(this, data);
        TypeSpec expressionType = expressionNode.getTypeSpec();

        // Get the assignment target type.
        TypeSpec targetType = node.getTypeSpec();

        // Convert an integer value to float if necessary.
        if ((targetType == Predefined.realType) &&
                (expressionType == Predefined.integerType))
        {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        SymTabEntry id = (SymTabEntry) variableNode.getAttribute(ID);
        String fieldName = id.getName();
        TypeSpec type = id.getTypeSpec();
        String typeCode = null;

        if (type == Predefined.integerType) {
            typeCode = "I";
        }
        else if (type == Predefined.realType) {
            typeCode = "F";
        }
        else if (type == Predefined.charType) {
            typeCode = "Ljava/lang/String;";
        }

        // Emit the appropriate store instruction.
        CodeGenerator.objectFile.println("    putstatic " + programName +
                "/" + fieldName + " " + typeCode);
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTfunctionDeclaration node, Object data) {
        return data;
    }

    public Object visit(ASTfunctionCall node, Object data) {
        String functionId = (String) node.getAttribute(ID);
        String returnType = "V"; // TODO: Need to dynamically check this
        StringBuilder parameters = new StringBuilder();

        parameters.append("I"); // TODO: Hardcoding for my example

        for (int i = 1; i < node.jjtGetNumChildren(); i++) { // Start at 1 to skip function name
            node.jjtGetChild(i).jjtAccept(this, data);
        }

        CodeGenerator.objectFile.println("    invokestatic  " + programName + "/"
                + functionId + "(" + parameters.toString() + ")" + returnType);
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTidentifier node, Object data)
    {
        String programName = (String) data;
        SymTabEntry id = (SymTabEntry) node.getAttribute(ID);
        String fieldName = id.getName();
        TypeSpec type = id.getTypeSpec();
        String typeCode = null;

        if (type == Predefined.integerType) {
            typeCode = "I";
        }
        else if (type == Predefined.realType) {
            typeCode = "F";
        }
        else if (type == Predefined.charType) {
            typeCode = "Ljava/lang/String;";
        }

        // Emit the appropriate load instruction.
        CodeGenerator.objectFile.println("    getstatic " + programName +
                "/" + fieldName + " " + typeCode);
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTintegerConstant node, Object data)
    {
        int value = (Integer) node.getAttribute(VALUE);

        // Emit a load constant instruction.
        CodeGenerator.objectFile.println("    ldc " + value);
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTinterpretedString node, Object data)
    {
        String value = (String) node.getAttribute(VALUE);

        // Emit a load constant instruction.
        CodeGenerator.objectFile.println("    ldc " + value);
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTrealConstant node, Object data)
    {
        float value = (Float) node.getAttribute(VALUE);

        // Emit a load constant instruction.
        CodeGenerator.objectFile.println("    ldc " + value);
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTprintStatement node, Object data)
    {
        CodeGenerator.objectFile.println("    getstatic java/lang/System/out Ljava/io/PrintStream;");
        CodeGenerator.objectFile.flush();

        SimpleNode printNode = (SimpleNode) node.jjtGetChild(0);
        TypeSpec type = printNode.getTypeSpec();
        String typeCode = null;
        printNode.jjtAccept(this, data);

        if (type == Predefined.integerType) {
            typeCode = "I";
        }
        else if (type == Predefined.realType) {
            typeCode = "F";
        }
        else if (type == Predefined.charType) {
            typeCode = "Ljava/lang/String;";
        }

        CodeGenerator.objectFile.println("    invokevirtual java/io/PrintStream/println(" + typeCode + ")V");
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTblock node, Object data)
    {
        node.childrenAccept(this, programName);

        // If the data is not the programName, that means I overwrote the data, so use it.
        // The data is always the programName unless you choose to send a child something else.
        if (data != programName) {
            String label = (String) data;
            CodeGenerator.objectFile.println("    goto " + label);
            CodeGenerator.objectFile.flush();
        }

        return data;
    }

    public Object visit(ASTswitchBlock node, Object data)
    {
        // For now, a clone of the block visit method.
        // Looking to see if it needs any change at all.
        node.childrenAccept(this, programName);
        if (data != programName) {
            String label = (String) data;
            CodeGenerator.objectFile.println("    goto " + label);
            CodeGenerator.objectFile.flush();
        }

        return data;
    }

    public Object visit(ASTforStatement node, Object data)
    {
        SimpleNode forClause = (SimpleNode) node.jjtGetChild(0);
        SimpleNode block = (SimpleNode) node.jjtGetChild(1);

        // If the for clause has 1 child, it is a while loop, so create a label
        if (forClause.jjtGetNumChildren() == 1) {
            String beginLabel = getNextLabel();
            CodeGenerator.objectFile.println(beginLabel + ":"); // Jump back here after each iteration
            CodeGenerator.objectFile.flush();
            String endLabel = (String) forClause.jjtAccept(this, data); // Jump to returned label when loop ends
            block.jjtAccept(this, data);
            CodeGenerator.objectFile.println("    goto " + beginLabel); // Restart loop
            CodeGenerator.objectFile.println(endLabel + ":"); // Jump here when the loop is done
        }
        else {
            ArrayList<Object> loopData = (ArrayList<Object>) forClause.jjtAccept(this, data);
            block.jjtAccept(this, data);
            ((Node) loopData.get(0)).jjtAccept(this, data); // Incrementing after running the body
            CodeGenerator.objectFile.println("    goto " + loopData.get(1));
            CodeGenerator.objectFile.println(loopData.get(2) + ":");
        }

        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTforClause node, Object data) {
        ArrayList<Object> loopData = new ArrayList<Object>();

        // If it has 1 child, it is a while loop
        if (node.jjtGetNumChildren() == 1) {
            return node.jjtGetChild(0).jjtAccept(this, data); // This returns a string label it will be jumping to
        }

        node.jjtGetChild(0).jjtAccept(this, data); // Perform the assignment, but don't include it as part of loop

        String beginLabel = getNextLabel(); // Prepare label for looping after the assignment has been done
        CodeGenerator.objectFile.println(beginLabel + ":");
        CodeGenerator.objectFile.flush();
        String endLabel = (String) node.jjtGetChild(1).jjtAccept(this, data); // Get label it will be jumping to in condition

        loopData.add(node.jjtGetChild(2)); // Delay incrementing until after the loop
        loopData.add(beginLabel);
        loopData.add(endLabel);

        return loopData;
    }

    public Object visit(ASTifStatement node, Object data)
    {
        SimpleNode condition = (SimpleNode) node.jjtGetChild(0);
        SimpleNode block = (SimpleNode) node.jjtGetChild(1);
        SimpleNode elseStatement = null;

        // Check if an else block exists
        if (node.jjtGetNumChildren() == 3) {
            elseStatement = (SimpleNode) node.jjtGetChild(2);
        }

        // When going into the condition, it will emit a conditional jump to the returned label
        String label = (String) condition.jjtAccept(this, data);
        String label2 = getNextLabel(); // If condition is true and there exist an else statement, jump to this label
        block.jjtAccept(this, label2); // Going in here will emit all the code in the body
        CodeGenerator.objectFile.println(label + ":"); // The label to jump to when the condition is false
        CodeGenerator.objectFile.flush();

        if (elseStatement != null) {
            elseStatement.jjtAccept(this, data);
        }

        CodeGenerator.objectFile.println(label2 + ":"); // Jump to this label if condition is true and there is an else
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTswitchStatement node, Object data)
    {
        SimpleNode switchVar = (SimpleNode) node.jjtGetChild(0);
        SimpleNode block = (SimpleNode) node.jjtGetChild(1);
        SimpleNode caseGroup = (SimpleNode) block.jjtGetChild(0);

        return data;
    }

    public Object visit(ASTequalEqual node, Object data)
    {
        String label = getNextLabel();
        emitComparisonCode(node, data);
        CodeGenerator.objectFile.println("    ifne " + label);
        CodeGenerator.objectFile.flush();

        return label;
    }

    public Object visit(ASTlessThan node, Object data)
    {
        String label = getNextLabel();
        emitComparisonCode(node, data);
        CodeGenerator.objectFile.println("    ifge " + label);
        CodeGenerator.objectFile.flush();

        return label;
    }

    public Object visit(ASTgreaterThan node, Object data)
    {
        String label = getNextLabel();
        emitComparisonCode(node, data);
        CodeGenerator.objectFile.println("    ifle " + label);
        CodeGenerator.objectFile.flush();

        return label;
    }

    public Object visit(ASTnotEqual node, Object data)
    {
        String label = getNextLabel();
        emitComparisonCode(node, data);
        CodeGenerator.objectFile.println("    ifeq " + label);
        CodeGenerator.objectFile.flush();

        return label;
    }

    public Object visit(ASTlessEqual node, Object data)
    {
        String label = getNextLabel();
        emitComparisonCode(node, data);
        CodeGenerator.objectFile.println("    ifgt " + label);
        CodeGenerator.objectFile.flush();

        return label;
    }

    public Object visit(ASTgreaterEqual node, Object data)
    {
        String label = getNextLabel();
        emitComparisonCode(node, data);
        CodeGenerator.objectFile.println("    iflt " + label);
        CodeGenerator.objectFile.flush();

        return label;
    }

    public Object visit(ASTincrement node, Object data)
    {
        SimpleNode incNode = (SimpleNode) node.jjtGetChild(0);
        TypeSpec type = node.getTypeSpec();

        SymTabEntry id = (SymTabEntry) incNode.getAttribute(ID);
        String fieldName = id.getName();

        String typePrefix = (type == Predefined.integerType) ? "i" : "f";
        String typePrefix2 = (type == Predefined.integerType) ? "I" : "F";

        CodeGenerator.objectFile.println("    ldc 1");
        CodeGenerator.objectFile.flush();

        incNode.jjtAccept(this, data);
        CodeGenerator.objectFile.println("    " + typePrefix + "add");
        CodeGenerator.objectFile.flush();

        CodeGenerator.objectFile.println("    " + "putstatic " + data + "/" + fieldName + " " + typePrefix2);
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTdecrement node, Object data)
    {
        SimpleNode decNode = (SimpleNode) node.jjtGetChild(0);
        TypeSpec type = node.getTypeSpec();

        String typePrefix = (type == Predefined.integerType) ? "i" : "f";
        String typePrefix2 = (type == Predefined.integerType) ? "I" : "F";

        SymTabEntry id = (SymTabEntry) decNode.getAttribute(ID);
        String fieldName = id.getName();

        CodeGenerator.objectFile.println("    ldc -1");
        CodeGenerator.objectFile.flush();

        decNode.jjtAccept(this, data);
        CodeGenerator.objectFile.println("    " + typePrefix + "add");
        CodeGenerator.objectFile.flush();

        CodeGenerator.objectFile.println("    " + "putstatic " + data + "/" + fieldName + " " + typePrefix2);
        CodeGenerator.objectFile.flush();

        return data;
    }


    public Object visit(ASTadd node, Object data)
    {
        SimpleNode addend0Node = (SimpleNode) node.jjtGetChild(0);
        SimpleNode addend1Node = (SimpleNode) node.jjtGetChild(1);

        TypeSpec type0 = addend0Node.getTypeSpec();
        TypeSpec type1 = addend1Node.getTypeSpec();

        // Get the addition type.
        TypeSpec type = node.getTypeSpec();
        String typePrefix = (type == Predefined.integerType) ? "i" : "f";

        if (type != Predefined.charType) {
            // Emit code for the first expression
            // with type conversion if necessary.
            addend0Node.jjtAccept(this, data);
            if ((type == Predefined.realType) &&
                    (type0 == Predefined.integerType))
            {
                CodeGenerator.objectFile.println("    i2f");
                CodeGenerator.objectFile.flush();
            }

            // Emit code for the second expression
            // with type conversion if necessary.
            addend1Node.jjtAccept(this, data);
            if ((type == Predefined.realType) &&
                    (type1 == Predefined.integerType))
            {
                CodeGenerator.objectFile.println("    i2f");
                CodeGenerator.objectFile.flush();
            }

            // Emit the appropriate add instruction.
            CodeGenerator.objectFile.println("    " + typePrefix + "add");
            CodeGenerator.objectFile.flush();
        }
        else {
            String lhsType = null;
            String rhsType = null;

            if (type0 == Predefined.integerType) {
                lhsType = "I";
            }
            else if (type0 == Predefined.realType) {
                lhsType = "F";
            }
            else if (type0 == Predefined.charType) {
                lhsType = "Ljava/lang/String;";
            }

            if (type1 == Predefined.integerType) {
                rhsType = "I";
            }
            else if (type1 == Predefined.realType) {
                rhsType = "F";
            }
            else if (type1 == Predefined.charType) {
                rhsType = "Ljava/lang/String;";
            }

            CodeGenerator.objectFile.println("    new       java/lang/StringBuilder");
            CodeGenerator.objectFile.println("    dup");
            CodeGenerator.objectFile.println("    invokenonvirtual java/lang/StringBuilder/<init>()V");
            addend0Node.jjtAccept(this, data);
            CodeGenerator.objectFile.println("    invokevirtual java/lang/StringBuilder/append("
                    + lhsType + ")Ljava/lang/StringBuilder;");
            addend1Node.jjtAccept(this, data);
            CodeGenerator.objectFile.println("    invokevirtual java/lang/StringBuilder/append("
                    + rhsType + ")Ljava/lang/StringBuilder;");
            CodeGenerator.objectFile.println("    invokevirtual java/lang/StringBuilder/toString()Ljava/lang/String;");
            CodeGenerator.objectFile.flush();
        }

        return data;
    }

    public Object visit(ASTbitwiseAnd node, Object data)
    {
        SimpleNode addend0Node = (SimpleNode) node.jjtGetChild(0);
        SimpleNode addend1Node = (SimpleNode) node.jjtGetChild(1);

        TypeSpec type0 = addend0Node.getTypeSpec();
        TypeSpec type1 = addend1Node.getTypeSpec();

        addend0Node.jjtAccept(this, data);
        addend1Node.jjtAccept(this, data);

        // Can only use bitwise operations on integers. What do we do when they're not integers? hmm...
        if (type0 == Predefined.integerType && type1 == Predefined.integerType) {
            // Emit the appropriate and instruction.
            CodeGenerator.objectFile.println("    " + "iand");
            CodeGenerator.objectFile.flush();
        }

        return data;
    }

    public Object visit(ASTbitwiseOr node, Object data)
    {
        SimpleNode addend0Node = (SimpleNode) node.jjtGetChild(0);
        SimpleNode addend1Node = (SimpleNode) node.jjtGetChild(1);

        TypeSpec type0 = addend0Node.getTypeSpec();
        TypeSpec type1 = addend1Node.getTypeSpec();

        addend0Node.jjtAccept(this, data);
        addend1Node.jjtAccept(this, data);

        // Can only use bitwise operations on integers. What do we do when they're not integers? hmm...
        if (type0 == Predefined.integerType && type1 == Predefined.integerType) {
            // Emit the appropriate and instruction.
            CodeGenerator.objectFile.println("    " + "ior");
            CodeGenerator.objectFile.flush();
        }

        return data;
    }

    public Object visit(ASTxor node, Object data)
    {
        SimpleNode addend0Node = (SimpleNode) node.jjtGetChild(0);
        SimpleNode addend1Node = (SimpleNode) node.jjtGetChild(1);

        TypeSpec type0 = addend0Node.getTypeSpec();
        TypeSpec type1 = addend1Node.getTypeSpec();

        addend0Node.jjtAccept(this, data);
        addend1Node.jjtAccept(this, data);

        // Can only use bitwise operations on integers. What do we do when they're not integers? hmm...
        if (type0 == Predefined.integerType && type1 == Predefined.integerType) {
            // Emit the appropriate and instruction.
            CodeGenerator.objectFile.println("    " + "ixor");
            CodeGenerator.objectFile.flush();
        }

        return data;
    }

    public Object visit(ASTsubtract node, Object data)
    {
        SimpleNode addend0Node = (SimpleNode) node.jjtGetChild(0);
        SimpleNode addend1Node = (SimpleNode) node.jjtGetChild(1);

        TypeSpec type0 = addend0Node.getTypeSpec();
        TypeSpec type1 = addend1Node.getTypeSpec();

        // Get the addition type.
        TypeSpec type = node.getTypeSpec();
        String typePrefix = (type == Predefined.integerType) ? "i" : "f";

        // Emit code for the first expression
        // with type conversion if necessary.
        addend0Node.jjtAccept(this, data);
        if ((type == Predefined.realType) &&
                (type0 == Predefined.integerType))
        {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        // Emit code for the second expression
        // with type conversion if necessary.
        addend1Node.jjtAccept(this, data);
        if ((type == Predefined.realType) &&
                (type1 == Predefined.integerType))
        {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        // Emit the appropriate add instruction.
        CodeGenerator.objectFile.println("    " + typePrefix + "sub");
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTmultiply node, Object data)
    {
        SimpleNode addend0Node = (SimpleNode) node.jjtGetChild(0);
        SimpleNode addend1Node = (SimpleNode) node.jjtGetChild(1);

        TypeSpec type0 = addend0Node.getTypeSpec();
        TypeSpec type1 = addend1Node.getTypeSpec();

        // Get the addition type.
        TypeSpec type = node.getTypeSpec();
        String typePrefix = (type == Predefined.integerType) ? "i" : "f";

        // Emit code for the first expression
        // with type conversion if necessary.
        addend0Node.jjtAccept(this, data);
        if ((type == Predefined.realType) &&
                (type0 == Predefined.integerType))
        {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        // Emit code for the second expression
        // with type conversion if necessary.
        addend1Node.jjtAccept(this, data);
        if ((type == Predefined.realType) &&
                (type1 == Predefined.integerType))
        {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        // Emit the appropriate add instruction.
        CodeGenerator.objectFile.println("    " + typePrefix + "mul");
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTdivide node, Object data)
    {
        SimpleNode addend0Node = (SimpleNode) node.jjtGetChild(0);
        SimpleNode addend1Node = (SimpleNode) node.jjtGetChild(1);

        TypeSpec type0 = addend0Node.getTypeSpec();
        TypeSpec type1 = addend1Node.getTypeSpec();

        // Get the addition type.
        TypeSpec type = node.getTypeSpec();
        String typePrefix = (type == Predefined.integerType) ? "i" : "f";

        // Emit code for the first expression
        // with type conversion if necessary.
        addend0Node.jjtAccept(this, data);
        if ((type == Predefined.realType) &&
                (type0 == Predefined.integerType))
        {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        // Emit code for the second expression
        // with type conversion if necessary.
        addend1Node.jjtAccept(this, data);
        if ((type == Predefined.realType) &&
                (type1 == Predefined.integerType))
        {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        // Emit the appropriate add instruction.
        CodeGenerator.objectFile.println("    " + typePrefix + "div");
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTmodulo node, Object data)
    {
        SimpleNode addend0Node = (SimpleNode) node.jjtGetChild(0);
        SimpleNode addend1Node = (SimpleNode) node.jjtGetChild(1);

        TypeSpec type0 = addend0Node.getTypeSpec();
        TypeSpec type1 = addend1Node.getTypeSpec();

        // Get the addition type.
        TypeSpec type = node.getTypeSpec();
        String typePrefix = (type == Predefined.integerType) ? "i" : "f";

        // Emit code for the first expression
        // with type conversion if necessary.
        addend0Node.jjtAccept(this, data);
        if ((type == Predefined.realType) &&
                (type0 == Predefined.integerType))
        {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        // Emit code for the second expression
        // with type conversion if necessary.
        addend1Node.jjtAccept(this, data);
        if ((type == Predefined.realType) &&
                (type1 == Predefined.integerType))
        {
            CodeGenerator.objectFile.println("    i2f");
            CodeGenerator.objectFile.flush();
        }

        // Emit the appropriate add instruction.
        CodeGenerator.objectFile.println("    " + typePrefix + "rem");
        CodeGenerator.objectFile.flush();

        return data;
    }
}
