package IR.inst;

import IR.IrBlock;
import IR.operand.Operand;

public class BinaryInst extends Inst{
    public static enum InstOp{
        add, sub, mul, sdiv, srem, shl, ashr, and, or, xor
    }

    public InstOp instOp;
    public Operand value1;
    public Operand value2;

    public BinaryInst(Operand destReg, IrBlock irBlock,InstOp instOp,Operand value1,Operand value2) {
        super(destReg, irBlock);
        this.instOp = instOp;
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public String toString() {
        return destReg.toString() + " = " + instOp.toString() + " " + value1.type.toString() + " " + value1.toString() + ", " + value2.toString();
    }
}
