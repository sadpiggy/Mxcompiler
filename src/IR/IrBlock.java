package IR;

import IR.inst.Inst;
import IR.inst.TerminalInst;
import IR.llvmType.LlvmLabelType;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class IrBlock {
    //public String name;
    public LlvmLabelType label;//头
    public LinkedList<Inst> insts;
    public TerminalInst tailInst;//尾
    public int loopDepth;

    public IrBlock(String index){//
        label = new LlvmLabelType(index);
        insts = new LinkedList<>();
        tailInst = null;
        loopDepth = 0;
    }

    public void push_back(Inst inst){
        insts.addLast(inst);
        if (inst instanceof TerminalInst)tailInst = (TerminalInst) inst;
    }

    public void printIr() throws FileNotFoundException {
//        System.setOut(new PrintStream(new BufferedOutputStream(
//                new FileOutputStream("ravel/build/test.ll")),true));
        System.out.println(label.index+":");
        for (var it : insts)System.out.println(it.toString());
        //System.out.println(insts.get(0).toString());
    }

    public String getBlockLabel(){
        return label.index;
    }

}
