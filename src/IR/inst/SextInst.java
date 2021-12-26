package IR.inst;

import IR.IrBlock;
import IR.operand.Operand;

public class SextInst extends Inst{
    public Operand value;

    public SextInst(Operand destReg, IrBlock irBlock,Operand value) {
        super(destReg, irBlock);
        this.value = value;
    }

    @Override
    public String toString() {
        return destReg.toString() + " = sext " + value.type.toString() + " " + value.toString() + " to " + destReg.type.toString();
    }
}
