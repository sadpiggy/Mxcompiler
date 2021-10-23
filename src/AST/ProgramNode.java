package AST;

import Mutil.Position;

import java.util.ArrayList;

public class ProgramNode extends AstNode{
    private ArrayList<ProgramSectionNode> ProgramSectionNodeList;

    public ProgramNode(Position pos_,ArrayList<ProgramSectionNode>ProgramSectionNodeList_) {
        super(pos_);
        ProgramSectionNodeList = ProgramSectionNodeList_;
    }

    public ArrayList<ProgramSectionNode> getProgramSectionNodeList() {
        return ProgramSectionNodeList;
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("<ProgramNode>\n"+"programSectionNode:\n");
        for(var it: ProgramSectionNodeList) stringBuilder.append(it.toString());
        return stringBuilder.toString();
    }
}
