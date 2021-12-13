package IR.inst;

import IR.IrBlock;
import IR.IrFunc;
import IR.operand.Operand;

import java.util.ArrayList;

public class CallInst extends Inst{
    public IrFunc callee;
    public ArrayList<Operand>params;

    public CallInst(Operand destReg, IrBlock irBlock,IrFunc callee,ArrayList<Operand>params) {
        super(destReg, irBlock);
        this.callee = callee;
        this.params = params;
    }

    public CallInst(IrBlock irBlock,IrFunc callee,ArrayList<Operand>params) {
        super(irBlock);
        this.callee = callee;
        this.params = params;
    }

    @Override
    public String toString() {
        String ret = "";
        if (this.destReg.toString() != null) ret += this.destReg.toString() + " = ";
        ret += "call " + callee.toString(params);
        return ret;
    }
}
