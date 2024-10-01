package implementation;

import ast.ASTNode;
import interpreter.*;
import linter.BrokenRule;
import linter.Linter;
import linter.LinterOutput;
import linter.LinterVersion;
import lexer.Lexer;
import org.example.lexer.TokenMapper;
import parser.Parser;
import token.Token;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class Adapter implements PrintScriptInterpreter, PrintScriptLinter {

    private Interpreter interpreter; // Declaración del intérprete como variable de instancia

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        exec(src, version, emitter, handler);
    }

    private void exec(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler) {
        try {
            executeByLine(src, version, emitter, handler);
        } catch (Throwable e) {
            handler.reportError(e.getMessage());
        }
    }

    private void executeByLine(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler) throws IOException {
        var tokenMapper = new TokenMapper("1.1");
        Lexer lexer = new Lexer(tokenMapper);

        // Crear instancia del ListPrinter
        ListPrinter listPrinter = new ListPrinter();
        interpreter = new Interpreter(listPrinter); // Pasar ListPrinter al intérprete

        // Crear una única instancia de PrinterAdapter
        PrinterAdapter adapter = new PrinterAdapter(emitter);

        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.contains("if")) {
                line = handleIf(line, reader);
            }
            List<Token> tokens = lexer.execute(line);
            List<ASTNode> ast = new Parser().execute(tokens);
            String response = Objects.requireNonNull(interpreter.execute(ast.get(0))).toString();

            // Procesar la respuesta con el PrinterAdapter
            splitByLinesAndPrintResponse(adapter, response);
        }

        // Emitir solo el último mensaje almacenado en el ListPrinter
        List<String> messages = listPrinter.getPrintedMessages();
        if (!messages.isEmpty()) {
            String lastMessage = messages.get(messages.size() - 1);  // Obtener el último mensaje
            emitter.print(lastMessage);  // Emitir solo el último mensaje
        }

        // Limpiar mensajes después de emitir
        listPrinter.clearMessages();

        reader.close();
    }





    private static void splitByLinesAndPrintResponse(PrinterAdapter printerAdapter, String response) {
        printerAdapter.print(response); // Imprimir la respuesta completa de una vez
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
        return line + " " + newLine;
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
