package IR.operand;

import IR.llvmType.LlvmIntegerType;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;

public class NullPointerConst extends Const{
    public String value;
    public NullPointerConst() {
        super(new LlvmPointerType(new LlvmIntegerType(8,false)));
        value = "null";
    }

    @Override
    public String toString() {
        return value;
    }
}
