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


        for (int i=0;i< registers.size();i++){
            PhysicalReg phyI = registers.get(i);
            for (int j=i+1;j< registers.size();j++){
                //System.out.println(phyI.);
                PhysicalReg phyJ = registers.get(j);
                if (!phyI.isAddress&&!phyJ.isAddress){
                    //System.out.println("gi");
                    boolean hasConflict = false;
                   for (int m = 0;m<phyI.liveStart.size();m++){
                       for (int n=0;n<phyJ.liveStart.size();n++){
                           if (phyI.liveStart.get(m).value> phyI.liveEnd.get(m).value){
                               phyI.liveEnd.get(m).value = phyI.liveStart.get(m).value;
                           }
                           if (phyJ.liveStart.get(n).value> phyJ.liveEnd.get(n).value){
                               phyJ.liveEnd.get(n).value = phyJ.liveStart.get(n).value;
                           }
                           if (!(phyI.liveEnd.get(m).value<phyJ.liveStart.get(n).value||phyI.liveStart.get(m).value>phyJ.liveEnd.get(n).value)){
                               hasConflict = true;
                               break;
                           }
                       }
                       if (hasConflict)break;
                   }
                   if (hasConflict){
                       phyI.addConflict(phyJ);
                       phyJ.addConflict(phyI);
                   }
                }
            }
        }
    }


}
