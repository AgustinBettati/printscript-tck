package implementation;
import formatter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import linter.PrintScriptLinter;

public interface PrintScriptFactory {
    PrintScriptInterpreter interpreter();
    PrintScriptFormatter formatter();
    PrintScriptLinter linter();
}
