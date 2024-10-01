package implementation;

import interpreter.PrintEmitter;
import interpreter.Printer;
import java.util.List;

public class PrinterAdapter implements Printer {
    private final PrintEmitter emitter;  // Instancia de PrintEmitter (puede ser PrintCollector o PrintCounter)
    private final ListPrinter listPrinter;

    public PrinterAdapter(PrintEmitter emitter) {
        this.emitter = emitter;
        this.listPrinter = new ListPrinter();  // Usa tu implementación de ListPrinter
    }
    @Override
    public void print(String message) {
        listPrinter.print(message);  // Almacena los mensajes en la lista
    }

    public void emitAllMessages() {
        List<String> messages = listPrinter.getPrintedMessages();
        for (String message : messages) {
            if (!message.equals("kotlin.Unit")) {  // Avoid emitting 'Unit'
                emitter.print(message);
            }
        }
        listPrinter.clearMessages(); // Clear messages after emitting
    }

    public List<String> getStoredMessages() {
        return listPrinter.getPrintedMessages();  // Devuelve los mensajes almacenados
    }
}

