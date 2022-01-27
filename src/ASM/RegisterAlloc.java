package ASM;

import ASM.AsmOperand.PhysicalReg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;

public class RegisterAlloc {
    public AsmFunc asmFunc;
    public Stack<PhysicalReg> regStack;//被删掉的节点
    public int allocRegSize = 15;
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
        for (var it : registers){
            allocSingle(it);
        }
        while (regStack.size()!=0){
            allocSingle(regStack.pop());
        }
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
                if (!reg.isVirtual&& Objects.equals(reg.phyType, it)){
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

}