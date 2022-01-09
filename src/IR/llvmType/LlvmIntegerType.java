package IR.llvmType;

public class LlvmIntegerType extends LlvmSingleValueType{//1 bit i位
    public int width;
    public boolean isBool;
   // public int size;

    public LlvmIntegerType(int width,boolean isBool){//bool i1,占8位;char i8.占8位;int i32,占32位
        this.width  = width;
        this.isBool = isBool;
        //this.size = width;
    }


    @Override
    public String toString() {
        if (isBool)return "i"+1;
        else return "i"+width;
    }

    @Override
    public int getSize() {
        return 32;
    }

    @Override
    public int getAlignSize() {
        return 4;
    }
}
