package Mutil.error;

import Mutil.Position;

public class SemanticError extends Errormy{
    public SemanticError(String message_, Position position_) {
        super("SemanticError " + message_, position_);
    }
}
