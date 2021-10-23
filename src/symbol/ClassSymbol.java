package symbol;

import AST.AstNode;
import Mutil.type.Type;

public class ClassSymbol extends Symbol{
    public ClassSymbol(String name_, Type type_, AstNode define_) {
        super(name_, type_, define_);
    }
}
