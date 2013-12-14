package wci.backend.compiler;

import wci.frontend.*;
import wci.intermediate.*;
import wci.intermediate.symtabimpl.Predefined;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.VALUE;

public class MethodBodyGeneratorVisitor extends GoParserVisitorAdapter
{
    private String functionName;
    private int tagNumber = 0;

    public MethodBodyGeneratorVisitor(String functionName) {
        this.functionName = functionName;
    }

    public String getCurrentLabel() { return "methodlabel" + tagNumber; }
    public String getNextLabel() { return "methodlabel" + ++tagNumber; }

    public Object visit(ASTfunctionDeclaration node, Object data) {
        return data;
    }

    public Object visit(ASTidentifier node, Object data)
    {
        SymTabEntry id = (SymTabEntry) node.getAttribute(ID);
        String fieldName = id.getName();
        TypeSpec type = id.getTypeSpec();
        String typeCode = null;

        if (type == Predefined.integerType) {
            typeCode = "i";
        }
        else if (type == Predefined.realType) {
            typeCode = "f";
        }
        else if (type == Predefined.charType) {
            typeCode = "Ljava/lang/String;"; // TODO: How to load a local variable string?
        }

        // Emit the appropriate load instruction.
        CodeGenerator.objectFile.println("    " + typeCode + "load " + id.getIndex());
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
}

