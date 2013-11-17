package wci.intermediate;

import wci.frontend.*;
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
        }
        
        node.setTypeSpec(type);
    }
    

    public Object visit(ASTblock node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }

    public Object visit(ASTcaseGroup node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }


    public Object visit(ASTfunctionDeclaration node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }
    
    
    
    public Object visit(ASTifStatement node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }
    
    public Object visit(ASTparameter node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTparameterList node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTstart node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }
    
    public Object visit(ASTstatement node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
            }
    
    
    
    public Object visit(ASTswitchBlock node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
            }
    
    
    
   public Object visit(ASTswitchStatement node, Object data)
    {

        Object obj = super.visit(node, data);
        setType(node);
        return obj;
            }
    
   

    
}
