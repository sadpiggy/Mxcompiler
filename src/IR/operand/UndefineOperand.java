package IR.operand;

import IR.llvmType.LlvmSingleValueType;

public class UndefineOperand extends Operand{
    public UndefineOperand(LlvmSingleValueType type) {
        super(type);
    }

    @Override
    public String toString() {
        return "undef";
    }
}
