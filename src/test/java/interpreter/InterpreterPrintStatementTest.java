package interpreter;

import implementation.CustomImplementationFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.ErrorCollector;
import util.PrintCollector;
import util.QueueInputProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static util.Queues.toQueue;

@RunWith(Parameterized.class)
public class InterpreterPrintStatementTest {

    private final PrintScriptInterpreter interpreter = new CustomImplementationFactory().interpreter();

    @SuppressWarnings("WeakerAccess")
    @Parameterized.Parameter(value = 0)
    public String version;

    @SuppressWarnings("WeakerAccess")
    @Parameterized.Parameter(value = 1)
    public String directory;

    @Parameterized.Parameters(name = "version {0} - {1})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{

            {"1.0", "arithmetic-operations"},
            {"1.0", "arithmetic-operations-decimal"},
            {"1.0", "simple-declare-assign"},
            {"1.0", "string-and-number-concat"},
            {"1.1", "arithmetic-operations"},
            {"1.1", "arithmetic-operations-decimal"},
            {"1.1", "simple-declare-assign"},
            {"1.1", "string-and-number-concat"},
            {"1.1", "if-statement-true"},
            {"1.1", "if-statement-false"},
            {"1.1", "else-statement-true"},
            {"1.1", "else-statement-false"},
            {"1.1", "read-input"},
            {"1.1", "read-env"}

        });
    }

    @Test
    public void testPrintStatement() throws FileNotFoundException {
        final String testDirectory = "src/test/resources/print-statement/" + version + "/" + directory + "/";
        final File srcFile = new File(testDirectory + "main.ps");
        final List<String> expectedOutput = readLines(testDirectory + "output.txt");
        final List<String> input = readLinesIfExists(testDirectory + "input.txt").orElse(emptyList());

        final PrintCollector printCollector = new PrintCollector();
        final ErrorCollector errorCollector = new ErrorCollector();
        final InputProvider inputProvider = new QueueInputProvider(toQueue(input));
        final var fileInputStream = new FileInputStream(srcFile);
        interpreter.execute(fileInputStream, version, printCollector, errorCollector, inputProvider);

        assertThat(errorCollector.getErrors(), is(emptyList()));
        assertThat(printCollector.getMessages(), is(expectedOutput));
    }

    private List<String> readLines(String filePath) throws FileNotFoundException {
        return readLinesIfExists(filePath).orElseThrow(() -> new FileNotFoundException(filePath));
    }

    private Optional<List<String>> readLinesIfExists(String filePath) throws FileNotFoundException {
        final File file = new File(filePath);
        if (file.exists()) {
            Scanner s = new Scanner(file);
            ArrayList<String> list = new ArrayList<>();
            while (s.hasNextLine()) {
                list.add(s.nextLine());
            }
            s.close();
            return Optional.of(list);
        }

        return Optional.empty();
    }

}
