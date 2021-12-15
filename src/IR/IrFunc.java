package IR;

import IR.llvmType.LlvmSingleValueType;
import IR.operand.Operand;
import IR.operand.Register;

import java.util.ArrayList;
import java.util.LinkedList;

public class IrFunc extends Operand{
    public String name;
    public ArrayList<Register>formParams;
    public LinkedList<IrBlock>blocks;
    public boolean isDeclare;
   // public String formParamString;
    //public IrScope irScope;


    public IrFunc(LlvmSingleValueType type,String name,ArrayList<Register>formParams,boolean isDeclare) {
        super(type);
        this.name = name;
        this.formParams = formParams;
        this.blocks = new LinkedList<>();
        this.isDeclare = isDeclare;
        //irScope = new IrScope();

//        StringBuilder stringBuilder = new StringBuilder("(");
//        if (formParams!=null){
//            stringBuilder.append(formParams.get(0).toString());
//            for(int i=1;i<formParams.size();i++){
//                stringBuilder.append(",");
//                stringBuilder.append(formParams.get(i).toString());
//            }
//        }
//        stringBuilder.append(")");
//        formParamString = stringBuilder.toString();
    }

    public void setBlocks(LinkedList<IrBlock>blocks){
        this.blocks = blocks;
    }

    public String toString(ArrayList<Operand>realParams){
        StringBuilder stringBuilder = new StringBuilder("(");
        if (realParams!=null){
            stringBuilder.append(realParams.get(0).toString());
            for(int i=1;i<realParams.size();i++){
                stringBuilder.append(",");
                stringBuilder.append(realParams.get(i).toString());
            }
        }
        stringBuilder.append(")");
        return type.toString() +  " @" + name + stringBuilder.toString();
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public void PrintIr(){
        StringBuilder stringBuilder = new StringBuilder("(");
        if (formParams!=null){
            stringBuilder.append(formParams.get(0).toString());
            for(int i=1;i<formParams.size();i++){
                stringBuilder.append(",");
                stringBuilder.append(formParams.get(i).toString());
            }
        }
        stringBuilder.append(") {");

        if (isDeclare){
            //declare dso_local i32 @printf(i8*, ...)
            System.out.println(
                    "declare " + type.toString() + " @" + name + stringBuilder.toString()
            );
        }else {
            System.out.println(//define dso_local void @pp(i32 %0) #0 {
                    "define " + type.toString() + " @" + name + stringBuilder.toString()
            );
        }

        if (!isDeclare){
            for(var it : blocks)it.printIr();
            System.out.println("}");
        }
    }

}
