package ASM;

import ASM.AsmInst.*;
import ASM.AsmOperand.*;
import AST.NullContainNode;
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

import java.util.ArrayList;
import java.util.Objects;

public class InstSelector {//构造函数那里有bug
    //sp 16的倍数
    //s0 - 8 - offset
    public IrFirstPass irFirstPass;
    public AsmRoot asmRoot;
    public int specialTag = 0;

    public IrFunc currentIrFunc;
    public IrBlock currentIrBlock;
    public AsmFunc currentAsmFunc;
    public AsmBlock currentAsmBlock;
    public AsmBlock tailBlock;
    public liveValue live = new liveValue(0);
    public int loopDepth = 0;
    //public liveValue oldLive = null;
   // boolean isInLoop = false;
    //boolean oldInLoop = false;

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
    public PhysicalReg s1 = new PhysicalReg("s1","cs1");
    public PhysicalReg s2 = new PhysicalReg("s2","cs2");
    public PhysicalReg s3 = new PhysicalReg("s3","cs3");
    public PhysicalReg s4 = new PhysicalReg("s4","cs4");
    public PhysicalReg s5 = new PhysicalReg("s5","cs5");
    public PhysicalReg s6 = new PhysicalReg("s6","cs6");
    public PhysicalReg s7 = new PhysicalReg("s7","cs7");
    public PhysicalReg s8 = new PhysicalReg("s8","cs8");
    public PhysicalReg s9 = new PhysicalReg("s9","cs9");
    public PhysicalReg s10 = new PhysicalReg("s10","cs10");
    public PhysicalReg s11 = new PhysicalReg("s11","cs11");
    public PhysicalReg t3 = new PhysicalReg("t3","ct3");;
    public PhysicalReg t4 = new PhysicalReg("t4","ct4");;
    public PhysicalReg t5 = new PhysicalReg("t5","ct5");;
    public PhysicalReg t6 = new PhysicalReg("t6","ct6");;
    public ArrayList<PhysicalReg> sRegs;
    public ArrayList<PhysicalReg> atRegs;


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
        sRegs = new ArrayList<>();
        sRegs.add(s1);
        sRegs.add(s2);
        sRegs.add(s3);
        sRegs.add(s4);
        sRegs.add(s5);
        sRegs.add(s6);
        sRegs.add(s7);
        sRegs.add(s8);
        sRegs.add(s9);
        sRegs.add(s10);
        sRegs.add(s11);
        atRegs = new ArrayList<>();
        atRegs.add(a1);
        atRegs.add(a2);
        atRegs.add(a3);
        atRegs.add(a4);
        atRegs.add(a5);
        atRegs.add(a6);
        atRegs.add(a7);
        atRegs.add(t3);
        atRegs.add(t4);
        atRegs.add(t5);
        atRegs.add(t6);
    }



    public PhysicalReg getPhysicalReg(Operand operand,boolean isLeft,liveValue liveEnd,boolean isRs1){
        if (operand instanceof Register){
            Register register = (Register) operand;
            if (isLeft){
                if (!currentAsmFunc.containReg(register.name))
                {
                    if (loopDepth==0)
                    return currentAsmFunc.newPhyReg(register.name);
                    else return currentAsmFunc.newPhyReg(register.name,live);
                }
                if (loopDepth==0)return currentAsmFunc.getPhyReg(register.name, currentAsmFunc.getLiveEnd(),currentAsmFunc.getLiveEnd());
                else return currentAsmFunc.getPhyReg(register.name, currentAsmFunc.getLiveEnd(),live);
            }else {
                return currentAsmFunc.getPhyReg(register.name,liveEnd);
            }
        }else if (operand instanceof IntegerConst){
            IntegerConst integerConst = (IntegerConst) operand;
            PhysicalReg physicalReg ;//= currentAsmFunc.newPhyReg(specialTag++ + "work");
            if (isRs1)physicalReg = t1;
            else physicalReg = t2;
            currentAsmBlock.push_back(new LiInst(currentAsmBlock,physicalReg,new IntegerImm(integerConst.value)));
            return physicalReg;
        }else if (operand instanceof StringConst){
            StringConst stringConst = (StringConst) operand;
            PhysicalReg physicalReg;
            if (isRs1)physicalReg = t1;
            else physicalReg = t2;
            if (isLeft){
                physicalReg = t0;
            }
            currentAsmBlock.push_back(new LuiInst(currentAsmBlock,physicalReg,new RelocationImm(RelocationImm.Type.hi,stringConst.name)));
            currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,physicalReg,physicalReg,new RelocationImm(RelocationImm.Type.lo,stringConst.name)));
            return physicalReg;
        }else if (operand instanceof NullPointerConst){
           return zero;
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
                return physicalReg;
            }
        }
        //System.out.println("106InstSelector 肿么肥四");
        //return zero;
    }

    public void run(){
        for (var it : irFirstPass.stringConsts){
            asmRoot.asmStringContains.add(new AsmGlobalValue(it.name, it.value, it.stringSize,true));
        }
        for (var it : irFirstPass.globalOperands){
            if (Objects.equals(it.typeName, "string")){
                asmRoot.asmGlobalValues.add(new AsmGlobalValue(it.name, it.value, it.alignSize,false));
            }else {
                asmRoot.asmGlobalValues.add(new AsmGlobalValue(it.name, it.alignSize));
            }
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
        currentAsmFunc.stackSize = 0;
        //currentAsmFunc.stackSize = 68;

        int overflowSize = 0;

        if (irFunc.formParams!=null&&irFunc.formParams.size()!=0){
            for (int i=0;i<irFunc.formParams.size();i++){
                if (i<=7){//
                    Register register = irFunc.formParams.get(i);
                    //System.out.println(register.name);
                    currentAsmFunc.setPhyReg(new PhysicalReg("a"+i, register.name), currentAsmFunc.getLiveEnd(), currentAsmFunc.getLiveEnd());
                   // System.out.println(currentAsmFunc.getPhyReg(register.name,new liveValue(0)));
                }else {
                    Register register = irFunc.formParams.get(i);
                    overflowSize+=4;
                    currentAsmFunc.setPhyReg(new PhysicalReg(register.name,-overflowSize), currentAsmFunc.getLiveEnd(),currentAsmFunc.getLiveEnd());
                    currentAsmFunc.changeStackSize();
                }

            }
        }

        currentAsmBlock = new AsmBlock(irFunc.name+"_head",currentAsmFunc );
        currentAsmFunc.headSpInst = new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,sp,sp,null);
        currentAsmBlock.push_front(currentAsmFunc.headSpInst);
        currentAsmBlock.push_front(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,s0,sp,new IntegerImm(0)));
        int s0Address = -currentAsmFunc.stackSize-8;
        int raAddress = -currentAsmFunc.stackSize-4;
        currentAsmBlock.push_front(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new IntegerImm(s0Address),s0,sp));
        currentAsmBlock.push_front(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new IntegerImm(raAddress),ra,sp));
        currentAsmFunc.stackSize+=8;

        boolean should_save = true;

        tailBlock = new AsmBlock(irFunc.name+"_tail",currentAsmFunc);

        //Register paramin_this = new Register();
        boolean put_head = true;

        for (var it : irFunc.blocks){
            if (put_head){
                put_head = false;
                currentAsmFunc.setBlock(currentAsmBlock);
                visitIrBlock(it);
            }else {
                currentAsmBlock = new AsmBlock(it.getBlockLabel(),currentAsmFunc);
                currentAsmFunc.setBlock(currentAsmBlock);
                visitIrBlock(it);
            }
        }
        currentAsmBlock = tailBlock;
        //返回值这里可能要处理一下
        currentAsmFunc.tailSpInst = new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,sp,sp,null);
        currentAsmBlock.push_back(currentAsmFunc.tailSpInst);
        currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,s0,sp,new IntegerImm(s0Address)));
        currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,ra,sp,new IntegerImm(raAddress)));

        currentAsmBlock.push_back(new ASM.AsmInst.RetInst(currentAsmBlock));
        currentAsmFunc.setBlock(tailBlock);
    }

    public void visitIrBlock(IrBlock irBlock){
        for(var it : irBlock.insts){
            if (it.isLoopBegin){
                liveValue newLive = new liveValue(0);
                newLive.notChange = true;
                newLive.oldLive = live;
                live = newLive;
                loopDepth++;
            }
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
        Register register = (Register) inst.destReg;
        currentAsmFunc.setPhyReg(new PhysicalReg(register.name), currentAsmFunc.getLiveEnd(), currentAsmFunc.getLiveEnd());
    }

    public void visitBinaryInst(BinaryInst inst) {
        currentAsmFunc.changeStackSize();//这个是干嘛的？ todo
        Register destReg = (Register) inst.destReg;
        PhysicalReg rd = null;//getPhysicalReg(destReg,true,0,false);
        PhysicalReg value1 = null;//getPhysicalReg(inst.value1, false,rd.liveStart, true);
        PhysicalReg value2 = null;//getPhysicalReg(inst.value2, false, rd.liveStart, false);
        if (loopDepth==0){
            rd = getPhysicalReg(destReg,true,new liveValue(0),false);
            value1 = getPhysicalReg(inst.value1, false, currentAsmFunc.getLiveEnd(), true);
            value2 = getPhysicalReg(inst.value2, false, currentAsmFunc.getLiveEnd(), false);
        }else {
            rd = getPhysicalReg(destReg,true,live,false);
            value1 = getPhysicalReg(inst.value1, false,live, true);
            value2 = getPhysicalReg(inst.value2, false, live, false);
        }

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
        PhysicalReg dest = null;//getPhysicalReg(inst.destReg, true,0,false);
        PhysicalReg value = null;//getPhysicalReg(inst.value, false,dest.liveStart,true);
        if (loopDepth==0){
            dest = getPhysicalReg(inst.destReg, true,new liveValue(0),false);
            value = getPhysicalReg(inst.value, false,dest.liveStart.getLast(),true);
        }else {
            dest = getPhysicalReg(inst.destReg, true,live,false);
            value = getPhysicalReg(inst.value, false,live,true);
        }
        //if (value==null)System.out.println("nmsl");
        currentAsmBlock.push_back(new MoveInst(currentAsmBlock,dest,value));
    }

    public void visitBrInst(BrInst inst) {

        if (inst.condition==null){
            currentAsmBlock.push_back(new JInst(currentAsmBlock,inst.trueBranch.getName()));
        }else {
            //这个很可能有问题
            if(loopDepth==0)currentAsmBlock.push_back(new ASM.AsmInst.BrInst(currentAsmBlock, ASM.AsmInst.BrInst.BrTypeOp.beqz,getPhysicalReg(inst.condition,false, currentAsmFunc.getLiveEnd(), true),inst.falseBranch.getName()));
            else currentAsmBlock.push_back(new ASM.AsmInst.BrInst(currentAsmBlock, ASM.AsmInst.BrInst.BrTypeOp.beqz,getPhysicalReg(inst.condition,false, live, true),inst.falseBranch.getName()));
            currentAsmBlock.push_back(new JInst(currentAsmBlock,inst.trueBranch.getName()));
        }

        if (inst.isLoopEnd){
            loopDepth--;
            live.value = currentAsmFunc.getLiveEnd().value;
            live.notChange = false;

            live = live.oldLive;

        }

    }

    public void visitCallInst(CallInst inst) {

        for (int i=0;i<=10;i++){
            currentAsmFunc.newPhyReg(atRegs.get(i).phyType,atRegs.get(i).phyType);
        }

        int overflowSize = 0;

        if (inst.params!=null&&inst.params.size()!=0){
            for (int i=0;i<inst.params.size();i++){
                if (i<=7){
                    Operand operand = inst.params.get(i);
                    PhysicalReg physicalReg = null;
                    if (loopDepth==0)physicalReg = getPhysicalReg(operand,false,currentAsmFunc.getLiveEnd(),true);//
                    else physicalReg = getPhysicalReg(operand,false,live,true);
                    currentAsmBlock.push_back(new MoveInst(currentAsmBlock,new PhysicalReg("a"+i,"cnm"),physicalReg));
//                   if (i!=0){
//                       PhysicalReg fakeReg = new PhysicalReg("fake"+i,atRegs.get(i-1).phyType);//fakeReg.hadAlloc = true;
//                       //fakeReg.phyType = atRegs.get(i-1).phyType;
//                       currentAsmFunc.setPhyReg(fakeReg, currentAsmFunc.getLiveEnd(), currentAsmFunc.getLiveEnd());
//                   }
                }else {
                    Operand operand = inst.params.get(i);
                    PhysicalReg physicalReg = null;//getPhysicalReg(operand,false,currentAsmFunc.getLiveEnd(),true);//
                    if (loopDepth==0)physicalReg = getPhysicalReg(operand,false,currentAsmFunc.getLiveEnd(),true);//
                    else physicalReg = getPhysicalReg(operand,false,live,true);//
                    overflowSize+=4;
                    currentAsmBlock.push_back(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new IntegerImm(-overflowSize),physicalReg,sp));
                }
            }
        }
        currentAsmBlock.push_back(new ASM.AsmInst.CallInst(currentAsmBlock,inst.callee.name));


        if (inst.destReg!=null){
            Register register = (Register) inst.destReg;
            PhysicalReg physicalReg = getPhysicalReg(register,true, currentAsmFunc.getLiveEnd(), false);
            currentAsmBlock.push_back(new MoveInst(currentAsmBlock,physicalReg,a0));
            //if (a0==null)System.out.println("nmsl");
        }


    }

    public void visitGetElementPtrInst(GetElementPtrInst inst) {
        PhysicalReg dest = getPhysicalReg(inst.destReg,true,live,false);


        if (inst.indexes.size()==2){//结构体
            Operand operand = inst.indexes.get(1);
            IntegerConst integerConst = (IntegerConst) operand;
            LlvmStructType llvmStructType = (LlvmStructType) ((LlvmPointerType) inst.pointer.type).pointeeType;
            int index = llvmStructType.getIndexAlign(integerConst.value);
            //System.out.println(index);
            PhysicalReg pointer = null;//getPhysicalReg(inst.pointer,false,dest.liveStart,true);
            if (loopDepth==0)pointer = getPhysicalReg(inst.pointer,false,dest.liveStart.getLast(),true);
            else pointer = getPhysicalReg(inst.pointer,false,live,true);
            currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,dest,pointer,new IntegerImm(index)));
        }else {//数组
            Operand operand = inst.indexes.get(0);
            PhysicalReg index = null;//getPhysicalReg(operand,false,dest.liveStart,true);
            PhysicalReg pointer = null;//getPhysicalReg(inst.pointer,false,dest.liveStart,false);//t2
            if (loopDepth==0){
                index = getPhysicalReg(operand,false,dest.liveStart.getLast(),true);
                pointer = getPhysicalReg(inst.pointer,false,dest.liveStart.getLast(),false);//t2
            }else {
                index = getPhysicalReg(operand,false,live,true);
                pointer = getPhysicalReg(inst.pointer,false,live,false);//t2
            }
            currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,t2,zero,new IntegerImm(4)));
            currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.mul,index,index,t2));
            currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.add,dest,index,pointer));
        }


    }

    public void visitIcmpInst(IcmpInst inst) {
        PhysicalReg dest = null;
        PhysicalReg value1 = null;
        PhysicalReg value2 = null;
        if (loopDepth!=0){
            dest = getPhysicalReg(inst.destReg,true,live,false);
            value1 = getPhysicalReg(inst.value1,false,live,true);
            value2 = getPhysicalReg(inst.value2, false,live,false);
        }else {
            dest = getPhysicalReg(inst.destReg, true,currentAsmFunc.getLiveEnd(),false);
            value1 = getPhysicalReg(inst.value1, false,dest.liveStart.getLast(),true);
            value2 = getPhysicalReg(inst.value2, false,dest.liveStart.getLast(),false);
        }

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
                currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.slt,dest,value2,value1,true));
                break;
            }
            case sle:{
                currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,value1,value1,new IntegerImm(-1)));
                currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.slt,dest,value1,value2));
                break;
            }
            case sge:{
                currentAsmBlock.push_back(new ITypeInst(currentAsmBlock, ITypeInst.ITypeOp.addi,value1,value1,new IntegerImm(1)));
                currentAsmBlock.push_back(new RTypeInst(currentAsmBlock, RTypeInst.RTypeOp.slt,dest,value2,value1,true));
            }
        }
    }

    public void visitLoadInst(LoadInst inst) {
        PhysicalReg dest = null;
        PhysicalReg src = null;
        if (loopDepth!=0){
            dest = getPhysicalReg(inst.destReg,true,live,false);
            src = getPhysicalReg(inst.pointer,false,live,true);
        }else {
            dest = getPhysicalReg(inst.destReg, true,currentAsmFunc.getLiveEnd(),false);
            src = getPhysicalReg(inst.pointer,false,currentAsmFunc.getLiveEnd(),true);
        }


        if (inst.pointer.isAlloc){
            currentAsmBlock.push_back(new ASM.AsmInst.MoveInst(currentAsmBlock,dest,src));
        }else {
            if (inst.pointer instanceof GlobalOperand){
                currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,dest,src,new RelocationImm(RelocationImm.Type.lo,((GlobalOperand) inst.pointer).name)));
            }else if (src.isAddress){
                currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,dest,s0,new IntegerImm(src.offset)));
            }else {
                currentAsmBlock.push_back(new ASM.AsmInst.LoadInst(currentAsmBlock, ASM.AsmInst.LoadInst.LoadTypeOp.lw,dest,src,new IntegerImm(0)));
            }
        }

    }

    public void visitRetInst(RetInst inst) {

        if (inst.value!=null){
           // PhysicalReg value = getPhysicalReg(inst.value,false,currentAsmFunc.getLiveEnd(),true);
            PhysicalReg value = null;
            if (loopDepth!=0){
                value = getPhysicalReg(inst.value,false,live,true);
            }else {
                value = getPhysicalReg(inst.value,false,currentAsmFunc.getLiveEnd(),true);
            }


            currentAsmBlock.push_back(new MoveInst(currentAsmBlock,a0,value));
        }
        currentAsmBlock.push_back(new JInst(currentAsmBlock, ".L"+tailBlock.name));
    }

    public void visitSextInst(SextInst inst) {
        PhysicalReg dest = null;
        PhysicalReg value = null;
        if (loopDepth!=0){
            dest = getPhysicalReg(inst.destReg,true,live,false);
            value = getPhysicalReg(inst.value,false,live,true);
        }else {
            dest = getPhysicalReg(inst.destReg,true,new liveValue(0),false);
            value = getPhysicalReg(inst.value,false,dest.liveStart.getLast(),true);
        }

        //if (value==null)System.out.println("nmsl");
        currentAsmBlock.push_back(new MoveInst(currentAsmBlock,dest,value));
    }

    public void visitStoreInst(StoreInst inst) {

        PhysicalReg pointer = null;//= getPhysicalReg(inst.pointer,false,currentAsmFunc.getLiveEnd(),true);
        PhysicalReg value = null;//getPhysicalReg(inst.value,false,currentAsmFunc.getLiveEnd(),false);
        if (loopDepth!=0){
            pointer = getPhysicalReg(inst.pointer,false,live,false);
            value = getPhysicalReg(inst.value,false,live,true);
        }else {
            pointer = getPhysicalReg(inst.pointer,false,currentAsmFunc.getLiveEnd(),false);
            value = getPhysicalReg(inst.value,false,currentAsmFunc.getLiveEnd(),true);
        }

        if (value==null)System.out.println(inst.value);

        //破掉ssa,所以liveBegin会有多个了，用链表

        if (inst.pointer.isAlloc){
            currentAsmBlock.push_back(new ASM.AsmInst.MoveInst(currentAsmBlock,pointer,value));
        }
        else {
            if (inst.pointer instanceof GlobalOperand){
                currentAsmBlock.push_back(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new RelocationImm(RelocationImm.Type.lo,((GlobalOperand) inst.pointer).name),value,pointer));
            }else if (pointer.isAddress){
                currentAsmBlock.push_back(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new IntegerImm(pointer.offset),value,s0));
            }else {
                currentAsmBlock.push_back(new ASM.AsmInst.StoreInst(currentAsmBlock, ASM.AsmInst.StoreInst.StoreTypeOp.sw,new IntegerImm(0),value,pointer));
            }
        }

        //有的不能转

    }

}
