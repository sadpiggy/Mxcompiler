package IR.inst;

import IR.IrBlock;
import IR.operand.Operand;

public class IcmpInst extends Inst{
    public static enum CondOp{
        eq,ne,sgt,sge,slt,sle
    }
    public CondOp condOp;
    public Operand value1;
    public Operand value2;

    public IcmpInst(Operand destReg, IrBlock irBlock,CondOp condOp,Operand value1,Operand value2) {
        super(destReg, irBlock);
        this.condOp = condOp;
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public String toString() {
        return destReg.toString() + " = icmp " + condOp.toString() + " " + value1.type.toString() + " " + value1.toString() + ", " + value2.toString();
    }
}
