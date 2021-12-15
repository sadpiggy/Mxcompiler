package IR;

import IR.llvmType.LlvmStructType;
import IR.operand.GlobalOperand;

import java.util.ArrayList;
import java.util.Objects;

public class IrRoot {
    public ArrayList<LlvmStructType>structs;
    public ArrayList<IrFunc>funcs;
    public ArrayList<GlobalOperand>globalOperands;
   // public IrScope globalScope;

    public IrRoot(){
        structs = new ArrayList<>();
        funcs = new ArrayList<>();
        globalOperands = new ArrayList<>();
        //globalScope = new IrScope();
    }

    public void addStruct(LlvmStructType struct){
        structs.add(struct);
    }

    public void addFunc(IrFunc irFunc){
        funcs.add(irFunc);
    }

    public void addGlobalVar(GlobalOperand globalOperand){
        globalOperands.add(globalOperand);
    }

    public LlvmStructType getStruct(String name){
        for(var it : structs){
            if (Objects.equals(it.structName, name))return it;
        }
        System.out.println("itRoot.java 38 cnm");
        return null;
    }

    public IrFunc getIrFunc(String name){
        for (var it : funcs){
            if (Objects.equals(it.name, name))return it;
        }
        System.out.println("itRoot.java 46 cnm");
        return null;
    }

    public GlobalOperand getGlobalOperand(String name){
        for(var it : globalOperands){
            if (Objects.equals(it.name, name))return it;
        }
        System.out.println("itRoot.java 54 cnm");
        return null;
    }

}
