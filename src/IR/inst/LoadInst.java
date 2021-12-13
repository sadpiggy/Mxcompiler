package IR.inst;

import IR.IrBlock;
import IR.llvmType.LlvmIntegerType;
import IR.llvmType.LlvmPointerType;
import IR.operand.Operand;

public class LoadInst extends Inst{
    public Operand pointer;
    public int alignSize;

    public LoadInst(Operand destReg, IrBlock irBlock,Operand pointer) {
        super(destReg, irBlock);
        this.pointer = pointer;
        if (this.destReg.type instanceof LlvmPointerType){
            alignSize = 8;
        }else {
            if (((LlvmIntegerType)this.destReg.type).isBool)alignSize = 1;
            else alignSize = 4;
        }
    }

    @Override
    public String toString() {
        return destReg.toString() + " = load " + destReg.type.toString() + ", " + pointer.type.toString() + " " + pointer.toString() + ", align " + alignSize;
    }
}
