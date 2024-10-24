package own;

import org.junit.Test;
import util.MockInputStream;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class TransformTest {
  private static final String MESSAGE = "This is a text";
  private static final String LINE = "println(\"" + MESSAGE + "\");\n";
  private static final int NUMBER_OF_LINES = 32 * 1024;

  /*
  @Test
  public void test() throws IOException {
    InputStream is = new MockInputStream(LINE, NUMBER_OF_LINES);
    String result = new String(is.readAllBytes());
    System.out.println(result);
    assertEquals(NUMBER_OF_LINES * LINE.length(), result.length());
  }

   */
}
