package IR;

import IR.operand.Operand;
import IR.operand.Register;

import java.util.HashMap;

public class IrScope {//不要束缚我自己了，我受够了
    public HashMap<String, Operand>vars;//name=name+block_name
    public IrScope fatherScope;
    public IrScope(){
        vars = new HashMap<>();fatherScope = null;
    }
    public IrScope(IrScope fatherScope){
        vars = new HashMap<>();
        this.fatherScope = fatherScope;
    }

    public void addVar(String name,Operand operand){
        vars.put(name,operand);
    }//name=name+block_name

    public Operand getVar(String name){
        if (vars.containsKey(name))return vars.get(name);
        if (fatherScope==null)return null;
        return fatherScope.getVar(name);
    }
}
