package implementation;

import interpreter.PrintEmitter;
import interpreter.Printer;
import org.jetbrains.annotations.NotNull;

public class EmitterPrinter implements Printer {
    private final PrintEmitter emitter;

    public EmitterPrinter(PrintEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void print(@NotNull String s) {
        emitter.print(s);
    }
}
