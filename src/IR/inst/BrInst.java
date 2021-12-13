package IR.inst;

import IR.IrBlock;
import IR.llvmType.LlvmLabelType;
import IR.operand.Operand;

public class BrInst extends TerminalInst{
    public Operand condition;
    public LlvmLabelType trueBranch;
    public LlvmLabelType falseBranch;

    public BrInst(IrBlock irBlock,Operand condition,LlvmLabelType trueBranch,LlvmLabelType falseBranch) {
        super(irBlock);
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    public BrInst(IrBlock irBlock,LlvmLabelType nextBranch) {
        super(irBlock);
        this.trueBranch = nextBranch;
    }

    @Override
    public String toString() {
        if (condition==null)return "br label " + trueBranch.toString();
        else return "br " + condition.type.toString() + " " + condition.toString() + ", label " + trueBranch.toString() + ", label " + falseBranch.toString();
    }
}
