package AST;

import Mutil.Position;
import Mutil.type.Type;

import java.util.ArrayList;

//[&](int a,int b)->{la;}(1,3)
public class LambdaExprNode extends ExprNode{//需要显示表达typeNode吗？
    private ArrayList<VarDefNode> FormalParas;
    private BlockStmtNode suit;
    private ArrayList<ExprNode> ActualParas;

    public LambdaExprNode(Position pos_, String text_,ArrayList<VarDefNode>FormalParas_,BlockStmtNode suit_,ArrayList<ExprNode>ActualParas_) {
        super(pos_, text_);
        FormalParas = FormalParas_;
        suit = suit_;
        ActualParas = ActualParas_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public Type getType() {
        return super.getType();
    }

    public ArrayList<VarDefNode> getFormalParas() {
        return FormalParas;
    }

    public BlockStmtNode getSuit() {
        return suit;
    }

    public ArrayList<ExprNode> getActualParas() {
        return ActualParas;
    }

    @Override
    public String toString() {//lambda表达式允许suit为空吗？？？
        StringBuilder stringBuilder = new StringBuilder("<LambdaNode>\nFormalParams: ");
        for(var it : FormalParas){
            stringBuilder.append(it.toString());
        }
        stringBuilder.append("suit: ");
        stringBuilder.append(suit.toString());
        stringBuilder.append("ActualParas: ");
        for(var it : ActualParas){
            stringBuilder.append(it.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
