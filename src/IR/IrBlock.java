package IR;

import IR.inst.Inst;
import IR.inst.TerminalInst;
import IR.llvmType.LlvmLabelType;

import java.util.ArrayList;
import java.util.LinkedList;

public class IrBlock {
    //public String name;
    public LlvmLabelType label;
    public LinkedList<Inst> insts;
    public TerminalInst tailInst;
    public int loopDepth;

    public IrBlock(int index){//
        label = new LlvmLabelType(index);
        insts = new LinkedList<>();
        tailInst = null;
        loopDepth = 0;
    }

    public void push_back(Inst inst){
        insts.addLast(inst);
        if (inst instanceof TerminalInst)tailInst = (TerminalInst) inst;
    }

    public void printIr(){
        System.out.println(label.index+":");
        for (var it : insts)System.out.println(insts.toString());
    }

}
