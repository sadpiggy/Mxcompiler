package AST;

import Mutil.Position;

public class ForStmtNode extends StmtNode{
    private ExprNode initExpr;
    private ExprNode conditionExpr;
    private ExprNode changeExpr;
    private StmtNode stmt;

    public ForStmtNode(Position pos_,ExprNode initExpr_,ExprNode conditionExpr_,ExprNode changeExpr_,StmtNode stmt_) {
        super(pos_);
        initExpr = initExpr_;
        conditionExpr = conditionExpr_;
        changeExpr = changeExpr_;
        //stmt = stmt_;
        if (stmt_ instanceof BlockStmtNode)stmt = stmt_;
        else stmt = new BlockStmtNode(pos_,stmt_);
    }

    public boolean hasInitExpr(){
        return initExpr != null;
    }

    public ExprNode getInitExpr() {
        return initExpr;
    }

    public void setInitExpr(ExprNode initExpr_){
        initExpr = initExpr_;
    }

    public boolean hasConditionExpr(){
        return conditionExpr != null;
    }

    public ExprNode getConditionExpr() {
        return conditionExpr;
    }

    public void setConditionExpr(ExprNode conditionExpr_){
        conditionExpr = conditionExpr_;
    }

    public boolean hasChangeExpr(){
        return changeExpr != null;
    }

    public ExprNode getChangeExpr() {
        return changeExpr;
    }

    public void setChangeExpr(ExprNode changeExpr_){
        changeExpr = changeExpr_;
    }

    public StmtNode getStmt(){
        return stmt;
    }

    @Override
    public String toString() {
        return "<ForStmtNode>\n" +
                "initExpr: " + (initExpr==null?"\n":initExpr.toString()) +
                "conditionExpr: "+ (conditionExpr==null?"\n":conditionExpr.toString()) +
                "changeExpr: " + (changeExpr==null?"":changeExpr.toString()) +
                "stmt: " + stmt.toString();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
