package IR.operand;

import IR.llvmType.LlvmSingleValueType;

public abstract class Const extends Operand{
    public Const(LlvmSingleValueType type) {
        super(type,null);
    }

    @Override public abstract String toString();
}
