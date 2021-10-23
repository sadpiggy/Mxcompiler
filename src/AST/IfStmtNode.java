package AST;

import Mutil.Position;

import java.util.LinkedList;

public class IfStmtNode extends StmtNode{
    public ExprNode conditionExpr;
    public StmtNode trueStmt;
    public StmtNode falseStmt;

    public IfStmtNode(Position pos_,ExprNode conditionExpr_,StmtNode trueStmt_,StmtNode falseStmt_) {
        super(pos_);
        conditionExpr = conditionExpr_;

       if (trueStmt_ instanceof BlockStmtNode)trueStmt = trueStmt_;
       else trueStmt = new BlockStmtNode(pos_,trueStmt_);

       if (falseStmt_==null)falseStmt = null;
       else {
           if (falseStmt_ instanceof BlockStmtNode) falseStmt = falseStmt_;
           else falseStmt = new BlockStmtNode(pos_, falseStmt_);
       }
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public ExprNode getConditionExpr() {
        return conditionExpr;
    }

    public boolean hasTrueStmt(){//这个应该是一定有的,for(;;);看似没有，其实最右边那个‘;’代表一个emptyStmt
        return trueStmt!=null;
    }

    public StmtNode getTrueStmt() {
        return trueStmt;
    }

    public boolean hasFalseStmt(){
        return falseStmt!=null;
    }

    public StmtNode getFalseStmt() {
        return falseStmt;
    }

    @Override
    public String toString() {
        return "<IfStmtNode>\ncondition: " + conditionExpr.toString() + "trueStmt: " + trueStmt.toString()
                +"falseStmt: "
                + (falseStmt==null?"\n": falseStmt.toString());
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
