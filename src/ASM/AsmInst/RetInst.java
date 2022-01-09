package ASM.AsmInst;

import ASM.AsmBlock;

public class RetInst extends asmInst{
    public RetInst(AsmBlock belongTo) {
        super(belongTo);
    }

    @Override
    public String toString() {
        return "\t" + "ret";
    }
}
