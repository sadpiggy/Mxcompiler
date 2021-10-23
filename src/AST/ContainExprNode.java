package AST;

import Mutil.Position;

public abstract class ContainExprNode extends ExprNode {
    public ContainExprNode(Position pos_, String text_) {
        super(pos_, text_);
    }
}
