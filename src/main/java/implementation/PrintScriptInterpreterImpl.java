package implementation;

import interpreter.*;
import kotlin.Pair;
import lexer.Lexer;
import nodes.Expression;
import nodes.StatementType;
import org.jetbrains.annotations.NotNull;
import parser.Parser;
import position.Position;
import position.visitor.Environment;
import token.Token;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            String input = null;
            Iterator<Token> tokens = new Lexer(src, version);
            Parser asts = new Parser(tokens, version, null);

            Environment currentEnvironment = createEnvironmentFromMap(System.getenv());
            asts.setEnv(currentEnvironment);

            while (asts.hasNext()) {
                StatementType statement = asts.next();
                Pair<StringBuilder, Environment> result;
                result = getStringBuilderEnvironmentPair(version, emitter, provider, statement, asts, currentEnvironment);


                String first = result.getFirst().toString().trim();
                String cleanedOutput = removeSurroundingQuotes(first);

                if (!cleanedOutput.isEmpty()) {
                    emitter.print(cleanedOutput);
                }

                currentEnvironment = result.getSecond();
                asts.setEnv(currentEnvironment);
            }
        } catch (OutOfMemoryError | Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    private static @NotNull Pair<StringBuilder, Environment> getStringBuilderEnvironmentPair(String version, PrintEmitter emitter, InputProvider provider, StatementType statement, Parser asts, Environment currentEnvironment) {
        Pair<StringBuilder, Environment> result;
        String input;
        if (statement instanceof StatementType.Variable) {
            if ("READ_INPUT".equals(((StatementType.Variable) statement).getInitializer() != null ? ((StatementType.Variable) statement).getInitializer().getExpressionType() : null)) {
                assert ((StatementType.Variable) statement).getInitializer() != null;
                Object value = ((StatementType.Variable) statement).getInitializer().getValue();
                if (value instanceof Expression.Grouping) {
                    emitter.print(Objects.requireNonNull(((Expression.Grouping) value).getExpression().getValue()).toString());
                    input = provider.input(Objects.requireNonNull(((Expression.Grouping) value).getExpression().getValue()).toString());
                    asts.setInput(input);
                    result = Interpreter.INSTANCE.interpret(statement, version, currentEnvironment, input);
                } else {
                    result = Interpreter.INSTANCE.interpret(statement, version, currentEnvironment, null);
                }
            } else {
                result = Interpreter.INSTANCE.interpret(statement, version, currentEnvironment, null);
            }
        } else {
            result = Interpreter.INSTANCE.interpret(statement, version, currentEnvironment, null);
        }
        return result;
    }

    // Sacar de aca y hacerlo en el Interpreter nuestro
    private String removeSurroundingQuotes(String str) {
        if (str.length() >= 2 && str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1).trim();
        }
        return str.trim();
    }

    private Environment createEnvironmentFromMap(Map<String, String> envVarsMap) {
        Environment env = new Environment();

        for (Map.Entry<String, String> entry : envVarsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            StatementType.Variable variable = new StatementType.Variable("const", key, new Expression.Literal(value, new Position(0,0)),  determineDataType(value),new Position(0,0));

            env = env.define(variable);
        }

        return env;
    }

    private String determineDataType(String value) {
        return "string";
    }

}
