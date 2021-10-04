import Mutil.MxstarErrorListener;
import Parser.MxstarLexer;
import Parser.MxstarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;

public class Main{
    public static void main(String[] args) throws Exception{
        String name = "testcases/test1";
        InputStream input = new FileInputStream(name);
        try {
           MxstarLexer lexer = new MxstarLexer(CharStreams.fromStream(input));
            //lexer.removeErrorListeners();
            //lexer.addErrorListener(new MxstarErrorListener());
            MxstarParser parser = new MxstarParser(new CommonTokenStream(lexer));
            //parser.removeErrorListeners();
            //parser.addErrorListener(new MxstarErrorListener());
            ParseTree parseTreeRoot = parser.program();
        } catch (Error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}