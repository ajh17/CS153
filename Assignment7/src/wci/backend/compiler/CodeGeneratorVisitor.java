package wci.backend.compiler;

import wci.frontend.*;
import wci.intermediate.*;
import wci.intermediate.symtabimpl.Predefined;

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
        String typeCode = type == Predefined.integerType ? "I" : "F";

        // Emit the appropriate store instruction.
        CodeGenerator.objectFile.println("    putstatic " + programName +
                "/" + fieldName + " " + typeCode);
        CodeGenerator.objectFile.flush();

        return data;
    }

    public Object visit(ASTidentifier node, Object data)
    {
        String programName = (String) data;
        SymTabEntry id = (SymTabEntry) node.getAttribute(ID);
        String fieldName = id.getName();
        TypeSpec type = id.getTypeSpec();
        String typeCode = type == Predefined.integerType ? "I" : "F";

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
        String typeCode;
        printNode.jjtAccept(this, data);

        if (type == Predefined.integerType) {
            typeCode = "I";
        }
        else if (type == Predefined.realType) {
            typeCode = "F";
        }
        else {
            typeCode = "Ljava/lang/String;";
        }

        CodeGenerator.objectFile.println("    invokevirtual java/io/PrintStream/println(" + typeCode + ")V");
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

        return data;
    }

    public Object visit(ASTsubtract node, Object data)
    {
        SimpleNode addend0Node = (SimpleNode) node.jjtGetChild(0);
        SimpleNode addend1Node = (SimpleNode) node.jjtGetChild(1);

        TypeSpec type0 = addend0Node.getTypeSpec();
        TypeSpec type1 = addend1Node.getTypeSpec();

        // Get the addition type.
        TypeSpec type = (type0 == Predefined.integerType && type1 == Predefined.integerType) ?
                                    Predefined.integerType : Predefined.realType;
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

    public Object visit(ASTblock node, Object data)
    {
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
            CodeGenerator.objectFile.println(beginLabel + ":");
            CodeGenerator.objectFile.flush();
            String endLabel = (String) forClause.jjtAccept(this, data);
            block.jjtAccept(this, data);
            CodeGenerator.objectFile.println("    goto " + beginLabel);
            CodeGenerator.objectFile.println(endLabel + ":");
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
            return node.jjtGetChild(0).jjtAccept(this, data);
        }

        node.jjtGetChild(0).jjtAccept(this, data);

        String beginLabel = getNextLabel();
        CodeGenerator.objectFile.println(beginLabel + ":");
        CodeGenerator.objectFile.flush();
        String endLabel = (String) node.jjtGetChild(1).jjtAccept(this, data);

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

        if (node.jjtGetNumChildren() == 3) {
            elseStatement = (SimpleNode) node.jjtGetChild(2);
        }

        String label = (String) condition.jjtAccept(this, data);
        String label2 = getNextLabel();
        block.jjtAccept(this, label2);
        CodeGenerator.objectFile.println(label + ":");
        CodeGenerator.objectFile.flush();

        if (elseStatement != null) {
            elseStatement.jjtAccept(this, data);
        }

        CodeGenerator.objectFile.println(label2 + ":");
        CodeGenerator.objectFile.flush();

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
}
