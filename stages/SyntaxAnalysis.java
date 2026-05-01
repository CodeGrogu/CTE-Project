package stages;

import java.util.*;

// Stage 2: Syntax Analysis.
//Pascal 224084038

public class SyntaxAnalysis {



    // Valid keywords in the language
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END"
    ));

    public static String analyze(String input) {

        // Stop if previous stage already failed
        if (input.startsWith("ERROR:")) {
            return input;
        }

        input = input.trim();

        // Syntax rules (moved from lexical stage)
        if (input.matches(".*\\d.*")) {
            return "ERROR: Syntax error - Digits are not allowed";
        }

        if (input.endsWith(";")) {
            return "ERROR: Syntax error - Semicolon not allowed";
        }

        if (input.matches(".*[+\\-*/]{2,}.*")) {
            return "ERROR: Syntax error - Combined operators not allowed";
        }

        // Split input into tokens
        List<String> tokens = Arrays.asList(input.split("\\s+"));

        if (tokens.isEmpty()) {
            return "ERROR: Syntax error - Empty statement";
        }

        String firstToken = tokens.get(0);

        // Decide statement type
        if (KEYWORDS.contains(firstToken)) {
            return parseKeyword(tokens, input);
        } 
        else if (isIdentifier(firstToken)) {
            return parseAssignment(tokens, input);
        } 
        else {
            return "ERROR: Syntax error - Invalid start of statement";
        }
    }

    // Handle keyword-based statements
    private static String parseKeyword(List<String> tokens, String input) {

        String keyword = tokens.get(0);

        switch (keyword) {

            case "BEGIN":
            case "END":
                // Must be alone
                if (tokens.size() != 1) {
                    return "ERROR: Syntax error - " + keyword + " must stand alone";
                }
                return input;

            case "INTEGER":
            case "INPUT":
                // Format: identifier , identifier ...
                if (tokens.size() < 2) {
                    return "ERROR: Syntax error - Missing identifiers";
                }

                for (int i = 1; i < tokens.size(); i++) {
                    if (i % 2 == 1) {
                        if (!isIdentifier(tokens.get(i))) {
                            return "ERROR: Syntax error - Invalid identifier";
                        }
                    } else {
                        if (!tokens.get(i).equals(",")) {
                            return "ERROR: Syntax error - Expected ','";
                        }
                    }
                }
                return input;

            case "LET":
                // Format: LET id = expression
                if (tokens.size() < 4) {
                    return "ERROR: Syntax error - Incomplete LET statement";
                }

                if (!isIdentifier(tokens.get(1))) {
                    return "ERROR: Syntax error - Invalid identifier";
                }

                if (!tokens.get(2).equals("=")) {
                    return "ERROR: Syntax error - Missing '='";
                }

                if (!validExpression(tokens.subList(3, tokens.size()))) {
                    return "ERROR: Syntax error - Invalid expression";
                }

                return input;

            case "WRITE":
                // Format: WRITE expression
                if (tokens.size() < 2) {
                    return "ERROR: Syntax error - Missing expression";
                }

                if (!validExpression(tokens.subList(1, tokens.size()))) {
                    return "ERROR: Syntax error - Invalid expression";
                }

                return input;

            default:
                return "ERROR: Syntax error - Unknown keyword";
        }
    }

    // Handle assignment: X = expression
    private static String parseAssignment(List<String> tokens, String input) {

        if (tokens.size() < 3) {
            return "ERROR: Syntax error - Incomplete assignment";
        }

        if (!isIdentifier(tokens.get(0))) {
            return "ERROR: Syntax error - Invalid identifier";
        }

        if (!tokens.get(1).equals("=")) {
            return "ERROR: Syntax error - Missing '='";
        }

        if (!validExpression(tokens.subList(2, tokens.size()))) {
            return "ERROR: Syntax error - Invalid expression";
        }

        return input;
    }

    // Check if expression structure is valid
    private static boolean validExpression(List<String> tokens) {

        boolean expectOperand = true;

        for (String t : tokens) {

            if (expectOperand) {
                if (!isIdentifier(t) && !t.equals("(")) {
                    return false;
                }
                expectOperand = false;
            } else {
                if (t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/")) {
                    expectOperand = true;
                } else if (t.equals(")")) {
                    // allow closing bracket
                } else {
                    return false;
                }
            }
        }

        return !expectOperand;
    }

    // Identifier rules
    private static boolean isIdentifier(String token) {
        return token.matches("[A-Za-z]") || token.matches("[a-z]{2,}");
    }
}