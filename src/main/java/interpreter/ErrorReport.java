package interpreter;

import output.OutputResult;

public class ErrorReport implements OutputResult<String> {
    private String error;

    public ErrorReport(String error) {
        this.error = error;
    }

    @Override
    public void saveResult(String s) {
        this.error = s;
    }

    @Override
    public String getResult() {
        return error;
    }
}
