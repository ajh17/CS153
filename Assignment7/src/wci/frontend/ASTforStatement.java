/* Generated By:JJTree: Do not edit this line. ASTforStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=wci.intermediate.icodeimpl.ICodeNodeImpl,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package wci.frontend;

public
class ASTforStatement extends SimpleNode {
  public ASTforStatement(int id) {
    super(id);
  }

  public ASTforStatement(GoParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GoParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=eab82057c65b7ad23862ead20d36e293 (do not edit this line) */
