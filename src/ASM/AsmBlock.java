package ASM;

import ASM.AsmInst.RetInst;
import ASM.AsmInst.asmInst;
import IR.inst.Inst;
import IR.inst.TerminalInst;
import IR.llvmType.LlvmLabelType;

import java.io.FileNotFoundException;
import java.util.LinkedList;

public class AsmBlock {
    public AsmFunc belongTo;
    public int instIndex;
    public String name;
    public LinkedList<asmInst> insts;
    public RetInst tailInst;//尾
    public int loopDepth;

    public AsmBlock(String name,AsmFunc belongTo){
        insts = new LinkedList<>();
        loopDepth = 0;
        this.name = name;
        tailInst = null;
        instIndex = 0;
        this.belongTo = belongTo;
    }

    public void push_back(asmInst inst){
        insts.addLast(inst);
        inst.name = instIndex++;
        if (inst instanceof RetInst)tailInst = (RetInst) inst;
        belongTo.instSize++;
    }

    public void push_front(asmInst inst){
        insts.addFirst(inst);
    }

    public void pushAfterInst(asmInst targetInst,asmInst inst){
        int index = 0;
        for (var it : insts){
            if (it.name == targetInst.name)break;
            index++;
        }
        insts.add(++index,inst);
        inst.name = instIndex++;
    }

    public String toString(){
        return name;
    }

    public void printAsm() throws FileNotFoundException {
        System.out.println(".L" + name+":\n");
        for (var it : insts)System.out.println(it.toString());
    }

    public void printDebug() {
        System.out.println(".L" + name+":\n");
        for (var it : insts)System.out.println(it.toString());
    }

}
