package ASM.AsmOperand;

public class AsmGlobalValue extends asmOperand{
    public String name;
    public int size;
    public String stringValue;
    public boolean isString;
    public int printSize;
    public boolean isStringContain;

    public AsmGlobalValue(String name,int size){
        this.name = name;
        this.size = size;
        isString = false;
        if (size==1){
            printSize = 1;
        }else {
            printSize = 4;
        }
        isStringContain = false;
    }

    public AsmGlobalValue(String name,String stringValue,int size,boolean isStringContain){
        this.name = name;
        this.stringValue = stringValue;
        isString = true;
        this.size = size;
        this.isStringContain = isStringContain;
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
        if (isStringContain){
            return "\t"+ ".type "+name+",@object\n" +"\t"+ name+":\n" + "\t"+".asciz	\"" + stringValue +"\"\n" +"\t"+ ".size " + name +", " + size;
        }else {
            if (isString){
                return "\t.globl\t" + name + "\n"
                        + "\t.p2align\t2\n"
                        + name + ":\n"
                        + "\t.word\t" + ".L.str.1" + "\n";
            }else {
                return "\t.globl\t" + name + "\n"
                        + "\t.p2align\t2\n"
                        + name + ":\n"
                        + "\t.word\t" + 0 + "\n";
            }
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
