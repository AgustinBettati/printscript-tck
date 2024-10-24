package interpreter;

import common.argument.InputProvider;
import input.InputHandler;

public class InputAdapter implements InputHandler {
  private final InputProvider inputProvider;

  public InputAdapter(InputProvider inputProvider) {
    this.inputProvider = inputProvider;
  }

  @Override
  public String getInput() {
    return inputProvider.input("input");
  }
}
