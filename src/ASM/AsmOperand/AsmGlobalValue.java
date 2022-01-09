package ASM.AsmOperand;

public class AsmGlobalValue extends asmOperand{
    public String name;
    public int size;
    public String stringValue;
    public boolean isString;
    public int printSize;

    public AsmGlobalValue(String name,int size){
        this.name = name;
        this.size = size;
        isString = false;
        if (size==1){
            printSize = 1;
        }else {
            printSize = 4;
        }
    }

    public AsmGlobalValue(String name,String stringValue,int size){
        this.name = name;
        this.stringValue = stringValue;
        isString = true;
        this.size = size;
    }

    /*
    type	.L.str,@object          # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"lzcnm"
	.size	.L.str, 6

	.type	b,@object               # @b
	.comm	b,4,4
     */
    @Override
    public String toString() {
        if (isString){
            return ".type "+name+",@object\n" + name+":\n" + ".asciz	\"" + stringValue +"\"\n" + ".size " + name +", " + size;
        }else {
            return ".type "+name+",@object\n" + ".comm " + name + "," + printSize +"," + size;
        }
    }

//    @Override
//    public String toString() {
//        if (isString){
//            return name + ":\n"
//                    + "\t.asciz\t" + "\"" + stringValue + "\"\n";
//        }else {
//            return "\t.globl\t" + name + "\n"
//                    + "\t.p2align\t2\n"
//                    + name + ":\n"
//                    + "\t.word\t" + integerValue + "\n";
//        }
//    }
}
