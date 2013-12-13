package wci.backend.compiler;

import wci.backend.compiler.CodeGenerator;
import wci.frontend.*;
import wci.intermediate.*;
import wci.intermediate.symtabimpl.*;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.ID;

public class MethodGeneratorVisitor extends GoParserVisitorAdapter
{
    public Object visit(ASTfunctionDeclaration node, Object data) {
        SymTabEntry functionId = (SymTabEntry) node.getAttribute(ID);
        SymTabImpl scope = (SymTabImpl) functionId.getAttribute(SymTabKeyImpl.ROUTINE_SYMTAB);
        StringBuilder typeBuffer = new StringBuilder(); // Used to store list of parameter types
        StringBuilder initBuffer = new StringBuilder(); // Used to store local variable intialization code
        Character returnType = ' '; // TODO: Need to implement. Blank for now

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
                + functionId.getName() + "(" + typeBuffer.toString() + ")" + returnType);
        CodeGenerator.objectFile.println(initBuffer.toString());

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
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
