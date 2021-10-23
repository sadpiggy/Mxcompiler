package AST;

import Mutil.Position;

import java.util.LinkedList;

public class BlockStmtNode extends StmtNode{
    private LinkedList<StmtNode>stmtNodes;

    public BlockStmtNode(Position pos_,LinkedList<StmtNode>stmtNodes_) {
        super(pos_);
        stmtNodes = stmtNodes_;
    }

    public BlockStmtNode(Position pos_,StmtNode stmtNode_){
        super(pos_);
        stmtNodes = new LinkedList<>();
        if(stmtNode_!=null)stmtNodes.add(stmtNode_);
    }

    public void addStmtNode(StmtNode stmtNode_){
        if (stmtNode_ != null)stmtNodes.add(stmtNode_);
    }

    public void addStmtNodeAtHead(StmtNode stmtNode_){
        if (stmtNode_!=null)stmtNodes.addFirst(stmtNode_);
    }

    public void addStmtNodes(LinkedList<StmtNode>stmtNodes_){
        if (stmtNodes_ != null)stmtNodes.addAll(stmtNodes_);
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public LinkedList<StmtNode> getStmtNodes() {
        return stmtNodes;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("<BlockStmtNode>\nstatements: ");
        for (StmtNode it : stmtNodes) stringBuilder.append(it.toString());
        return stringBuilder.toString();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
