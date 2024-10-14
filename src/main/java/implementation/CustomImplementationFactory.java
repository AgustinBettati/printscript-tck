package implementation;

import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;

import java.io.BufferedInputStream;
import java.util.Arrays;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new Adapter();
    }

    @Override
    public PrintScriptFormatter formatter() {
        // your PrintScript formatter should be returned here.
        // make sure to ADAPT your formatter to PrintScriptFormatter interface.
        throw new NotImplementedException("Needs implementation"); // TODO: implement
        // Dummy impl: return (src, version, config, writer) -> { };
    }

    @Override
    public PrintScriptLinter linter() {
        return new Adapter();
    }
}