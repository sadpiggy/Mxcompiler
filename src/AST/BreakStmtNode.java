package AST;

import Mutil.Position;

public class BreakStmtNode extends StmtNode{
    public BreakStmtNode(Position pos_) {
        super(pos_);
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public String toString() {
        return "<BreakStmtNode>\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
