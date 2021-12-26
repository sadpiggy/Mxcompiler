package IR.operand;

import IR.llvmType.LlvmSingleValueType;
import IR.llvmType.LlvmType;

public abstract class Operand {
    public LlvmSingleValueType type;
    public String typeName;
    //public boolean isThis;
    //public String memberName;
    //public String name;//name可能后面有用
    public Operand(LlvmSingleValueType type){
        this.type = type;typeName=null;//isThis = false;memberName = null;
    }

    @Override abstract public String toString();
}
