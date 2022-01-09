package IR.operand;

import ASM.AsmOperand.PhysicalReg;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;

public class Register extends Operand{
    public String name;
//    public int offset;
//    public PhysicalReg physicalReg;
//    public boolean isPhysical;

    public Register(LlvmSingleValueType type,String name) {
       //super(new LlvmPointerType(type));
        super(type);
        this.name = name;
//        offset = 0;
//        physicalReg = null;
//        isPhysical = false;
    }

    public int getAlignSize(){
        return type.getAlignSize();
    }

    @Override
    public String toString() {
         return "%" + name;
    }
}
