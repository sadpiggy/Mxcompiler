package ASM;

import ASM.AsmInst.*;
import ASM.AsmOperand.*;
import IR.IrBlock;
import IR.IrFirstPass;
import IR.IrFunc;
import IR.inst.*;
import IR.inst.BrInst;
import IR.inst.CallInst;
import IR.inst.LoadInst;
import IR.inst.RetInst;
import IR.inst.StoreInst;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmStructType;
import IR.operand.*;

public class InstSelector {//构造函数那里有bug
    //sp 16的倍数
    //s0 - 8 - offset
    public IrFirstPass irFirstPass;
    public AsmRoot asmRoot;

    public IrFunc currentIrFunc;
    public IrBlock currentIrBlock;
    public AsmFunc currentAsmFunc;
    public AsmBlock currentAsmBlock;
    public AsmBlock tailBlock;

    public PhysicalReg sp;
    public PhysicalReg ra;
    public PhysicalReg s0;
    public PhysicalReg a0;
    public PhysicalReg a1;
    public PhysicalReg a2;
    public PhysicalReg a3;
    public PhysicalReg a4;
    public PhysicalReg a5;
    public PhysicalReg a6;
    public PhysicalReg a7;
    public PhysicalReg zero;
    public PhysicalReg t0;
    public PhysicalReg t1;
    public PhysicalReg t2;


    public InstSelector(IrFirstPass irFirstPass,AsmRoot asmRoot){
       // System.out.println("kjdhf");
        this.irFirstPass = irFirstPass;
        this.asmRoot = asmRoot;
        if (irFirstPass.stringConsts.size()!=0)asmRoot.hasString=true;
        sp = new PhysicalReg("sp","cnm0");
        ra = new PhysicalReg("ra","cnm1");
        s0 = new PhysicalReg("s0","cnm2");
        a0 = new PhysicalReg("a0","cnm3");
        a1 = new PhysicalReg("a1","cnm4");
        a2 = new PhysicalReg("a2","cnm5");
        a3 = new PhysicalReg("a3","cnm6");
        a4 = new PhysicalReg("a4","cnm7");
        a5 = new PhysicalReg("a5","cnm8");
        a6 = new PhysicalReg("a6","cnm9");
        a7 = new PhysicalReg("a7","cnm10");
        zero = new PhysicalReg("zero","cnm11");
        t0 = new PhysicalReg("t0","cnm12");
        t1 = new PhysicalReg("t1","cnm13");
        t2 = new PhysicalReg("t2","cnm14");
    }



    public PhysicalReg getPhysicalReg(Operand operand,boolean isLeft,int liveEnd,boolean isRs1){
        if (operand instanceof Register){
            Register register = (Register) operand;
            if (isLeft){
                return currentAsmFunc.newPhyReg(register.name);
            }else {
                return currentAsmFunc.getPhyReg(register.name,liveEnd);
            }
        }else if (operand instanceof IntegerConst){
            //System.out.println("jd");
            IntegerConst integerConst = (IntegerConst) operand;
            PhysicalReg physicalReg;
            if (isRs1)physicalReg = t1;
            else physicalReg = t2;
            currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,physicalReg,zero,new IntegerImm(integerConst.value)));
            return physicalReg;
        }else if (operand instanceof StringConst){
            StringConst stringConst = (StringConst) operand;
            PhysicalReg physicalReg;
            if (isRs1)physicalReg = t1;
            else physicalReg = t2;
            currentAsmBlock.push_back(new LuiInst(currentAsmBlock,physicalReg,new RelocationImm(RelocationImm.Type.hi,stringConst.name)));
            currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,physicalReg,physicalReg,new RelocationImm(RelocationImm.Type.lo,stringConst.name)));
            return physicalReg;
        }else {
            GlobalOperand globalOperand = (GlobalOperand) operand;
            if (isLeft){
                currentAsmBlock.push_back(new LuiInst(currentAsmBlock,t0,new RelocationImm(RelocationImm.Type.hi, globalOperand.name)));
                return t0;
            }else {
                PhysicalReg physicalReg;
                if (isRs1)physicalReg = t1;
                else physicalReg = t2;
                currentAsmBlock.push_back(new LuiInst(currentAsmBlock,physicalReg,new RelocationImm(RelocationImm.Type.hi, globalOperand.name)));
                //currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,physicalReg,physicalReg,new RelocationImm(RelocationImm.Type.lo,globalOperand.name)));
                return physicalReg;
            }
        }
        //System.out.println("106InstSelector 肿么肥四");
        //return zero;
    }

    public void run(){
        for (var it : irFirstPass.stringConsts){
            asmRoot.asmGlobalValues.add(new AsmGlobalValue(it.name, it.value, it.stringSize));
        }
        for (var it : irFirstPass.globalOperands){
            asmRoot.asmGlobalValues.add(new AsmGlobalValue(it.name,it.alignSize));
        }
        for (var it : irFirstPass.funcs){
            if (!it.isDeclare){
                if (it.isBuildIn){
                    if (it.isUsed) asmRoot.setFunc(new AsmFunc(it.name,true));
                }else {
                    //System.out.println(it.name);
                    currentAsmFunc = new AsmFunc(it.name,false);
                    asmRoot.setFunc(currentAsmFunc);
                    visitIrFunc(it);
                }
            }
        }
    }


    //参数不能直接溢出，因为 有内建的
    public void visitIrFunc(IrFunc irFunc){
        //s0 - offset
        currentAsmFunc.stackSize = 8;
        if (irFunc.formParams!=null&&irFunc.formParams.size()!=0){
            for (int i=0;i<irFunc.formParams.size();i++){
                Register register = irFunc.formParams.get(i);
                currentAsmFunc.setPhyReg(new PhysicalReg("a"+i, register.name));
            }
        }

        tailBlock = new AsmBlock(irFunc.name+"_tail");

        //Register paramin_this = new Register();

        for (var it : irFunc.blocks){
            currentAsmBlock = new AsmBlock(it.getBlockLabel());
            currentAsmFunc.setBlock(currentAsmBlock);
            visitIrBlock(it);
        }
        currentAsmFunc.setStackSize();
        currentAsmBlock = currentAsmFunc.blocks.get(0);

        currentAsmBlock.push_front(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,sp,sp,new IntegerImm(-currentAsmFunc.stackSize)));
        currentAsmBlock.push_front(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,s0,sp,new IntegerImm(0)));
        currentAsmBlock.push_front(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new IntegerImm(-8),s0,sp));
        currentAsmBlock.push_front(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new IntegerImm(-4),ra,sp));

//        currentAsmBlock = currentAsmFunc.blocks.get(currentAsmFunc.blocks.size()-1);
//        currentAsmBlock.insts.removeLast();
        currentAsmBlock.push_back(new JInst(currentAsmBlock, tailBlock.name));
        currentAsmBlock = tailBlock;
        //返回值这里可能要处理一下
        //currentAsmBlock.push_back(new MoveInst(currentAsmBlock,a0,zero));
        currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,sp,sp,new IntegerImm(currentAsmFunc.stackSize)));
        currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,s0,sp,new IntegerImm(-8)));
        currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,ra,sp,new IntegerImm(-4)));
        currentAsmBlock.push_back(new ASM.AsmInst.RetInst(currentAsmBlock));
        currentAsmFunc.setBlock(tailBlock);
    }

    public void visitIrBlock(IrBlock irBlock){
        for(var it : irBlock.insts){
            //System.out.println(it);
            if (it instanceof AllocInst){
                visitAllocInst((AllocInst) it);
            }else if (it instanceof BinaryInst){
                visitBinaryInst((BinaryInst) it);
            }else if (it instanceof BitcastInst){
                visitBitCastInst((BitcastInst) it);
            }else if (it instanceof BrInst){
                visitBrInst((BrInst) it);
            }else if (it instanceof CallInst){
                visitCallInst((CallInst) it);
            }else if (it instanceof GetElementPtrInst){
                visitGetElementPtrInst((GetElementPtrInst) it);
            }else if (it instanceof IcmpInst){
                visitIcmpInst((IcmpInst) it);
            }else if (it instanceof LoadInst){
                visitLoadInst((LoadInst) it);
            }else if (it instanceof RetInst){
                visitRetInst((RetInst) it);
            }else if (it instanceof SextInst){
                visitSextInst((SextInst) it);
            }else if (it instanceof StoreInst){
                visitStoreInst((StoreInst) it);
            }else {
                System.out.println("cnm in instSelector 82line");
            }
        }
    }


    public void visitAllocInst(AllocInst inst) {
        //System.out.println("kfk");
        Register register = (Register) inst.destReg;
        currentAsmFunc.changeStackSize();
        //currentAsmFunc.stackSize += 4;//inst.alignSize;
        currentAsmFunc.setPhyReg(new PhysicalReg(register.name,-currentAsmFunc.stackSize));
    }

    public void visitBinaryInst(BinaryInst inst) {
        currentAsmFunc.changeStackSize();
        Register destReg = (Register) inst.destReg;
        PhysicalReg rd = getPhysicalReg(destReg,true,0,false);
        PhysicalReg value1 = getPhysicalReg(inst.value1, false,rd.liveStart, true);
        PhysicalReg value2 = getPhysicalReg(inst.value2, false, rd.liveStart, false);

        switch (inst.instOp){
            case add:currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.add,rd,value1,value2));break;
            case sub:currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.sub,rd,value1,value2));break;
            case mul:currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.mul,rd,value1,value2));break;
            case sdiv:currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.div,rd,value1,value2));break;
            case srem:currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.rem,rd,value1,value2));break;
            case shl:currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.sll,rd,value1,value2));break;
            case ashr:currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.sra,rd,value1,value2));break;
            case and:currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.and,rd,value1,value2));break;
            case or:currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.or,rd,value1,value2));break;
            case xor:currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.xor,rd,value1,value2));break;
        }

    }

    public void visitBitCastInst(BitcastInst inst) {
        PhysicalReg dest = getPhysicalReg(inst.destReg, true,0,false);
        PhysicalReg value = getPhysicalReg(inst.value, false,dest.liveStart,true);
        currentAsmBlock.push_back(new MoveInst(currentAsmBlock,dest,value));
//        dest.isVirtual = value.isVirtual;;
//        dest.isAddress = value.isAddress;
//        dest.offset = value.offset;
//        dest.phyType = value.phyType;
    }

    public void visitBrInst(BrInst inst) {
        if (inst.condition==null){
            currentAsmBlock.push_back(new JInst(currentAsmBlock,inst.trueBranch.getName()));
        }else {
            //这个很可能有问题
            currentAsmBlock.push_back(new ASM.AsmInst.BrInst(currentAsmBlock, ASM.AsmInst.BrInst.BrTypeOp.beqz,getPhysicalReg(inst.condition,false, currentAsmFunc.registers.size(), true),inst.falseBranch.getName()));
            currentAsmBlock.push_back(new JInst(currentAsmBlock,inst.trueBranch.getName()));
        }
    }

    public void visitCallInst(CallInst inst) {
        //之后考虑溢出
        if (inst.params!=null&&inst.params.size()!=0){
            for (int i=0;i<inst.params.size();i++){
                Operand operand = inst.params.get(i);
                PhysicalReg physicalReg = getPhysicalReg(operand,false,currentAsmFunc.registers.size(),true);
                currentAsmBlock.push_back(new MoveInst(currentAsmBlock,new PhysicalReg("a"+i,"cnm"),physicalReg));
                //getPhysicalReg(operand,new PhysicalReg("a"+i),false,0);
            }
        }
        currentAsmBlock.push_back(new ASM.AsmInst.CallInst(currentAsmBlock,inst.callee.name));
        if (inst.destReg!=null){
            Register register = (Register) inst.destReg;
            PhysicalReg physicalReg = getPhysicalReg(register,true,0,false);
            currentAsmBlock.push_back(new MoveInst(currentAsmBlock,physicalReg,a0));
        }
    }

    public void visitGetElementPtrInst(GetElementPtrInst inst) {
        PhysicalReg dest = getPhysicalReg(inst.destReg,true,0,false);
        if (inst.indexes.size()==2){//结构体
            Operand operand = inst.indexes.get(1);
            IntegerConst integerConst = (IntegerConst) operand;
            LlvmStructType llvmStructType = (LlvmStructType) ((LlvmPointerType) inst.pointer.type).pointeeType;
            int index = llvmStructType.getIndexAlign(integerConst.value);
            PhysicalReg pointer = getPhysicalReg(inst.pointer,false,dest.liveStart,true);
            currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,dest,pointer,new IntegerImm(index)));
        }else {//数组
            Operand operand = inst.indexes.get(0);
            PhysicalReg index = getPhysicalReg(operand,false,dest.liveStart,true);
            currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,t2,zero,new IntegerImm(4)));
            currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.mul,index,index,t2));
            PhysicalReg pointer = getPhysicalReg(inst.pointer,false,dest.liveStart,false);//t2
            currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.add,dest,index,pointer));
        }
    }

    public void visitIcmpInst(IcmpInst inst) {
        PhysicalReg dest = getPhysicalReg(inst.destReg, true,0,false);
        PhysicalReg value1 = getPhysicalReg(inst.value1, false,dest.liveStart,true);
        PhysicalReg value2 = getPhysicalReg(inst.value2, false,dest.liveStart,false);
        switch (inst.condOp){
            case eq:{
                currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.sub,value1,value1,value2));
                currentAsmBlock.push_back(new SzInst(currentAsmBlock, SzInst.SzTypeOp.seqz,dest,value1));
                break;
            }
            case ne:{
                currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.sub,value1,value1,value2));
                currentAsmBlock.push_back(new SzInst(currentAsmBlock, SzInst.SzTypeOp.snez,dest,value1));
                break;
            }
            case slt:{
                currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.slt,dest,value1,value2));
                break;
            }
            case sgt:{
                currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.slt,dest,value2,value1));
                break;
            }
            case sle:{
                currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,value1,value1,new IntegerImm(-1)));
                currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.slt,dest,value1,value2));
                break;
            }
            case sge:{
                currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,value2,value2,new IntegerImm(-1)));
                currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.slt,dest,value2,value1));
            }
        }
    }

    public void visitLoadInst(LoadInst inst) {
        PhysicalReg dest = getPhysicalReg(inst.destReg, true,0,false);
        PhysicalReg src = getPhysicalReg(inst.pointer,false,0,false);
        if (inst.pointer instanceof GlobalOperand){
            currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,dest,src,new RelocationImm(RelocationImm.Type.lo,((GlobalOperand) inst.pointer).name)));
        }else if (src.isAddress){
            currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,dest,s0,new IntegerImm(src.offset)));
        }else {
            currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,dest,src,new IntegerImm(0)));
        }
    }

    public void visitRetInst(RetInst inst) {
        if (inst.value!=null){
            PhysicalReg value = getPhysicalReg(inst.value,false,currentAsmFunc.registers.size(),true);
            //System.out.println(value);
            currentAsmBlock.push_back(new MoveInst(currentAsmBlock,a0,value));
        }
        //currentAsmBlock.push_back(new ASM.AsmInst.RetInst(currentAsmBlock));
        currentAsmBlock.push_back(new JInst(currentAsmBlock, tailBlock.name));
    }

    public void visitSextInst(SextInst inst) {
        PhysicalReg dest = getPhysicalReg(inst.destReg, true,0,false);
        PhysicalReg value = getPhysicalReg(inst.value, false,dest.liveStart,true);
        currentAsmBlock.push_back(new MoveInst(currentAsmBlock,dest,value));
//        dest.isVirtual = value.isVirtual;;
//        dest.isAddress = value.isAddress;
//        dest.offset = value.offset;
//        dest.phyType = value.phyType;
    }

    public void visitStoreInst(StoreInst inst) {
        PhysicalReg pointer = getPhysicalReg(inst.pointer,false,currentAsmFunc.registers.size(),true);
        PhysicalReg value = getPhysicalReg(inst.value,false,currentAsmFunc.registers.size(),false);
        if (inst.pointer instanceof GlobalOperand){
            currentAsmBlock.push_back(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new RelocationImm(RelocationImm.Type.lo,((GlobalOperand) inst.pointer).name),value,pointer));
        }else if (pointer.isAddress){
            currentAsmBlock.push_back(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new IntegerImm(pointer.offset),value,s0));
        }else {
            currentAsmBlock.push_back(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new IntegerImm(0),value,pointer));
        }
    }

}