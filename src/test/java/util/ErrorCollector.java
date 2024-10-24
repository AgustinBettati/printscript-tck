package util;

import common.argument.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

public class ErrorCollector implements ErrorHandler {

    final private List<String> errors = new ArrayList<>();

    @Override
    public void reportError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }
}
