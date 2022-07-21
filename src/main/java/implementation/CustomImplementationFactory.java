package implementation;

import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.

        // Dummy impl: return (src, version, emitter, handler) -> { };
        return new PrintScriptInterpreterImpl();
    }
}