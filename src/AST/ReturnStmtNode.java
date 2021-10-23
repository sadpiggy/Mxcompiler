package AST;

import Mutil.Position;

public class ReturnStmtNode extends StmtNode{
    private ExprNode exprNode;

    public ReturnStmtNode(Position pos_,ExprNode exprNode_) {
        super(pos_);
        exprNode = exprNode_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public boolean hasExprNode(){
        return exprNode!=null;
    }

    public ExprNode getExprNode() {
        return exprNode;
    }

    @Override
    public String toString() {
        return "<ReturnStmtNode>\n" +
                "exprNode: " +
                (exprNode==null?"\n":exprNode.toString());
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
