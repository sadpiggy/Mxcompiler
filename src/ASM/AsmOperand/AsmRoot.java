package ASM.AsmOperand;

import ASM.AsmBlock;
import ASM.AsmFunc;
import ASM.AsmInst.MoveInst;
import ASM.AsmInst.RetInst;
import ASM.ConflictAnalise;
import ASM.RegisterAlloc;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AsmRoot {
    public ArrayList<AsmFunc> asmFuncs;
    public ArrayList<AsmGlobalValue> asmGlobalValues;
    public ArrayList<AsmGlobalValue> asmStringContains;
    public boolean hasString;

    public AsmRoot(){
        asmFuncs = new ArrayList<>();
        asmGlobalValues = new ArrayList<>();
        asmStringContains = new ArrayList<>();
    }

    public void regsAlloc(){
        for (var it : asmFuncs){
            if (!it.isBuildIn){
                it.setInsts();
                ConflictAnalise conflictAnalise = new ConflictAnalise(it);
                conflictAnalise.run();
                RegisterAlloc registerAlloc = new RegisterAlloc(it);
                registerAlloc.foolishAlloc();
               // registerAlloc.alloverflow();

                for (var target : it.insts){
                    if (!target.isDead&&target.rd!=null&&!target.rd.isAddress){
                        it.moveInsts.push(target.rd.toString());
                    }
                }

                PhysicalReg[] savaRegs = new PhysicalReg[11];

                it.blocks.getLast().insts.removeLast();

                for (int index=1;index<=11;index++){
                    if (it.moveInsts.contains("s"+index)){
                        savaRegs[index-1] = new PhysicalReg(it.name+"_s"+index,-it.changeStackSize());
                        it.blocks.getFirst().push_front(new MoveInst(it.blocks.getFirst(),savaRegs[index-1], new PhysicalReg("s"+index,"s"+index),true));
                        it.blocks.getLast().push_back(new MoveInst(it.blocks.getLast(), new PhysicalReg("s"+index,"s"+index),savaRegs[index-1],true));
                    }
                }

                it.blocks.getLast().push_back(new RetInst(it.blocks.getLast()));

                it.setStackSize();
                it.headSpInst.imm = new IntegerImm(-it.stackSize);
                it.tailSpInst.imm = new IntegerImm(it.stackSize);

            }
        }
    }

    public void setFunc(AsmFunc func){
        asmFuncs.add(func);
    }

    public void printAsm(PrintStream printStream) throws FileNotFoundException {

//        for (var it : asmFuncs){
//            System.out.println(it.blocks.size());
//        }

//        System.setOut(new PrintStream(new BufferedOutputStream(
//                new FileOutputStream("ravel/build/test.s")),true));


        printStream.println(
                "\t.text\n"
                        //+ "\t.file\t\"myTest.mx\""
        );
        for (int i=0;i< asmFuncs.size();i++){
            asmFuncs.get(i).printAsm(i);
        }

        printStream.println("\t.section\t.sdata,\"aw\",@progbits");

        printStream.println(
               // "\t.section\t.rodata.str1.1,\"aMS\",@progbits,1\n" +
                        "\t.L.str:\n" +
                        "\t.asciz\t\"%s\"\n" +

                        "\t.L.str.1:\n" +
                        "\t.asciz\t\"%s\\n\"\n" +

                        "\t.L.str.2:\n" +
                        "\t.asciz\t\"%d\"\n" +

                        "\t.L.str.3:\n" +
                        "\t.asciz\t\"%d\\n\"\n" );


        printStream.println();

        for (var it :asmStringContains){
            printStream.println(it.toString()+"\n");
        }

        if (asmGlobalValues.size()!=0){
            //printStream.println("\t.section\t.sdata,\"aw\",@progbits");
            for (var it : asmGlobalValues){printStream.println(it.toString()+"\n");}
        }

    }

}
