package linter;

import common.argument.ErrorHandler;

import java.io.InputStream;

public interface PrintScriptLinter {
    /**
     * executes a PrintScript file handling its resulting messages and errors.
     * @param src Source file.
     * @param version PrintScript version, 1.0 and 1.1 must be supported.
     * @param config config file.
     * @param handler interface where all syntax and semantic error will be reported.
     */
    void lint(InputStream src, String version, InputStream config, ErrorHandler handler);
}
