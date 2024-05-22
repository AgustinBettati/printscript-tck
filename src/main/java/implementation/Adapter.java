package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.example.ast.nodes.ProgramNode;
import org.example.interpreter.Interpreter;
import org.example.interpreter.InterpreterImpl;
import org.example.lexer.Lexer;
import org.example.lexer.LexerImpl;
import org.example.parser.ParserImpl;
import org.example.token.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Adapter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Lexer lexer = new LexerImpl(version);
            Interpreter interpreter = new InterpreterImpl();
            ParserImpl parser = new ParserImpl();
            String fileInString = this.getString(src);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            List<String> separatedByConditionals = new ArrayList<>(Arrays.stream(fileInString.split("if\\(")).toList());
            for (int i = 1; i < separatedByConditionals.size(); i++) {
                separatedByConditionals.set(i, "if(" + separatedByConditionals.get(i));
            }
            if (separatedByConditionals.size() <= 1) {
                executeByLine(fileInString, version, emitter, handler, provider);
            } else {
                for (String branch : separatedByConditionals) {
                    List<Token> tokens = lexer.tokenize(branch);
                    ProgramNode ast = parser.parse(tokens);
                    PrintStream ps = new PrintStream(baos);
                    PrintStream oldOut = System.out;
                    System.setOut(ps);
                    interpreter.interpret(ast);
                    System.setOut(oldOut);
                    String response = baos.toString().replace("\r","").trim();
                    if (!response.isBlank()) {
                        Arrays.stream(response.split("\n")).forEach(emitter::print);
                    }
                }
            }
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    private void executeByLine(String src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Interpreter interpreter = new InterpreterImpl();
            Lexer lexer = new LexerImpl(version);
            List<Token> tokens = lexer.tokenize(src);
            ParserImpl parser = new ParserImpl();
            ProgramNode ast = parser.parse(tokens);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream oldOut = System.out;
            System.setOut(ps);
            interpreter.interpret(ast);
            System.setOut(oldOut);
            String response = baos.toString().trim();
            if (!response.isBlank()) emitter.print(response);
        } catch (Exception | Error e) {
            handler.reportError(e.getMessage());
        }
    }


    private String getString(InputStream src) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        reader.close();
        return sb.toString();
    }


}
