package IR.inst;

import IR.IrBlock;
import IR.operand.Operand;
import IR.operand.Register;

public abstract class Inst {
    //public boolean isInLoop;
    public Operand destReg;//目标寄存器
    public IrBlock irBlock;//包含这条指令的block
    public boolean isLoopBegin = false;
    public boolean isLoopEnd = false;
    public boolean notChange = false;
    public Inst(IrBlock irBlock){
        destReg = null;
        this.irBlock = irBlock;
        //this.isInLoop = isInLoop;
    }

    public Inst(Operand destReg,IrBlock irBlock){
        if (!(destReg instanceof Register))System.out.println("error in inst.java");
        this.destReg = destReg;
        this.irBlock = irBlock;
        //this.isInLoop = isInLoop;
    }

    @Override public abstract String toString();
}
