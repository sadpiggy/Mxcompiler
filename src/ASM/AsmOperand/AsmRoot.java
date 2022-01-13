package ASM.AsmOperand;

import ASM.AsmBlock;
import ASM.AsmFunc;
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
                ConflictAnalise conflictAnalise = new ConflictAnalise(it);
                conflictAnalise.run();
//                for (var value : it.registers){
//                    System.out.println(value.conflictRegs.size());
//                    for (var v:value.conflictRegs){
//                        System.out.println(v);
//                    }
//                }
                RegisterAlloc registerAlloc = new RegisterAlloc(it);
                registerAlloc.foolishAlloc();
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
                "\t.text\n" +
                        "\t.file\t\"myTest.mx\""
        );
        for (int i=0;i< asmFuncs.size();i++){
            asmFuncs.get(i).printAsm(i);
        }

       printStream.println("\t.type\t.L.str,@object          # @.str\n" +
               "\t.section\t.rodata.str1.1,\"aMS\",@progbits,1\n" +
               "\t.L.str:\n" +
               "\t.asciz\t\"%s\"\n" +
               "\t.size\t.L.str, 3\n" +
               "\n" +
               "\t.type\t.L.str.1,@object        # @.str.1\n" +
               "\t.L.str.1:\n" +
               "\t.asciz\t\"%s\\n\"\n" +
               "\t.size\t.L.str.1, 4\n" +
               "\n" +
               "\t.type\t.L.str.2,@object        # @.str.2\n" +
               "\t.L.str.2:\n" +
               "\t.asciz\t\"%d\"\n" +
               "\t.size\t.L.str.2, 3\n" +
               "\n" +
               "\t.type\t.L.str.3,@object        # @.str.3\n" +
               "\t.L.str.3:\n" +
               "\t.asciz\t\"%d\\n\"\n" +
               "\t.size\t.L.str.3, 4");

        for (var it :asmStringContains){
            printStream.println(it.toString()+"\n");
        }

        if (asmGlobalValues.size()!=0){
            printStream.println("\t.section\t.sdata,\"aw\",@progbits");
            for (var it : asmGlobalValues){printStream.println(it.toString()+"\n");}
        }

    }

}
