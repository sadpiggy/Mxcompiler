package AST;

import Mutil.Position;

import java.util.ArrayList;

public class VarDefListNode extends ProgramSectionNode{
    private ArrayList<VarDefNode>varDefNodes;

    public VarDefListNode(Position pos_,ArrayList<VarDefNode>varDefNodes_) {
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
        StringBuilder stringBuilder = new StringBuilder("<VarListNode>\nvarNodes:\n");
        for(var it: varDefNodes){
            stringBuilder.append(it.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
