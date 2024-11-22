package interpreter;

import interpreter.response.ErrorResponse;
import interpreter.response.InterpreterResponse;
import interpreter.response.SuccessResponse;
import lexer.Lexer;
import lexer.LexerFactory;
import parser.Parser;
import parser.ParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class PrintScriptInterpreterImplementation implements PrintScriptInterpreter{
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Interpreter interpreter = InterpreterFactory.interpreterVersion(new Administrator(), version);
        Parser parser = ParserFactory.parserVersion(version);
        Lexer lexer = LexerFactory.lexerVersion(version);

        String input = provider != null ? provider.input("") : null;
        if (input != null && !input.isBlank()) {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
        }

        try {
            InterpreterResponse response = interpreter.interpretAST(parser.generateAST(lexer.makeTokens(src)));
            if (response instanceof SuccessResponse){
                while (!interpreter.getAdmin().getPrintedElements().isEmpty()){
                    emitter.print(interpreter.getAdmin().getPrintedElements().poll());
                }
            }else{
                handler.reportError(((ErrorResponse)response).message());
            }
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}
