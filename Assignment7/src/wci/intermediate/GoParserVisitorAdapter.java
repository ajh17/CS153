package wci.intermediate;

import wci.frontend.ASTassignmentStatement;
import wci.frontend.ASTblock;
import wci.frontend.GoParserVisitor;
import wci.frontend.SimpleNode;

public class GoParserVisitorAdapter implements GoParserVisitor
{
    public Object visit(SimpleNode node, Object data) {
        return node.childrenAccept(this, data);
    }

    public Object visit(ASTblock node, Object data) {
        return node.childrenAccept(this, data);
    }
}
