package IR.operand;

import IR.llvmType.LlvmIntegerType;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;

public class NullPointerConst extends Const{
    public String value;
    public NullPointerConst(LlvmSingleValueType type) {
        //super(new LlvmPointerType(new LlvmIntegerType(8,false)));
        super(type);
        value = "null";
    }

    @Override
    public String toString() {
        return value;
    }
}
