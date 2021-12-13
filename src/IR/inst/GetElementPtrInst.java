package IR.inst;

import IR.IrBlock;
import IR.llvmType.LlvmPointerType;
import IR.operand.Operand;

import java.util.ArrayList;

public class GetElementPtrInst extends Inst{
    public Operand pointer;
    public ArrayList<Operand> indexes;

    public GetElementPtrInst(Operand destReg, IrBlock irBlock,Operand pointer,ArrayList<Operand> indexes) {
        super(destReg, irBlock);
        this.pointer = pointer;
        this.indexes = indexes;
    }

    @Override
    public String toString() {//这里可能错     .a i32,i32 // [2] i64 2
        StringBuilder ret = new StringBuilder(destReg.toString() + " = getelementptr " + ((LlvmPointerType) pointer.type).pointeeType.toString() + ", " + pointer.type.toString() + " " + pointer.toString());
        for (var index: indexes) ret.append(", ").append(index.type.toString()).append(" ").append(index.toString());
        return ret.toString();
    }
}
