package implementation;

import interpreter.Printer;

import java.util.ArrayList;
import java.util.List;

public class ListPrinter implements Printer {
    private List<String> printedMessages;

    public ListPrinter() {
        this.printedMessages = new ArrayList<>();
    }
    @Override
    public void print(String message) {
        printedMessages.add(message);
    }

    public List<String> getPrintedMessages() {
        return new ArrayList<>(printedMessages); // Devolver una copia de la lista
    }

    public void clearMessages() {
        printedMessages.clear();
    }
}
