/* Generated By:JJTree: Do not edit this line. ASTstatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=wci.intermediate.icodeimpl.ICodeNodeImpl,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package wci.frontend;

public
class ASTstatement extends SimpleNode {
  public ASTstatement(int id) {
    super(id);
  }

  public ASTstatement(GoParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GoParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=44cf38df20c2de919d42c81e975f8e13 (do not edit this line) */
