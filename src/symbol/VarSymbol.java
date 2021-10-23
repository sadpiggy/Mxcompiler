package symbol;

import AST.AstNode;
import Mutil.type.Type;

public class VarSymbol extends Symbol{
    public VarSymbol(String name_, Type type_, AstNode define_) {
        super(name_, type_, define_);
    }
}
