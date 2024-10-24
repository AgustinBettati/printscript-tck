package linter;

import output.OutputResult;
import report.Report;

import java.util.ArrayList;
import java.util.List;

public class OutputReport implements OutputResult<Report> {
  private List<String> errorCollector = new ArrayList<>();

  @Override
  public void saveResult(Report s) {
    if (!s.toString().isEmpty()) {
      errorCollector.add(s.toString());
    }
  }

  @Override
  public Report getResult() {
    return null;
  }

  public List<String> getErrors() {
    return errorCollector;
  }
}
