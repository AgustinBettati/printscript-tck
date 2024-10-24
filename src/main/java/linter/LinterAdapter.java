package linter;

import common.argument.ErrorHandler;
import common.convertors.StringConvertor;
import runner.Runner;

import java.io.InputStream;

public class LinterAdapter implements PrintScriptLinter {
  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
    Runner runner = new Runner();
    String linterConfig = StringConvertor.convertLinterConfig(config);
    OutputReport output = new OutputReport();

    runner.analyze(src, version, linterConfig, output);

    for (String error : output.getErrors()) {
      handler.reportError(error);
    }
  }
}
