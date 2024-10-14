package implementation;

import ast.ASTNode;
import interpreter.*;
import interpreter.Reader;
import linter.BrokenRule;
import linter.Linter;
import linter.LinterOutput;
import linter.LinterVersion;
import lexer.Lexer;
import org.example.lexer.TokenMapper;
import org.jetbrains.annotations.NotNull;
import parser.Parser;
import token.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class Adapter implements PrintScriptInterpreter, PrintScriptLinter {

    private Interpreter interpreter; // Declaración del intérprete como variable de instancia

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        exec(src, version, emitter, handler, provider);
    }

    private void exec(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            executeByLine(src, version, emitter, handler, provider);
        } catch (Throwable e) {
            handler.reportError(e.getMessage());
        }
    }

    private void executeByLine(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) throws IOException {
         Reader inputReader = new Reader() {
            @Override
            public @NotNull String input(@NotNull String s) {
                return provider.input(s);
            }
        };

        var tokenMapper = new TokenMapper(version);
        Lexer lexer = new Lexer(tokenMapper);

        // Crear instancia del ListPrinter
        ListPrinter listPrinter = new ListPrinter();
        interpreter = new Interpreter(listPrinter, inputReader); // Pasar ListPrinter al intérprete

        // Crear una única instancia de PrinterAdapter
        PrinterAdapter adapter = new PrinterAdapter(emitter);

        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
        String line;
        String statement = "";

        while ((line = reader.readLine()) != null) {
            statement = line;
            if(statement.contains("if")) statement = handleIf(statement, reader);

            List<Token> tokens = lexer.execute(statement);
            List<ASTNode> ast = new Parser().execute(tokens);

            Object result = interpreter.execute(ast.get(0));
            if (result != null) {
                String response = result.toString();
                // Procesar la respuesta con el PrinterAdapter
                splitByLinesAndPrintResponse(adapter, response);
            }
        }
        // Emitir todos los mensajes acumulados en ListPrinter
        List<String> messages = listPrinter.getPrintedMessages();
        if (!messages.isEmpty()) {
            for (String message : messages) {
                emitter.print(message);  // Emitir cada mensaje almacenado
            }
        }

        // Limpiar mensajes después de emitir
        listPrinter.clearMessages();

        reader.close();
    }

    private String handleIf(String statement, BufferedReader reader) throws IOException {
        StringBuilder result = new StringBuilder(statement);
        Integer llaves = 1;
        String line;
        while (llaves>0){
            if((line=reader.readLine())!=null){
                for (char ch: line.toCharArray()){
                    if (ch=='{'){
                        llaves++;
                    } else if (ch=='}'){
                        llaves--;
                    }
                }
                result.append(line);
            }
        }
        return result.toString();
    }





    private static void splitByLinesAndPrintResponse(PrinterAdapter printerAdapter, String response) {
        String[] lines = response.split("\n");  // Dividir en líneas
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                printerAdapter.print(line);  // Imprimir cada línea
            }
        }
    }



    // Implementación de PrintScriptLinter
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        try {
            LinterVersion linterVersion = LinterVersion.Companion.fromString(version);
            if (linterVersion == null) {
                throw new IllegalArgumentException("Unsupported version: " + version);
            }

            Linter linter = new Linter(linterVersion);

            String configContent = convertInputStreamToString(config);
            linter.readJson(configContent);

            BufferedReader reader = new BufferedReader(new InputStreamReader(src));
            String line;
            Lexer lexer = new Lexer(new TokenMapper("1.1"));
            Parser parser = new Parser();

            while ((line = reader.readLine()) != null) {
                List<Token> tokens = lexer.execute(line);
                List<ASTNode> ast = parser.execute(tokens);
                LinterOutput output = linter.check(ast);
                List<BrokenRule> brokenRules = output.getBrokenRules();

                for (BrokenRule brokenRule : brokenRules) {
                    handler.reportError(brokenRule.getRuleDescription());
                }
            }
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }
}
