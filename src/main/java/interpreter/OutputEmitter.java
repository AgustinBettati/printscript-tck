package interpreter;

import common.argument.PrintEmitter;
import output.OutputResult;

import java.util.List;

public class OutputEmitter implements OutputResult<String> {
    private final PrintEmitter printEmitter;

    public OutputEmitter(PrintEmitter printEmitter) {
        this.printEmitter = printEmitter;
    }

    @Override
    public void saveResult(String s) {
        printEmitter.print(s);
    }

    @Override
    public String getResult() {
        return "";
    }

}
