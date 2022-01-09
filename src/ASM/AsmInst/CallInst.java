package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmFunc;

public class CallInst extends asmInst{
    public String callFunc;

    public CallInst(AsmBlock belongTo,String callFunc) {
        super(belongTo);
        this.callFunc = callFunc;
    }

    @Override
    public String toString() {
        return "\t" + "call " + callFunc;
    }
}
