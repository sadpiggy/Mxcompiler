package ASM.AsmInst;

import ASM.AsmBlock;

public class JInst extends asmInst{
    public String target;

    public JInst(AsmBlock belongTo,String target) {
        super(belongTo);
        this.target = target;
    }

    @Override
    public String toString() {
        return "\t" + "j\t" + target;
    }
}
