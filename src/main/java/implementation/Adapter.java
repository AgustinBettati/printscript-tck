package implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import rules.FormattingRules;
import rules.LinterRules;
import rules.LinterRulesV1;
import rules.LinterRulesV2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Adapter {

    public String inputStreamToString(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error reading from InputStream: " + e.getMessage());
        }
        return result.toString();
    }

    public FormattingRules adaptFormattingConfig(InputStream configStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(configStream);

            boolean spaceBeforeColon = jsonNode.has("spaceBeforeColon") && jsonNode.get("spaceBeforeColon").asBoolean();
            boolean spaceAfterColon = jsonNode.has("enforce-spacing-after-colon-in-declaration") && jsonNode.get("enforce-spacing-after-colon-in-declaration").asBoolean();
            boolean spaceAroundAssignment = !jsonNode.has("enforce-spacing-around-equals") || jsonNode.get("enforce-spacing-around-equals").asBoolean();
            int newlineBeforePrintln = jsonNode.has("line-breaks-after-println") ? jsonNode.get("line-breaks-after-println").asInt(0) : 0;
            int blockIndentation = jsonNode.has("blockIndentation") ? jsonNode.get("blockIndentation").asInt(4) : 4;
            boolean ifBraceSameLine = !jsonNode.has("if-brace-same-line") || jsonNode.get("if-brace-same-line").asBoolean();
            boolean singleSpaceSeparation = jsonNode.has("mandatory-single-space-separation") && jsonNode.get("mandatory-single-space-separation").asBoolean();

            return new FormattingRules(
                    spaceBeforeColon,
                    spaceAfterColon,
                    spaceAroundAssignment,
                    newlineBeforePrintln,
                    blockIndentation,
                    ifBraceSameLine,
                    singleSpaceSeparation
            );

        } catch (IOException e) {
            System.err.println("Error reading config InputStream: " + e.getMessage());
        }

        return new FormattingRules(false, true, false, 0, 4, true, false);
    }

    public LinterRules getLinterRules(InputStream rulesIS, String version) {
        LinterRules linterRules;
        String rulesFile = inputStreamToString(rulesIS);

        switch (version) {
            case "1.0": {
                Map<String, Object> rulesMap;
                try {
                    rulesMap = jsonToMap(rulesFile);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage());
                }

                String identifierFormat;
                if (rulesMap.get("identifier_format") == null) {
                    identifierFormat = "off";
                } else {
                    identifierFormat = (String) rulesMap.get("identifier_format");
                }

                Boolean mandatoryVoRinPrintln;
                if (rulesMap.get("mandatory-variable-or-literal-in-println") == null) {
                    mandatoryVoRinPrintln = true;
                } else {
                    mandatoryVoRinPrintln = (Boolean) rulesMap.get("mandatory-variable-or-literal-in-println");
                }

                linterRules = new LinterRulesV1(
                        identifierFormat,
                        !mandatoryVoRinPrintln
                );
                break;
            }
            case "1.1": {
                Map<String, Object> rulesMap;
                try {
                    rulesMap = jsonToMap(rulesFile);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage());
                }

                String identifierFormat;
                if (rulesMap.get("identifier_format") == null) {
                    identifierFormat = "off";
                } else {
                    identifierFormat = (String) rulesMap.get("identifier_format");
                }

                boolean mandatoryVoRprintln;
                if (rulesMap.get("mandatory-variable-or-literal-in-println") == null) {
                    mandatoryVoRprintln = false;
                } else {
                    mandatoryVoRprintln = (Boolean) rulesMap.get("mandatory-variable-or-literal-in-println");
                }

                boolean mandatoryVorLinReadInput;
                if (rulesMap.get("mandatory-variable-or-literal-in-readInput") == null) {
                    mandatoryVorLinReadInput = true;
                } else {
                    mandatoryVorLinReadInput = (Boolean) rulesMap.get("mandatory-variable-or-literal-in-readInput");
                }

                linterRules = new LinterRulesV2(
                        identifierFormat,
                        !mandatoryVoRprintln,
                        !mandatoryVorLinReadInput
                );
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported version: " + version);
        }

        return linterRules;
    }

    private Map<String, Object> jsonToMap(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, Map.class);
    }
}
