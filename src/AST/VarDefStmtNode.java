package AST;

import Mutil.Position;

import java.util.ArrayList;

public class VarDefStmtNode extends StmtNode{
    private ArrayList<VarDefNode>varDefNodes;

    public VarDefStmtNode(Position pos_,ArrayList<VarDefNode>varDefNodes_) {
        super(pos_);
        varDefNodes = varDefNodes_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public ArrayList<VarDefNode> getVarDefNodes() {
        return varDefNodes;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("<VarDefStmtNode>\nvarDefNodes: ");
        for(VarDefNode it: varDefNodes) stringBuilder.append(it.toString());
        return stringBuilder.toString();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
