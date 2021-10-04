package Mutil.error;

import Mutil.Position;

public class SyntaxError extends Error{
    public SyntaxError(String message_, Position position_) {
        super("SyntaxError" + message_, position_);
    }
}
