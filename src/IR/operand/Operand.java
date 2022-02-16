package IR.operand;

import IR.llvmType.LlvmSingleValueType;
import IR.llvmType.LlvmType;

public abstract class Operand {
    public LlvmSingleValueType type;
    public String typeName;
    public boolean isAlloc = false;
    public Operand(LlvmSingleValueType type,String typeName){
        this.type = type;//isThis = false;memberName = null;
        this.typeName = typeName;
    }

    @Override abstract public String toString();
}
