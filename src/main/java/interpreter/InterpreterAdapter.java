package interpreter;

import common.argument.ErrorHandler;
import common.argument.InputProvider;
import common.argument.PrintEmitter;
import input.InputHandler;
import interpreter.engine.staticprovider.EnvLoader;
import runner.Runner;
import java.io.InputStream;

public class InterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        String bestFootballClub = System.getenv("BEST_FOOTBALL_CLUB");
        EnvLoader.addNewConstants("BEST_FOOTBALL_CLUB", bestFootballClub);

        Runner runner = new Runner();

        ErrorReport errorLog = new ErrorReport("");
        OutputEmitter printLog = new OutputEmitter(emitter);
        InputHandler inputHandler = new InputAdapter(provider);

        runner.execute(src, version, printLog, errorLog, inputHandler);

        if (!errorLog.getResult().isEmpty()){
            handler.reportError(errorLog.getResult());
        }
    }
}
