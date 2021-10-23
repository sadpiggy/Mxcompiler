package AST;

import Mutil.Position;

public class SubScriptExprNode extends ExprNode{
    private ExprNode exprNode;
    private ExprNode indexNode;
    int num;

    public SubScriptExprNode(Position pos_, String text_,ExprNode exprNode_,ExprNode indexNode_) {
        super(pos_, text_);
        exprNode = exprNode_;
        indexNode = indexNode_;
        if(exprNode instanceof SubScriptExprNode){
            num = ((SubScriptExprNode)exprNode).num + 1;
        }else {
            num = 1;
        }
    }

    public ExprNode getExprNode() {
        return exprNode;
    }

    public ExprNode getIndexNode() {
        return indexNode;
    }

    public int getNum() {
        return num;
    }

    @Override
    public boolean isAssignAble() {
        return true;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public String toString() {
        return "<SubscriptExprNode>\n"+"exprNode: " + exprNode.toString() + "indexNode: " + indexNode.toString()
                + "num: " + num;
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
