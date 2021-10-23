package AST;

import Mutil.Position;

public class MemberExprNode extends ExprNode{
    private ExprNode objExpr;
    private String memberName;

    public MemberExprNode(Position pos_, String text_,ExprNode objExpr_,String memberName_) {
        super(pos_, text_);
        objExpr = objExpr_;
        memberName = memberName_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public String getMemberName() {
        return memberName;
    }

    public ExprNode getObjExpr() {
        return objExpr;
    }

    public boolean isAssignAble(){
        return true;
    }

    @Override
    public String toString() {
        return "<MemberExprNode>\nobjExpr: " + objExpr.toString() + "\nmemberName: " + memberName + "\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
