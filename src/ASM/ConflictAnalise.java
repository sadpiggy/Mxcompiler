package ASM;

import ASM.AsmOperand.PhysicalReg;

import java.util.ArrayList;

public class ConflictAnalise {
    public AsmFunc asmFunc;
    public ConflictAnalise(AsmFunc asmFunc){
        this.asmFunc = asmFunc;
    }
    public void run(){
        ArrayList<PhysicalReg> registers = asmFunc.registers;
        //建边是单向还是双向呢 //双向吧

//        for (var it : registers){
//            if (it.isVirtual)
//            System.out.println(it.liveStart+"  "+ it.liveEnd);
//        }

        for (int i=0;i< registers.size();i++){
            PhysicalReg phyI = registers.get(i);
            for (int j=i+1;j< registers.size();j++){
                //System.out.println(phyI.);
                PhysicalReg phyJ = registers.get(j);
                if (phyI.isVirtual&&phyJ.isVirtual){
                    //System.out.println("gi");
                    if (phyI.liveStart> phyI.liveEnd)phyI.liveEnd = phyI.liveStart;
                    if (phyJ.liveStart> phyJ.liveEnd)phyJ.liveEnd = phyJ.liveStart;
                    if (!(phyI.liveEnd<phyJ.liveStart||phyI.liveStart>phyJ.liveEnd)){
                        //phyI.conflictRegs.push(phyJ);
                        //phyJ.conflictRegs.push(phyI);
                        phyI.addConflict(phyJ);
                        phyJ.addConflict(phyI);
                    }
                }
            }
        }
    }
}
