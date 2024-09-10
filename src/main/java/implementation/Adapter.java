//package implementation;
//import interpreter.PrintScriptInterpreter;
//import interpreter.*;
//import java.io.*;
//import java.util.Arrays;
//import java.util.List;
//
//class Adapter implements PrintScriptInterpreter {
//    @Override
//    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
//        exec(src, version, emitter, handler, provider);
//    }
//
//    private void exec(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
//        try {
//            executeByLine(src,version, emitter, handler, provider);
//        }
//        catch (Exception e) {
//            handler.reportError(e.getMessage());
//        }
//    }
//
//    private void executeByLine(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
//        try{
//            Lexer lexer = new Lexer(version, new StrategyMapper());
//            Interpreter interpreter = new Interpreter(getInputReaderType(provider));
//            BufferedReader reader = new BufferedReader(new InputStreamReader(src));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.contains("if")) {
//                    line = handleIf(line, reader);
//                }
//                List<Token> tokens = lexer.execute(line);
//                List<AbstractSyntaxTree> ast = new Parser().execute(tokens);
//                String response = interpreter.execute(ast).getString();
//                splitByLinesAndPrintResponse(emitter, response);
//            }
//            reader.close();
//        }
//        catch (Exception | Error e) {
//            handler.reportError(e.getMessage());
//        }
//    }
//
//    private static void splitByLinesAndPrintResponse(PrintEmitter emitter, String response) {
//        List<String> splitResponse = Arrays.stream(response.split("\n")).toList();
//        splitResponse.forEach((self)->{
//            if (!self.isBlank()) emitter.print(self);
//        });
//    }
//
//    private String handleIf(String line, BufferedReader reader) throws IOException {
//        while (!line.contains("}")) {
//            line += reader.readLine();
//        }
//        String newLine = reader.readLine();
//
//        if(newLine.contains("else")) {
//            while (!newLine.contains("}")) {
//                newLine += reader.readLine();
//            }
//        }
//        line += " " + newLine + " ";
//        return line;
//    }
//
//    private InputProvider getInputReaderType(InputProvider provider){
//        return (InputProvider) new AdaptInput(provider);
//    }
//
//}
