package IR.llvmType;

public class LlvmArrayType extends LlvmAggregateType{//这个不用了,因为这和MX不一样
    public LlvmFirstClassType basicType;
    public int dimension;
    public int size;

    public LlvmArrayType(LlvmFirstClassType basicType,int dimension){
        this.basicType = basicType;
        this.dimension = dimension;
        size = this.basicType.getSize()*this.dimension;
    }

    @Override
    public String toString() {
        return "[ " + dimension + " x " + basicType.toString() + " ]";
    }

    @Override
    public int getSize() {
        return size;
    }
}
