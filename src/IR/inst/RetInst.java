package IR.inst;

import IR.IrBlock;
import IR.operand.Operand;

public class RetInst extends TerminalInst{
    public Operand value;

    public RetInst(IrBlock irBlock,Operand value) {
        super(irBlock);
        this.value = value;
    }

    @Override
    public String toString() {
        if (value==null)return "ret void";
        else return "ret " + value.type.toString() + " " + value.toString();
    }
}
