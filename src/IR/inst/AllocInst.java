package IR.inst;

import IR.IrBlock;
import IR.llvmType.LlvmIntegerType;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;
import IR.operand.Operand;

public class AllocInst extends Inst{
    public LlvmSingleValueType type;
    public int alignSize;

    public AllocInst(LlvmSingleValueType type,Operand destReg, IrBlock irBlock) {
        super(destReg, irBlock);
        //this.type = type;

        destReg.isAlloc = true;

        this.type = type;
        if (this.type instanceof LlvmPointerType){
            alignSize = 8;
        }else {
            if (((LlvmIntegerType)this.type).isBool)alignSize = 1;
            else alignSize = 4;
        }
    }

    @Override
    public String toString() {
        return destReg.toString()+ " = alloca " + type.toString() + ", align " + alignSize;
    }
}
