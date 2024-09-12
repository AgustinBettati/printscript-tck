package interpreter;

import org.example.*;
import org.example.lexerresult.LexerSuccess;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Linter implements PrintScriptLinter{

  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
    try {
      StaticCodeAnalyzer linter = new PrintScriptSca(config);
      Scanner scanner = new Scanner(src).useDelimiter("\n");
      Lexer lexer = new PrintScriptLexer(version);
      while (scanner.hasNext()) {
        Result lexerResult = lexer.lex(scanner);
        if (!lexerResult.isSuccessful()) {
          handler.reportError(lexerResult.errorMessage());
        }
          LexerSuccess success = (LexerSuccess) lexerResult;
          List<Result> results = linter.analyze(success.getTokens());
          for (Result result : results) {
              if (result.isSuccessful()) continue;
              handler.reportError(result.errorMessage());
          }
      }

    } catch (IOException e) {
        handler.reportError("Error reading file");
    }
  }

  private String streamToString(InputStream stream) throws IOException {
    StringBuilder build = new StringBuilder();
    byte[] buf = new byte[1024];
    int length;
    try (InputStream is = stream) {
      while ((length = is.read(buf)) != -1) {
        build.append(new String(buf, 0, length));
      }
	}
    return build.toString();
  }
}
