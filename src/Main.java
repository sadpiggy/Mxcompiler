import ASM.AsmOperand.AsmRoot;
//import ASM.InstSelector;
import ASM.ConflictAnalise;
import ASM.InstSelector;
import AST.AstBuilder;
import AST.ProgramNode;
import FrontEnd.SemanticChecker;
import FrontEnd.SymbolCollector;
//import Mutil.GlobalScope;
import IR.IrFirstPass;
import Mutil.MxstarErrorListener;
import Mutil.Scope;
import Mutil.error.Errormy;
import Parser.MxstarLexer;
import Parser.MxstarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;
import java.util.Objects;

public class Main{
    public static void main(String[] args) throws Exception{
        boolean isLocal = false;
        //whatever
        //hao xiang
        boolean semanticOnly = args.length > 0 && args[0].equals("--semantic_only");
        String name = "testcases/testcase/myTest.mx";
        InputStream input = null;
        if (isLocal) input = new FileInputStream(name);
        else input = System.in;
        try {
            MxstarLexer lexer = new MxstarLexer(CharStreams.fromStream(input));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new MxstarErrorListener());
            MxstarParser parser = new MxstarParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new MxstarErrorListener());
            ParseTree parseTreeRoot = parser.program();
            //生成抽象语法树
            AstBuilder astBuilder = new AstBuilder();
            ProgramNode astRoot = (ProgramNode) astBuilder.visit(parseTreeRoot);
            Scope globalScope = new Scope(null);
            //semantic checker
            if (semanticOnly){
                SymbolCollector symbolCollector = new SymbolCollector(globalScope);
                astRoot.acceptVisitor(symbolCollector);
                SemanticChecker semanticChecker = new SemanticChecker(globalScope);
                astRoot.acceptVisitor(semanticChecker);
            }

            if (!semanticOnly){
                //ir
                IrFirstPass irFirstPass = new IrFirstPass();
                irFirstPass.run(astRoot);
                //codegen
                AsmRoot asmRoot = new AsmRoot();
                InstSelector instSelector = new InstSelector(irFirstPass,asmRoot);
//           // System.out.println("skdf");
                instSelector.run();
                asmRoot.regsAlloc();
                if (!isLocal)
                System.setOut(new PrintStream(new BufferedOutputStream(
                new FileOutputStream("output.s")),true));
                else System.setOut(new PrintStream(new BufferedOutputStream(
                new FileOutputStream("ravel/build/test.s")),true));

                asmRoot.printAsm(System.out);
                //FEN ZHI!!!
               // irFirstPass.printIr();
                //sdjs
            }

        } catch (Errormy er) {
            System.err.println(er.getString());
            throw new RuntimeException();
        }
    }
}