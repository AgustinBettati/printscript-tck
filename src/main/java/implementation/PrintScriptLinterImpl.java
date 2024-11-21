package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import lexer.Lexer;
import linter.Linter;
import linter.LinterResult;
import nodes.StatementType;
import parser.Parser;
import rules.LinterRules;
import token.Token;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class PrintScriptLinterImpl implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        Adapter adapter = new Adapter();
        LinterRules rules;
        rules = adapter.getLinterRules(config, version);
        Iterator<Token> tokens = new Lexer(src, version);
        Iterator<StatementType> asts = new Parser(tokens, version, null);
        Linter linter = new Linter(rules);
        LinterResult result = linter.lint(asts);

        if (result.isValid()) {
            return;
        }

        List<LinterResult> errors = linter.getErrors();
        for (LinterResult error : errors) {
            handler.reportError(error.getMessage());
        }
    }
}
