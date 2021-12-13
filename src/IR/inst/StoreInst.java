package IR.inst;

import IR.IrBlock;
import IR.llvmType.LlvmIntegerType;
import IR.llvmType.LlvmPointerType;
import IR.operand.Operand;

public class StoreInst extends Inst{//之后可能还要添加user def链
    public Operand value;
    public Operand pointer;
    public int alignSize;

    public StoreInst(IrBlock irBlock,Operand value,Operand pointer) {
        super(irBlock);
        this.value = value;
        this.pointer = pointer;
        if (this.value.type instanceof LlvmPointerType){
            alignSize = 8;
        }else {
            if (((LlvmIntegerType)this.value.type).isBool)alignSize = 1;
            else alignSize = 4;
        }
    }

    @Override
    public String toString() {
        return "store " + value.type.toString() + " " + value.toString() + ", " + pointer.type.toString() + " " + pointer.toString()+ ", align " + alignSize;
    }
}
