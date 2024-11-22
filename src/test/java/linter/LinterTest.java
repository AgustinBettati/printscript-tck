package linter;

import implementation.CustomImplementationFactory;
import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptLinter;
import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.ErrorCollector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static util.SuiteOps.*;

@RunWith(Parameterized.class)
public class LinterTest {
/*
    private static final String basePath = "src/test/resources/linter/";
    private final PrintScriptLinter linter = new CustomImplementationFactory().linter();

    @SuppressWarnings("WeakerAccess")
    @Parameterized.Parameter(value = 0)
    public String version;

    @Parameterized.Parameter(value = 1)
    public String name;

    @SuppressWarnings("WeakerAccess")
    @Parameterized.Parameter(value = 2)
    public File file;

    @Parameterized.Parameter(value = 3)
    public File config;

    @Parameterized.Parameters(name = "version {0} - {1}")
    public static Collection<Object[]> data() {
        return collectTestSet(basePath, false, filePicker());
    }

    @Ignore
    public void testLint() throws IOException {
        final var fileInputStream = new FileInputStream(file);
        ErrorCollector errorCollector = new ErrorCollector();
        final var shouldBeValid = this.name.startsWith("valid");
        final var configInputStream = new FileInputStream(this.config);
        linter.lint(fileInputStream, version, configInputStream, errorCollector);
        final Matcher<List<String>> errorMatcher = getErrorMatcherForExpectedResult(shouldBeValid);
        assertThat(errorCollector.getErrors(), errorMatcher);
    }

    private static BiFunction<String, String, List<Object[]>> filePicker() {
        return (basePath, version) -> {
            var tests = getVersionSpecificPath(basePath, version);
            try {
                return Files.list(tests).map(test -> List.of(
                        version,
                        test.getFileName().toString(),
                        getInnerFile(test, "main.ps"),
                        getInnerFile(test, "config.json"))
                    .toArray()).toList();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    private Matcher<List<String>> getErrorMatcherForExpectedResult(boolean shouldBeValid) {
        return shouldBeValid ? is(Collections.emptyList()) : not(Collections.emptyList());
    }

 */
}
