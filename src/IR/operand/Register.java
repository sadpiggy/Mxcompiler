package IR.operand;

import ASM.AsmOperand.PhysicalReg;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;

public class Register extends BasicReg{
    public String name;
    //public boolean isAddress;

    public Register(LlvmSingleValueType type,String name,String typeName,Operand valueReg) {
        super(type,typeName,valueReg);
        this.name = name;
        //this.isAddress = isAddress;
    }

    public String getTypeName(){
        return typeName;
    }



    public int getAlignSize(){
        return type.getAlignSize();
    }

    @Override
    public String toString() {
         return "%" + name;
    }
}
