import AST.AstBuilder;
import AST.ProgramNode;
import FrontEnd.SemanticChecker;
import FrontEnd.SymbolCollector;
import Mutil.GlobalScope;
import Mutil.MxstarErrorListener;
import Mutil.error.Errormy;
import Parser.MxstarLexer;
import Parser.MxstarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;

//I should have known, it's better for me to choose to make one smaller and clearer, but even if i don't like my code which is smelled like
//bullshit. I should not give up my code. I should make him better just like my shit(run :-)
public class Main{
    public static void main(String[] args) throws Exception{
        //String name = "testcases/testcase/sema/basic-package/basic-30.mx";
        //InputStream input = new FileInputStream(name);
        InputStream input = System.in;
        try {
            MxstarLexer lexer = new MxstarLexer(CharStreams.fromStream(input));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new MxstarErrorListener());
            MxstarParser parser = new MxstarParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new MxstarErrorListener());
            ParseTree parseTreeRoot = parser.program();

            AstBuilder astBuilder = new AstBuilder();
            ProgramNode astRoot = (ProgramNode) astBuilder.visit(parseTreeRoot);
            GlobalScope globalScope = new GlobalScope(null);
            SymbolCollector symbolCollector = new SymbolCollector(globalScope);
            astRoot.acceptVisitor(symbolCollector);
            SemanticChecker semanticChecker = new SemanticChecker(globalScope);
            astRoot.acceptVisitor(semanticChecker);
        } catch (Errormy er) {
            System.err.println(er.getString());
            throw new RuntimeException();
        }
    }
}