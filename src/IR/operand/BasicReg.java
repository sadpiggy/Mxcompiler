package IR.operand;

import IR.llvmType.LlvmSingleValueType;

public abstract class BasicReg extends Operand{
    private Operand valueReg;

    public BasicReg(LlvmSingleValueType type, String typeName,Operand valueReg) {
        super(type, typeName);
        this.valueReg = valueReg;
    }

    @Override
    public String toString() {
        return null;
    }
}
