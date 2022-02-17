package ASM;

import ASM.AsmInst.ITypeInst;
import ASM.AsmInst.MoveInst;
import ASM.AsmInst.asmInst;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.liveValue;
import IR.IrBlock;
import IR.inst.Inst;
import IR.operand.Register;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class AsmFunc {
    public String name;
    //public HashMap<String,Register> virtualRegs;
    public LinkedList<AsmBlock> blocks;
    public boolean isBuildIn;
    public AsmBlock headBlock;//这个应该用不上
    public int stackSize;
    public int instSize = 0;
    public ITypeInst headSpInst = null;
    public ITypeInst tailSpInst = null;
    public LinkedList<asmInst>insts = null;
    private int callTag = 0;
    public boolean[] useSreg = new boolean[12];
//    public LinkedList<asmInst>calleeSaveInsts1 = new LinkedList<>();
//    public LinkedList<asmInst>calleeSaveInsts2 = new LinkedList<>();
//    public LinkedList<asmInst>callerSaveInsts = new LinkedList<>();

    public int getCallTag(){
        return callTag++;
    }

    public ArrayList<PhysicalReg> registers;//0base

    public int changeStackSize(){
        stackSize+=4;
        return stackSize;
    }

    public void setInsts(){
        insts = new LinkedList<>();
        for (var block : blocks){
            if (insts.size()!=0)insts.getLast().nextInst = block.insts.getFirst();
            for (var inst : block.insts){
                insts.addLast(inst);
            }
        }
    }

    public liveValue getLiveEnd(){
        return new liveValue(registers.size());
    }

    public PhysicalReg newPhyReg(String labelName){
        PhysicalReg physicalReg = new PhysicalReg(labelName);//virtual
        setPhyReg(physicalReg,getLiveEnd(),getLiveEnd());
        return physicalReg;
    }

    public PhysicalReg newPhyReg(String labelName,liveValue liveEnd){
        PhysicalReg physicalReg = new PhysicalReg(labelName);//virtual
        setPhyReg(physicalReg,getLiveEnd(),liveEnd);
        return physicalReg;
    }

    public PhysicalReg newPhyReg(String labelName,liveValue liveBegin,liveValue liveEnd){
        PhysicalReg physicalReg = new PhysicalReg(labelName);//virtual
        setPhyReg(physicalReg,new liveValue(liveBegin.value),liveEnd);
        return physicalReg;
    }

    public PhysicalReg newPhyReg(String phyType,String labelName){
        PhysicalReg physicalReg = new PhysicalReg(phyType,labelName);//phy
        setPhyReg(physicalReg,getLiveEnd(),getLiveEnd());
        return physicalReg;
    }

    public PhysicalReg newPhyReg(String phyType,String labelName,liveValue liveEnd){
        PhysicalReg physicalReg = new PhysicalReg(phyType,labelName);//phy
        setPhyReg(physicalReg,getLiveEnd(),liveEnd);
        return physicalReg;
    }

    public boolean containReg(String name){
        for (var it : registers){
            if (Objects.equals(it.labelName, name)){
                return true;
            }
        }
        return false;
    }

    public PhysicalReg getPhyReg(String name, liveValue liveEnd){
        for (var it : registers){
            if (Objects.equals(it.labelName, name)){
                //it.liveEnd.getLast().value = liveEnd.value;
                //it.liveEnd.removeLast();
                //it.liveEnd.addLast(liveEnd);
                if (!it.liveEnd.getLast().notChange){
                    it.liveEnd.removeLast();
                    it.liveEnd.addLast(liveEnd);
                }
                return it;
            }
        }
        return null;
    }

    public PhysicalReg getPhyReg(String name, liveValue liveStart,liveValue liveEnd){
        for (var it : registers){
            if (Objects.equals(it.labelName, name)){
                it.liveStart.addLast(liveStart);
                it.liveEnd.addLast(liveEnd);
                return it;
            }
        }
        return null;
    }

    public void setPhyReg(PhysicalReg physicalReg,liveValue liveStart,liveValue liveEnd){//新产生一个register
        physicalReg.liveStart.addLast(liveStart);//registers.size();
        physicalReg.liveEnd.addLast(liveEnd);
        registers.add(physicalReg);
    }

    public AsmFunc(String name,boolean isBuildIn){
        this.name = name;
        for (int i=0;i<=11;i++)useSreg[i]=false;
        //virtualRegs = new HashMap<>();
        blocks = new LinkedList<>();
        this.isBuildIn = isBuildIn;
        headBlock = null;
        stackSize = 0;
        registers = new ArrayList<>();
    }

    public void setBlock(AsmBlock block){
        if (blocks.size()==0)headBlock = block;
        blocks.addLast(block);
    }
//sjkdc
    public void printAsm(int i) throws FileNotFoundException {
        if (!isBuildIn){





            System.out.println("\t.globl\t" + name +  "\n" +
                    "\t.p2align\t2\n" +
                      name + ":\n"        +
                    "\t.cfi_startproc\n" + "# %bb.0:");



            for (var it : blocks)it.printAsm();

            System.out.println(".Lfunc_endf" + i+ ":\n" +
                    "\t.size\t" + name + ", .Lfunc_endf" + i + "-" + name + "\n" +
                    "\t.cfi_endproc\n" +
                    "                                        # -- End function");

        }else
        {
            switch (name) {
                //buildIn
                case "print": {
                    System.out.println("\t.globl\tprint                   # -- Begin function print\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tprint,@function\n" +
                            "print:                                  # @print\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -16\n" +
                            "\t.cfi_def_cfa_offset 16\n" +
                            "\tsw\tra, 12(sp)\n" +
                            "\tsw\ts0, 8(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 16\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tlw\ta1, -16(s0)\n" +
                            "\tlui\ta0, %hi(.L.str)\n" +
                            "\taddi\ta0, a0, %lo(.L.str)\n" +
                            "\tcall\tprintf\n" +
                            "\tlw\ts0, 8(sp)\n" +
                            "\tlw\tra, 12(sp)\n" +
                            "\taddi\tsp, sp, 16\n" +
                            "\tret\n" +
                            ".Lfunc_end0:\n" +
                            "\t.size\tprint, .Lfunc_end0-print\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "println": {
                    System.out.println(".globl\tprintln                 # -- Begin function println\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tprintln,@function\n" +
                            "println:                                # @println\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -16\n" +
                            "\t.cfi_def_cfa_offset 16\n" +
                            "\tsw\tra, 12(sp)\n" +
                            "\tsw\ts0, 8(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 16\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tlw\ta1, -16(s0)\n" +
                            "\tlui\ta0, %hi(.L.str.1)\n" +
                            "\taddi\ta0, a0, %lo(.L.str.1)\n" +
                            "\tcall\tprintf\n" +
                            "\tlw\ts0, 8(sp)\n" +
                            "\tlw\tra, 12(sp)\n" +
                            "\taddi\tsp, sp, 16\n" +
                            "\tret\n" +
                            ".Lfunc_end1:\n" +
                            "\t.size\tprintln, .Lfunc_end1-println\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "printInt": {
                    System.out.println(".globl\tprintInt                # -- Begin function printInt\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tprintInt,@function\n" +
                            "printInt:                               # @printInt\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -16\n" +
                            "\t.cfi_def_cfa_offset 16\n" +
                            "\tsw\tra, 12(sp)\n" +
                            "\tsw\ts0, 8(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 16\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -12(s0)\n" +
                            "\tlw\ta1, -12(s0)\n" +
                            "\tlui\ta0, %hi(.L.str.2)\n" +
                            "\taddi\ta0, a0, %lo(.L.str.2)\n" +
                            "\tcall\tprintf\n" +
                            "\tlw\ts0, 8(sp)\n" +
                            "\tlw\tra, 12(sp)\n" +
                            "\taddi\tsp, sp, 16\n" +
                            "\tret\n" +
                            ".Lfunc_end2:\n" +
                            "\t.size\tprintInt, .Lfunc_end2-printInt\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "printlnInt": {
                    System.out.println(".globl\tprintlnInt              # -- Begin function printlnInt\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tprintlnInt,@function\n" +
                            "printlnInt:                             # @printlnInt\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -16\n" +
                            "\t.cfi_def_cfa_offset 16\n" +
                            "\tsw\tra, 12(sp)\n" +
                            "\tsw\ts0, 8(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 16\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -12(s0)\n" +
                            "\tlw\ta1, -12(s0)\n" +
                            "\tlui\ta0, %hi(.L.str.3)\n" +
                            "\taddi\ta0, a0, %lo(.L.str.3)\n" +
                            "\tcall\tprintf\n" +
                            "\tlw\ts0, 8(sp)\n" +
                            "\tlw\tra, 12(sp)\n" +
                            "\taddi\tsp, sp, 16\n" +
                            "\tret\n" +
                            ".Lfunc_end3:\n" +
                            "\t.size\tprintlnInt, .Lfunc_end3-printlnInt\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "getString": {
                    System.out.println(".globl\tgetString               # -- Begin function getString\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tgetString,@function\n" +
                            "getString:                              # @getString\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -16\n" +
                            "\t.cfi_def_cfa_offset 16\n" +
                            "\tsw\tra, 12(sp)\n" +
                            "\tsw\ts0, 8(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 16\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\taddi\ta0, zero, 1024\n" +
                            "\tmv\ta1, zero\n" +
                            "\tcall\tmalloc\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tlw\ta1, -16(s0)\n" +
                            "\tlui\ta0, %hi(.L.str)\n" +
                            "\taddi\ta0, a0, %lo(.L.str)\n" +
                            "\tcall\t__isoc99_scanf\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlw\ts0, 8(sp)\n" +
                            "\tlw\tra, 12(sp)\n" +
                            "\taddi\tsp, sp, 16\n" +
                            "\tret\n" +
                            ".Lfunc_end5:\n" +
                            "\t.size\tgetString, .Lfunc_end5-getString\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "getInt": {
                    System.out.println(".globl\tgetInt                  # -- Begin function getInt\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tgetInt,@function\n" +
                            "getInt:                                 # @getInt\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -16\n" +
                            "\t.cfi_def_cfa_offset 16\n" +
                            "\tsw\tra, 12(sp)\n" +
                            "\tsw\ts0, 8(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 16\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tlui\ta0, %hi(.L.str.2)\n" +
                            "\taddi\ta0, a0, %lo(.L.str.2)\n" +
                            "\taddi\ta1, s0, -12\n" +
                            "\tcall\t__isoc99_scanf\n" +
                            "\tlw\ta0, -12(s0)\n" +
                            "\tlw\ts0, 8(sp)\n" +
                            "\tlw\tra, 12(sp)\n" +
                            "\taddi\tsp, sp, 16\n" +
                            "\tret\n" +
                            ".Lfunc_end6:\n" +
                            "\t.size\tgetInt, .Lfunc_end6-getInt\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "toString": {
                    System.out.println(".globl\ttoString                # -- Begin function toString\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\ttoString,@function\n" +
                            "toString:                               # @toString\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -16\n" +
                            "\t.cfi_def_cfa_offset 16\n" +
                            "\tsw\tra, 12(sp)\n" +
                            "\tsw\ts0, 8(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 16\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -12(s0)\n" +
                            "\taddi\ta0, zero, 15\n" +
                            "\tmv\ta1, zero\n" +
                            "\tcall\tmalloc\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlw\ta2, -12(s0)\n" +
                            "\tlui\ta1, %hi(.L.str.2)\n" +
                            "\taddi\ta1, a1, %lo(.L.str.2)\n" +
                            "\tcall\tsprintf\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlw\ts0, 8(sp)\n" +
                            "\tlw\tra, 12(sp)\n" +
                            "\taddi\tsp, sp, 16\n" +
                            "\tret\n" +
                            ".Lfunc_end7:\n" +
                            "\t.size\ttoString, .Lfunc_end7-toString\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_length": {
                    System.out.println(".globl\tstring_length           # -- Begin function string_length\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tstring_length,@function\n" +
                            "string_length:                          # @string_length\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -16\n" +
                            "\t.cfi_def_cfa_offset 16\n" +
                            "\tsw\tra, 12(sp)\n" +
                            "\tsw\ts0, 8(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 16\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tcall\tstrlen\n" +
                            "\tlw\ts0, 8(sp)\n" +
                            "\tlw\tra, 12(sp)\n" +
                            "\taddi\tsp, sp, 16\n" +
                            "\tret\n" +
                            ".Lfunc_end15:\n" +
                            "\t.size\tstring_length, .Lfunc_end15-string_length\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_substring": {
                    System.out.println(".globl\tstring_substring        # -- Begin function string_substring\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tstring_substring,@function\n" +
                            "string_substring:                       # @string_substring\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -32\n" +
                            "\t.cfi_def_cfa_offset 32\n" +
                            "\tsw\tra, 28(sp)\n" +
                            "\tsw\ts0, 24(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 32\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tsw\ta1, -20(s0)\n" +
                            "\tsw\ta2, -24(s0)\n" +
                            "\tlw\ta0, -24(s0)\n" +
                            "\tlw\ta1, -20(s0)\n" +
                            "\tsub\ta0, a0, a1\n" +
                            "\taddi\ta0, a0, 1\n" +
                            "\tsrai\ta1, a0, 31\n" +
                            "\tcall\tmalloc\n" +
                            "\tsw\ta0, -32(s0)\n" +
                            "\tlw\ta0, -32(s0)\n" +
                            "\tlw\ta1, -16(s0)\n" +
                            "\tlw\ta2, -20(s0)\n" +
                            "\tadd\ta1, a1, a2\n" +
                            "\tlw\ta3, -24(s0)\n" +
                            "\tsub\ta2, a3, a2\n" +
                            "\tcall\tmemcpy\n" +
                            "\tlw\ta0, -32(s0)\n" +
                            "\tlw\ta1, -24(s0)\n" +
                            "\tlw\ta2, -20(s0)\n" +
                            "\tsub\ta1, a1, a2\n" +
                            "\tadd\ta0, a0, a1\n" +
                            "\tsb\tzero, 0(a0)\n" +
                            "\tlw\ta0, -32(s0)\n" +
                            "\tlw\ts0, 24(sp)\n" +
                            "\tlw\tra, 28(sp)\n" +
                            "\taddi\tsp, sp, 32\n" +
                            "\tret\n" +
                            ".Lfunc_endsub0:\n" +
                            "\t.size\tstring_substring, .Lfunc_endsub0-string_substring\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_parseInt": {
                    System.out.println(".globl\tstring_parseInt         # -- Begin function string_parseInt\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tstring_parseInt,@function\n" +
                            "string_parseInt:                        # @string_parseInt\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -32\n" +
                            "\t.cfi_def_cfa_offset 32\n" +
                            "\tsw\tra, 28(sp)\n" +
                            "\tsw\ts0, 24(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 32\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlui\ta1, %hi(.L.str.2)\n" +
                            "\taddi\ta1, a1, %lo(.L.str.2)\n" +
                            "\taddi\ta2, s0, -20\n" +
                            "\tcall\t__isoc99_sscanf\n" +
                            "\tlw\ta0, -20(s0)\n" +
                            "\tlw\ts0, 24(sp)\n" +
                            "\tlw\tra, 28(sp)\n" +
                            "\taddi\tsp, sp, 32\n" +
                            "\tret\n" +
                            ".Lfunc_end17:\n" +
                            "\t.size\tstring_parseInt, .Lfunc_end17-string_parseInt\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_ord": {
                    System.out.println(".globl\tstring_ord              # -- Begin function string_ord\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tstring_ord,@function\n" +
                            "string_ord:                             # @string_ord\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -32\n" +
                            "\t.cfi_def_cfa_offset 32\n" +
                            "\tsw\tra, 28(sp)\n" +
                            "\tsw\ts0, 24(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 32\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tsw\ta1, -20(s0)\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlw\ta1, -20(s0)\n" +
                            "\tadd\ta0, a0, a1\n" +
                            "\tlb\ta0, 0(a0)\n" +
                            "\tlw\ts0, 24(sp)\n" +
                            "\tlw\tra, 28(sp)\n" +
                            "\taddi\tsp, sp, 32\n" +
                            "\tret\n" +
                            ".Lfunc_end18:\n" +
                            "\t.size\tstring_ord, .Lfunc_end18-string_ord\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_add": {
                    System.out.println(".globl\tstring_add              # -- Begin function string_add\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tstring_add,@function\n" +
                            "string_add:                             # @string_add\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -32\n" +
                            "\t.cfi_def_cfa_offset 32\n" +
                            "\tsw\tra, 28(sp)\n" +
                            "\tsw\ts0, 24(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 32\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tsw\ta1, -24(s0)\n" +
                            "\taddi\ta0, zero, 1024\n" +
                            "\tmv\ta1, zero\n" +
                            "\tcall\tmalloc\n" +
                            "\tsw\ta0, -32(s0)\n" +
                            "\tlw\ta0, -32(s0)\n" +
                            "\tlw\ta1, -16(s0)\n" +
                            "\tcall\tstrcpy\n" +
                            "\tlw\ta0, -32(s0)\n" +
                            "\tlw\ta1, -24(s0)\n" +
                            "\tcall\tstrcat\n" +
                            "\tlw\ta0, -32(s0)\n" +
                            "\tlw\ts0, 24(sp)\n" +
                            "\tlw\tra, 28(sp)\n" +
                            "\taddi\tsp, sp, 32\n" +
                            "\tret\n" +
                            ".Lfunc_end8:\n" +
                            "\t.size\tstring_add, .Lfunc_end8-string_add\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_equal": {
                    System.out.println("string_equal:                           # @string_equal\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -32\n" +
                            "\t.cfi_def_cfa_offset 32\n" +
                            "\tsw\tra, 28(sp)\n" +
                            "\tsw\ts0, 24(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 32\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tsw\ta1, -24(s0)\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlw\ta1, -24(s0)\n" +
                            "\tcall\tstrcmp\n" +
                            "\tseqz\ta0, a0\n" +
                            "\tlw\ts0, 24(sp)\n" +
                            "\tlw\tra, 28(sp)\n" +
                            "\taddi\tsp, sp, 32\n" +
                            "\tret\n" +
                            ".Lfunc_end9:\n" +
                            "\t.size\tstring_equal, .Lfunc_end9-string_equal\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_notequal": {
                    System.out.println(".globl\tstring_notequal         # -- Begin function string_notequal\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tstring_notequal,@function\n" +
                            "string_notequal:                        # @string_notequal\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -32\n" +
                            "\t.cfi_def_cfa_offset 32\n" +
                            "\tsw\tra, 28(sp)\n" +
                            "\tsw\ts0, 24(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 32\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tsw\ta1, -24(s0)\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlw\ta1, -24(s0)\n" +
                            "\tcall\tstrcmp\n" +
                            "\tsnez\ta0, a0\n" +
                            "\tlw\ts0, 24(sp)\n" +
                            "\tlw\tra, 28(sp)\n" +
                            "\taddi\tsp, sp, 32\n" +
                            "\tret\n" +
                            ".Lfunc_end10:\n" +
                            "\t.size\tstring_notequal, .Lfunc_end10-string_notequal\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_lesser": {
                    System.out.println(".globl\tstring_lesser           # -- Begin function string_lesser\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tstring_lesser,@function\n" +
                            "string_lesser:                          # @string_lesser\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -32\n" +
                            "\t.cfi_def_cfa_offset 32\n" +
                            "\tsw\tra, 28(sp)\n" +
                            "\tsw\ts0, 24(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 32\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tsw\ta1, -24(s0)\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlw\ta1, -24(s0)\n" +
                            "\tcall\tstrcmp\n" +
                            "\tsrli\ta0, a0, 31\n" +
                            "\tlw\ts0, 24(sp)\n" +
                            "\tlw\tra, 28(sp)\n" +
                            "\taddi\tsp, sp, 32\n" +
                            "\tret\n" +
                            ".Lfunc_end11:\n" +
                            "\t.size\tstring_lesser, .Lfunc_end11-string_lesser\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_greater": {
                    System.out.println(".globl\tstring_greater          # -- Begin function string_greater\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tstring_greater,@function\n" +
                            "string_greater:                         # @string_greater\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -32\n" +
                            "\t.cfi_def_cfa_offset 32\n" +
                            "\tsw\tra, 28(sp)\n" +
                            "\tsw\ts0, 24(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 32\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tsw\ta1, -24(s0)\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlw\ta1, -24(s0)\n" +
                            "\tcall\tstrcmp\n" +
                            "\tsgtz\ta0, a0\n" +
                            "\tlw\ts0, 24(sp)\n" +
                            "\tlw\tra, 28(sp)\n" +
                            "\taddi\tsp, sp, 32\n" +
                            "\tret\n" +
                            ".Lfunc_end12:\n" +
                            "\t.size\tstring_greater, .Lfunc_end12-string_greater\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_lessEqual": {
                    System.out.println(".globl\tstring_lessEqual        # -- Begin function string_lessEqual\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tstring_lessEqual,@function\n" +
                            "string_lessEqual:                       # @string_lessEqual\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -32\n" +
                            "\t.cfi_def_cfa_offset 32\n" +
                            "\tsw\tra, 28(sp)\n" +
                            "\tsw\ts0, 24(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 32\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tsw\ta1, -24(s0)\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlw\ta1, -24(s0)\n" +
                            "\tcall\tstrcmp\n" +
                            "\tslti\ta0, a0, 1\n" +
                            "\tlw\ts0, 24(sp)\n" +
                            "\tlw\tra, 28(sp)\n" +
                            "\taddi\tsp, sp, 32\n" +
                            "\tret\n" +
                            ".Lfunc_end13:\n" +
                            "\t.size\tstring_lessEqual, .Lfunc_end13-string_lessEqual\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                    break;
                }
                case "string_greatEqual": {
                    System.out.println(".globl\tstring_greatEqual       # -- Begin function string_greatEqual\n" +
                            "\t.p2align\t2\n" +
                            "\t.type\tstring_greatEqual,@function\n" +
                            "string_greatEqual:                      # @string_greatEqual\n" +
                            "\t.cfi_startproc\n" +
                            "# %bb.0:\n" +
                            "\taddi\tsp, sp, -32\n" +
                            "\t.cfi_def_cfa_offset 32\n" +
                            "\tsw\tra, 28(sp)\n" +
                            "\tsw\ts0, 24(sp)\n" +
                            "\t.cfi_offset ra, -4\n" +
                            "\t.cfi_offset s0, -8\n" +
                            "\taddi\ts0, sp, 32\n" +
                            "\t.cfi_def_cfa s0, 0\n" +
                            "\tsw\ta0, -16(s0)\n" +
                            "\tsw\ta1, -24(s0)\n" +
                            "\tlw\ta0, -16(s0)\n" +
                            "\tlw\ta1, -24(s0)\n" +
                            "\tcall\tstrcmp\n" +
                            "\tnot\ta0, a0\n" +
                            "\tsrli\ta0, a0, 31\n" +
                            "\tlw\ts0, 24(sp)\n" +
                            "\tlw\tra, 28(sp)\n" +
                            "\taddi\tsp, sp, 32\n" +
                            "\tret\n" +
                            ".Lfunc_end14:\n" +
                            "\t.size\tstring_greatEqual, .Lfunc_end14-string_greatEqual\n" +
                            "\t.cfi_endproc\n" +
                            "                                        # -- End function");
                }
            }
        }

    }

    public String getName(){
        return null;
    }

    public String toString(){
        return null;
    }

    public void setStackSize(){
       if (stackSize%16!=0){
           stackSize = stackSize/16;
           stackSize = stackSize*16 + 16;
       }
    }

//    public void setVirtualReg(Register register){
//        virtualRegs.put(register.name,register);
//    }
//
//    public boolean containReg(String name){
//        return virtualRegs.containsKey(name);
//    }
//
//    public Register getReg(String name){
//        return virtualRegs.get(name);
//    }

}
