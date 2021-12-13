package IR.operand;

import IR.llvmType.LlvmIntegerType;
import IR.llvmType.LlvmSingleValueType;

public class IntegerConst extends Const{
    public int value;
    public boolean isBool;

    public IntegerConst(int width,boolean isBool,int value) {
        super(new LlvmIntegerType(width,isBool));
        this.value = value;
        this.isBool = isBool;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
