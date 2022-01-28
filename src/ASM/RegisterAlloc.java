package ASM;

import ASM.AsmInst.MoveInst;
import ASM.AsmInst.asmInst;
import ASM.AsmOperand.PhysicalReg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;

public class RegisterAlloc {
    public AsmFunc asmFunc;
    public Stack<PhysicalReg> regStack;//被删掉的节点
    public int allocRegSize = 22;
    public RegisterAlloc(AsmFunc asmFunc){
        this.asmFunc = asmFunc;
        regStack = new Stack<>();
    }

    public void foolishAlloc(){
        ArrayList<PhysicalReg> registers = new ArrayList<>();//asmFunc.registers
        for (var it : asmFunc.registers){
            if (it.isVirtual){
                registers.add(it);
            }
        }
        boolean value = true;
        while (value){
            value = deleteNode(registers);
            //System.out.println("ml");
        }
//        for (var it : registers){
//            allocSingle(it);
//        }

        while (registers.size()!=0){
            PhysicalReg reg = registers.get(0);
            allocSingle(reg);
            registers.remove(0);
            if (registers.size()!=0){
                value = true;
                while (value){
                    value = deleteNode(registers);
                }
            }
        }

        while (regStack.size()!=0){
            allocSingle(regStack.pop());
        }

        clearCallerSave();
    }

    public void alloverflow(){
        ArrayList<PhysicalReg> registers = new ArrayList<>();//asmFunc.registers
        for (var it : asmFunc.registers){
            if (it.isVirtual){
                registers.add(it);
            }
        }
        for (var it : registers){
            // System.out.println(it);
            it.isVirtual = false;
            it.isAddress = true;
            asmFunc.changeStackSize();
            it.offset = -asmFunc.stackSize;
            //System.out.println(asmFunc.stackSize);
        }
    }

    boolean deleteNode(ArrayList<PhysicalReg> registers){
        boolean value = false;
        ArrayList<PhysicalReg>deleteList = new ArrayList<>();
        for (var it : registers){
            if (it.conflictRegs.size()<allocRegSize){
                for (var reg : it.conflictRegs){
                    if (reg.conflictRegs.contains(it)){
                        reg.deleteConflict(it);
                    }
                }
                regStack.push(it);
                //registers.remove(it);
                deleteList.add(it);
                value = true;
            }
        }
        for (var it : deleteList){
            registers.remove(it);
        }
        return value;
    }

    void allocSingle(PhysicalReg target){
        boolean hadAlloc = false;
        for (var it : PhysicalReg.allocatablePhyRegNames){
            boolean flag = true;
            for (var reg : target.conflictRegs){
                if (/*!reg.isVirtual&& */Objects.equals(reg.phyType, it)){
                    flag = false;
                    break;
                }
            }
            if (flag){
                target.isVirtual=false;
                target.phyType = it;
                hadAlloc = true;
                break;
            }
        }
        if (!hadAlloc){//溢出
            //  System.out.println("nmsl");
            target.isVirtual = false;
            target.isAddress = true;
            asmFunc.changeStackSize();
            target.offset = -asmFunc.stackSize;
            for (var it : target.conflictRegs)it.deleteConflict(target);
        }
    }

    void clearCallerSave(){
        //System.out.println(asmFunc.callerSaveInsts.size());
        for (int i=asmFunc.callerSaveInsts.size()-1;i>=0;i--){
            asmInst inst = asmFunc.callerSaveInsts.get(i);
            asmInst next = inst.nextInst;
            inst.isDead = true;
            while (next!=null){
                if (!next.isDead){
                    if (Objects.equals(next.getRdName(), inst.getRs1Name()))break;
                    if (Objects.equals(next.getRs1Name(), inst.getRs1Name())){
                        next.rs1 = inst.rd;
                        inst.isDead = false;
                    }
                    if (Objects.equals(next.getRs2Name(), inst.getRs1Name())){
                        next.rs2 = inst.rd;
                        inst.isDead = false;
                    }
                }
                next = next.nextInst;
            }
        }
    }

}