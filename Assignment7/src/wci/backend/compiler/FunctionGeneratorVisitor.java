package wci.backend.compiler;

import wci.frontend.*;
import wci.intermediate.*;
import wci.intermediate.symtabimpl.*;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.ID;

public class FunctionGeneratorVisitor extends GoParserVisitorAdapter
{
    public Object visit(ASTfunctionDeclaration node, Object data) {
        SymTabEntry functionId = (SymTabEntry) node.getAttribute(ID);
        SymTabImpl scope = (SymTabImpl) functionId.getAttribute(SymTabKeyImpl.ROUTINE_SYMTAB);
        StringBuilder typeBuffer = new StringBuilder(); // Used to store list of parameter types
        StringBuilder initBuffer = new StringBuilder(); // Used to store local variable intialization code
        Character returnType = 'V'; // TODO: Need to implement dynamic checking of return type

        for (SymTabEntry entry : scope.values()) {
            TypeSpec type = entry.getTypeSpec();
            initBuffer.append("    .var " + entry.getIndex() + " is " + entry.getName() + " ");

            if (type == Predefined.integerType) {
                typeBuffer.append("I");
                initBuffer.append("I\n");
            }
            else if (type == Predefined.realType) {
                typeBuffer.append("F");
                initBuffer.append("F\n");
            }
            else if (type == Predefined.charType) {
                typeBuffer.append("Ljava/lang/String;");
                initBuffer.append("Ljava/lang/String;\n");
            }
            else if (type == Predefined.booleanType) {
                typeBuffer.append("Z");
                initBuffer.append("Z\n");
            }
        }

        CodeGenerator.objectFile.println(".method private static "
                + functionId.getName() + "(" + typeBuffer.toString() + ")" + returnType + "\n");
        CodeGenerator.objectFile.println(initBuffer.toString());

        GoParserVisitor codeGenerator = new CodeGeneratorVisitor(functionId.getName());
        for (int i = 1; i < node.jjtGetNumChildren(); i++) { // Start at 1 to skip the function identifier
            node.jjtGetChild(i).jjtAccept(codeGenerator, data);
        }

        CodeGenerator.objectFile.println();
        CodeGenerator.objectFile.println("    return");
        CodeGenerator.objectFile.println();
        CodeGenerator.objectFile.println(".limit locals " + scope.size());
        CodeGenerator.objectFile.println(".limit stack  " + 16);
        CodeGenerator.objectFile.println(".end method");
        CodeGenerator.objectFile.flush();

        return data;
    }
}
