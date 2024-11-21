package implementation;

import formatter.Formatter;
import interpreter.PrintScriptFormatter;
import lexer.Lexer;
import nodes.StatementType;
import parser.Parser;
import rules.FormattingRules;
import token.Token;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Iterator;

public class PrintScriptFormatterImpl implements PrintScriptFormatter {

    Adapter adapter = new Adapter();

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        FormattingRules rules = adapter.adaptFormattingConfig(config);
        try {
            Iterator<Token> tokens = new Lexer(src, version);
            Iterator<StatementType> asts = new Parser(tokens, version, null);
            String formattedFile = Formatter.INSTANCE.format(asts, rules, version);
            writer.write(formattedFile.stripTrailing());
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Error closing writer: " + e.getMessage());
            }
        }
    }
}
