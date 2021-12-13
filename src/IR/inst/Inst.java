package IR.inst;

import IR.IrBlock;
import IR.operand.Operand;
import IR.operand.Register;

public abstract class Inst {
    public Operand destReg;//目标寄存器
    public IrBlock irBlock;//包含这条指令的block
    public Inst(IrBlock irBlock){
        destReg = null;
        this.irBlock = irBlock;
    }

    public Inst(Operand destReg,IrBlock irBlock){
        if (!(destReg instanceof Register))System.out.println("error in inst.java");
        this.destReg = destReg;
        this.irBlock = irBlock;
    }

    @Override public abstract String toString();
}
