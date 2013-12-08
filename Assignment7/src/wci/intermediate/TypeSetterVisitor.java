package wci.intermediate;

import wci.frontend.*;
import wci.intermediate.*;
import wci.intermediate.symtabimpl.*;

public class TypeSetterVisitor extends GoParserVisitorAdapter
{
    private void setType(SimpleNode node)
    {
        int count = node.jjtGetNumChildren();
        TypeSpec type = Predefined.integerType;
        
        for (int i = 0; (i < count) && (type == Predefined.integerType); ++i) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);
            TypeSpec childType = child.getTypeSpec();
            
            if (childType == Predefined.realType) {
                type = Predefined.realType;
            }
            else if (childType == Predefined.charType) {
                type = Predefined.charType;
            }
        }
        
        node.setTypeSpec(type);
    }
    
    public Object visit(ASTassignmentStatement node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }

    public Object visit(ASTprintStatement node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }

    public Object visit(ASTadd node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }
    
    public Object visit(ASTidentifier node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTintegerConstant node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTrealConstant node, Object data)
    {
        return data;
    }
}
