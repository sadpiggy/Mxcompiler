package AST;

import Mutil.Position;

public class WhileStmtNode extends  StmtNode{
    private ExprNode conditionExpr;
    private StmtNode stmt;

    public WhileStmtNode(Position pos_,ExprNode conditionExpr_,StmtNode stmt_) {
        super(pos_);
        conditionExpr = conditionExpr_;
        if (stmt_ instanceof BlockStmtNode)stmt = stmt_;
        else stmt =new BlockStmtNode(pos_,stmt_);
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public ExprNode getConditionExpr() {
        return conditionExpr;
    }

    public StmtNode getStmt() {
        return stmt;
    }

    @Override
    public String toString() {
        return "<WhileStmtNode>\n" +
                "conditionExpr: " + conditionExpr.toString() +
                "stmt: " + stmt.toString() ;
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
