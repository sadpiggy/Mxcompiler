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
    // boolean debugUse = false;
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
        }
//        for (var it : registers){//这个要改
//            allocSingle(it);
//            //allocSingleOver(it);
//        }
        // 8189196276
        // 8189196276
        while (regStack.size()!=0){
            allocSingle(regStack.pop());
        }

      //  debugUse = true;

        for (var it : registers){//这个要改
            allocSingle(it);
           // allocSingleOver(it);
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
            if (it.getConflictSize()<allocRegSize){
                for (var reg : it.getConflictRegs()){
                    if (reg.getConflictRegs().contains(it)){
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
            for (var reg : target.getConflictRegs()){
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
            target.phyType = "address";
            asmFunc.changeStackSize();
            target.offset = -asmFunc.stackSize;
            for (var it : target.getConflictRegs())it.deleteConflict(target);
        }
    }

    void allocSingleOver(PhysicalReg target){
        //  System.out.println("nmsl");
        target.isVirtual = false;
        target.isAddress = true;
        target.phyType = "address";
        asmFunc.changeStackSize();
        target.offset = -asmFunc.stackSize;
        for (var it : target.getConflictRegs())it.deleteConflict(target);
    }

}
