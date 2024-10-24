package interpreter;

import common.argument.ErrorHandler;
import common.argument.InputProvider;
import common.argument.PrintEmitter;

import java.io.InputStream;

public interface PrintScriptInterpreter {
    /**
     * executes a PrintScript file handling its resulting messages and errors.
     * @param src Source file.
     * @param version PrintScript version, 1.0 and 1.1 must be supported.
     * @param emitter interface where print statements must be called.
     * @param handler interface where all syntax and semantic error will be reported.
     * @param provider interface that provides input values during the execution.
     */
    void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider);
}
