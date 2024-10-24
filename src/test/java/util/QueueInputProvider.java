package util;

import common.argument.InputProvider;

import java.util.Queue;

public class QueueInputProvider implements InputProvider {

    final private Queue<String> messages;

    public QueueInputProvider(Queue<String> messages) {
        this.messages = messages;
    }

    public String input(String name) {
        return messages.poll();
    }
}
