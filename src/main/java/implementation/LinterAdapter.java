package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import org.example.Runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LinterAdapter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
//        StringBuilder configuration = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(config))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                configuration.append(line.replace("\r", " ")).append(System.lineSeparator());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            Runner.lint(src, version, configuration.toString());
//        } catch (Exception e) {
//            handler.reportError(e.toString());
//        }
    }
}
