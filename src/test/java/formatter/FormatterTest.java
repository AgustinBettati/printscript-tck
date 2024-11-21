//package formatter;
//
//import implementation.CustomImplementationFactory;
//import interpreter.PrintScriptFormatter;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.util.Collection;
//import java.util.List;
//import java.util.function.BiFunction;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.Assert.assertEquals;
//import static util.SuiteOps.*;
//import static util.SuiteOps.getInnerFile;
//
//@RunWith(Parameterized.class)
//public class FormatterTest {
//
//    private static final String basePath = "src/test/resources/formatter/";
//    private final PrintScriptFormatter formatter = new CustomImplementationFactory().formatter();
//
//    @SuppressWarnings("WeakerAccess")
//    @Parameterized.Parameter(value = 0)
//    public String version;
//
//    @Parameterized.Parameter(value = 1)
//    public String name;
//
//    @SuppressWarnings("WeakerAccess")
//    @Parameterized.Parameter(value = 2)
//    public File file;
//
//    @Parameterized.Parameter(value = 3)
//    public File golden;
//
//    @Parameterized.Parameter(value = 4)
//    public File config;
//
//    @Parameterized.Parameters(name = "version {0} - {1}")
//    public static Collection<Object[]> data() {
//        return collectTestSet(basePath, true, filePicker());
//    }
//
//    @Test
//    public void testFormat() throws FileNotFoundException {
//        final var fileInputStream = new FileInputStream(file);
//        final var golden = readFile(this.golden);
//        final var configInputStream = new FileInputStream(this.config);
//        final var writer = new StringWriter();
//        formatter.format(fileInputStream, version, configInputStream, writer);
//        assertEquals(golden, writer.toString());
//    }
//
//    private static BiFunction<String, String, List<Object[]>> filePicker() {
//        return (basePath, version) -> {
//            var tests = getVersionSpecificPath(basePath, version);
//            try {
//                return Files.list(tests).map(test -> List.of(
//                        version,
//                        test.getFileName().toString(),
//                        getInnerFile(test, "main.ps"),
//                        getInnerFile(test, "golden.ps"),
//                        getInnerFile(test,"config.json")).toArray()).toList();
//            } catch (IOException e) {
//                throw new UncheckedIOException(e);
//            }
//        };
//    }
//}
