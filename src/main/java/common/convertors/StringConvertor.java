package common.convertors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public class StringConvertor {
  public static String convert(InputStream inputStream) {
    try {
      return new String(inputStream.readAllBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String convertLinterConfig(InputStream inputStream) {
    try {
      String theirConfig = new String(inputStream.readAllBytes());
      return adaptConfig(theirConfig);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String adaptConfig(String theirConfig) {
    JsonObject result = new JsonObject();
    JsonObject config = JsonParser.parseString(theirConfig).getAsJsonObject();

    try {
      JsonObject identifier = new JsonObject();
      JsonObject writingConvention = new JsonObject();
      String identifierRules = config.get("identifier_format").getAsString();
      switch (identifierRules) {
        case "snake case" -> {
          writingConvention.addProperty("conventionName", "snakeCase");
          writingConvention.addProperty("conventionPattern", "^[a-z]+(_[a-z0-9]+)*$");
        }
        case "camel case" -> {
          writingConvention.addProperty("conventionName", "camelCase");
          writingConvention.addProperty("conventionPattern", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");
        }
        default -> throw new IllegalArgumentException("Invalid identifier format: " + identifierRules);
      }
      identifier.add("writingConvention", writingConvention);
      result.add("identifier", identifier);
    } catch (Exception ignored) {

    }

    JsonObject callExpression = new JsonObject();
    JsonArray basicArgs = new JsonArray();
    basicArgs.add("IDENTIFIER");
    basicArgs.add("STRING_LITERAL");
    basicArgs.add("NUMBER_LITERAL");
    basicArgs.add("BOOLEAN_LITERAL");

    try {
      String methodName = "println";
      JsonObject printlnArguments = getArguments(config, basicArgs, methodName);
      callExpression.add(methodName, printlnArguments);
    } catch (Exception ignored) {

    }

    try {
      String methodName = "readInput";
      JsonObject readInputArguments = getArguments(config, basicArgs, methodName);;
      callExpression.add(methodName, readInputArguments);
    } catch (Exception ignored) {

    }

    if (!callExpression.entrySet().isEmpty()) {
      result.add("callExpression", callExpression);
    }

    return result.toString();
  }

  private static JsonObject getArguments(JsonObject config, JsonArray basicArgs, String methodName) {
    JsonObject args = new JsonObject();
    JsonArray copyBasicArgs = basicArgs.deepCopy();
    boolean isStrict = config.get("mandatory-variable-or-literal-in-" + methodName).getAsBoolean();
    if (!isStrict) {
      copyBasicArgs.add("CALL_EXPRESSION");
      copyBasicArgs.add("BINARY_EXPRESSION");
    }
    args.add("arguments", copyBasicArgs);

    return args;
  }
}
