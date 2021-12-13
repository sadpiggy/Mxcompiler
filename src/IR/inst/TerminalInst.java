package IR.inst;

import IR.IrBlock;

public abstract class TerminalInst extends Inst{
    public TerminalInst(IrBlock irBlock) {
        super(irBlock);
    }

    @Override public abstract String toString();
}
