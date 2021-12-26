package IR.operand;

import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;
import IR.llvmType.LlvmStructType;

public class ThisOperand extends Operand{
    //boolean isThis;
    LlvmStructType nowStruct;
    String memberName;

    public ThisOperand(String memberName, LlvmStructType nowStruct) {
        super(new LlvmPointerType(nowStruct));
       // isThis = true;
        this.memberName = memberName;
        this.nowStruct = nowStruct;
    }

    @Override
    public String toString() {
        return null;
    }
}
