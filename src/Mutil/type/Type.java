package Mutil.type;

import Mutil.Position;
import Mutil.error.SemanticError;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
//开始瞎几*乱写了
public class Type {
   public String typeName = "";//typeName是返回值,
   public boolean isClass = false;
   public boolean isFunc = false;
   public boolean isVar = false;

   //public boolean isName = false;
   public boolean isArray = false;//int[][] a;是array并且应该也是int
   public boolean isVoid = false;
   //public boolean isBasic = false;
   public boolean isId = false;
   public boolean isInt = false;
   public boolean isBool = false;
   public boolean isString = false;
   public boolean isNull = false;
   public int numOfArray = 0;
   public Position position;//用于检测是在后面还是前面
   public boolean isAssignAble = false;
   //public boolean isNewExpr = false;

   public HashMap<String,Type>varMembers = null;
   public HashMap<String,Type>funcMembers = null;
   public ArrayList<Pair<String,Type>>params = null;

   public Type(){}
   public void defineClass(){
      varMembers = new HashMap<>();
      funcMembers = new HashMap<>();
      isClass = true;//这个可能没有必要
   }
   public void defineFunc(){
      params = new ArrayList<>();
      isFunc = true;
   }

   public Type clone(){//这个clone不是完全克隆，只是克隆一小需要用到的部分，不然效率太低了
      Type type = new Type();
      type.typeName = typeName;
      type.isBool = isBool;
      type.isArray = isArray;
      type.isId = isId;
      type.isVoid = isVoid;
      type.isString = isString;
      type.isInt = isInt;
      type.isVar = isVar;
      type.isClass = isClass;
      type.isFunc = isFunc;
      type.isAssignAble = isAssignAble;
      type.position = position;
      type.numOfArray = numOfArray;
      return type;
   }

   public String getTypeName(){
      return typeName;
   }

   public void addFuncMembers(String Id,Type type){
      if(funcMembers==null)throw new SemanticError("no class but use funcMember",type.position);
      if(funcMembers.containsKey(Id))throw new SemanticError("repeated funcName in class", type.position);
      funcMembers.put(Id,type);
   }

   public void addVarMembers(String Id,Type type){
      if(varMembers==null)throw new SemanticError("no class but use varMember",type.position);
      if(varMembers.containsKey(Id))throw new SemanticError("repeated varName in class", type.position);
      varMembers.put(Id,type);
   }

}
