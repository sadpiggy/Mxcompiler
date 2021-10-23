package AST;

import Mutil.Position;

public class ContinueStmtNode extends StmtNode{
    public ContinueStmtNode(Position pos_) {
        super(pos_);
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public String toString() {
        return "<ContinueStmtNode>\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
     astVisitor.visit(this);
    }
}
