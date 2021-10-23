package AST;

import Mutil.Position;

import java.util.ArrayList;

public class FuncCallExprNode extends ExprNode{
    private String identifier;
    private ArrayList<ExprNode>params;

    public FuncCallExprNode(Position pos_, String text_,String identifier_,ArrayList<ExprNode>params_) {
        super(pos_, text_);
        identifier = identifier_;
        params = params_;
    }

    public String getIdentifier(){
        return identifier;
    }

    public ArrayList<ExprNode> getParams() {
        return params;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("<FuncCallExprNode>\n"+"identifier: ");
        stringBuilder.append( identifier + "\nparams: ");
        for(ExprNode it: params) stringBuilder.append(it.toString());
        return stringBuilder.toString();
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
