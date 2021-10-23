package AST;

import Mutil.Position;

import java.util.ArrayList;

public class MethodExprNode extends ExprNode{
    private ExprNode objExpr;
    private String methodName;
    private ArrayList<ExprNode>params;

    public MethodExprNode(Position pos_, String text_,ExprNode objExpr_,String methodName_,ArrayList<ExprNode>params_) {
        super(pos_, text_);
        objExpr = objExpr_;
        methodName = methodName_;
        params = params_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public ExprNode getObjExpr() {
        return objExpr;
    }

    public String getMethodName() {
        return methodName;
    }

    public ArrayList<ExprNode> getParams() {
        return params;
    }

    @Override
    public String toString() {
        String string = "<MethodExprNode>\n" +
                "objExpr: " + objExpr +
                "\nmethodName: " + methodName + '\n' +
                "params: " ;
        StringBuilder stringBuilder = new StringBuilder(string);
        for(ExprNode it: params) stringBuilder.append(it.toString());
        return stringBuilder.toString();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
