package implementation;

import interpreter.InputProvider;
import org.jetbrains.annotations.NotNull;

public class AdaptInput {
    InputProvider inputProvider;

    public AdaptInput(InputProvider inputProvider) {
        this.inputProvider = inputProvider;
    }

    public String input(@NotNull String s) {
        return inputProvider.input(s);
    }
}
