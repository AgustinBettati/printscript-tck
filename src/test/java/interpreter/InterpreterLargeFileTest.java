package interpreter;

import implementation.CustomImplementationFactory;
import org.junit.Ignore;
import org.junit.Test;
import util.ErrorCollector;
import util.MockInputStream;
import util.PrintCollector;
import util.PrintCounter;

import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class InterpreterLargeFileTest {

    private static final String MESSAGE = "This is a text";
    private static final String LINE = "println(\"" + MESSAGE + "\");\n";
    //32
    private static final int NUMBER_OF_LINES = 12 * 1024;
    private final PrintScriptInterpreter interpreter = new CustomImplementationFactory().interpreter();

    @Ignore
    public void testWithCounter() {
        final PrintCounter printCounter = new PrintCounter(message -> Objects.equals(message, MESSAGE));
        final ErrorCollector errorCollector = new ErrorCollector();
        interpreter.execute(new MockInputStream(LINE, NUMBER_OF_LINES), "1.0", printCounter, errorCollector, (ignored) -> "");

        assertThat(errorCollector.getErrors(), is(emptyList()));
        assertThat(printCounter.getCount(), is(NUMBER_OF_LINES));
    }

    @Ignore
    public void testWithCollector()  {
        final PrintCollector printCollector = new PrintCollector();
        final ErrorCollector errorCollector = new ErrorCollector();
        final var inputStream = new MockInputStream(LINE, NUMBER_OF_LINES);
        interpreter.execute(inputStream, "1.0", printCollector, errorCollector, (ignored) -> "");

        assertThat(errorCollector.getErrors(), is(singletonList("Java heap space")));
    }
}
