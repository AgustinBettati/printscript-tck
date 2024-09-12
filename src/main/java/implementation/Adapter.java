package implementation;

import ast.ASTNode;
import interpreter.PrintScriptInterpreter;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import org.example.lexer.Lexer;
import org.example.lexer.TokenMapper;
import token.Token;
import parser.Parser;
import interpreter.Interpreter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class Adapter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        exec(src, version, emitter, handler, provider);
    }

    private void exec(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            executeByLine(src, version, emitter, handler);
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    private void executeByLine(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler) {
        try {
            var tokenMapper = new TokenMapper("1.0");
            Lexer lexer = new Lexer(tokenMapper);
            Interpreter interpreter = new Interpreter(); // Proper usage of Interpreter
            BufferedReader reader = new BufferedReader(new InputStreamReader(src));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("if")) {
                    line = handleIf(line, reader);
                }
                List<Token> tokens = lexer.execute(line);
                List<ASTNode> astNodes = new Parser().execute(tokens); // We now have a list of ASTNodes

                // Iterate over the ASTNodes and execute them
                for (ASTNode ast : astNodes) {
                    String response = Objects.requireNonNull(interpreter.execute(ast)).toString();
                    splitByLinesAndPrintResponse(emitter, response);
                }
            }
            reader.close();
        } catch (Exception | Error e) {
            handler.reportError(e.getMessage());
        }
    }


    private static void splitByLinesAndPrintResponse(PrintEmitter emitter, String response) {
        List<String> splitResponse = Arrays.stream(response.split("\n")).toList();
        splitResponse.forEach((self) -> {
            if (!self.isBlank()) emitter.print(self);
        });
    }

    private String handleIf(String line, BufferedReader reader) throws IOException {
        StringBuilder lineBuilder = new StringBuilder(line);
        while (!lineBuilder.toString().contains("}")) {
            lineBuilder.append(reader.readLine());
        }
        line = lineBuilder.toString();
        StringBuilder newLine = new StringBuilder(reader.readLine());

        if (newLine.toString().contains("else")) {
            while (!newLine.toString().contains("}")) {
                newLine.append(reader.readLine());
            }
        }
        line += " " + newLine + " ";
        return line;
    }

    private InputProvider getInputReaderType(InputProvider provider) {
        return (InputProvider) new AdaptInput(provider); // Simplified cast
    }
}
