package AST;

import Mutil.Position;

import java.util.ArrayList;

public class NewExprNode extends ExprNode{
    private NameTypeNode nameTypeNode;
    private ArrayList<ExprNode> ExprInBracket;
    private int num;

    public NewExprNode(Position pos_, String text_,NameTypeNode nameTypeNode_,ArrayList<ExprNode>ExprInBracket_,int num_) {
        super(pos_, text_);
        nameTypeNode = nameTypeNode_;
        ExprInBracket = ExprInBracket_;
        num = num_;//num是括号的数量
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public int getNum() {
        return num;
    }

    public NameTypeNode getNameTypeNode() {
        return nameTypeNode;
    }

    public ArrayList<ExprNode> getExprInBracket() {
        return ExprInBracket;
    }



    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
