package stages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Stage 2: Syntax Analysis.
//Pascal 224084038

public class SyntaxAnalysis {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END"
    ));

    private static final Set<Character> SEMANTIC_SYMBOLS = new HashSet<>(Arrays.asList(
        '%', '$', '&', '<', '>'
    ));

    public static String analyze(String input) {
        if (input.startsWith("ERROR:")) {
            return input;
        }

        String cleanInput = input.trim();
        if (cleanInput.isEmpty()) {
            return "ERROR: Syntax error - empty statement";
        }

        if (containsDigit(cleanInput)) {
            return "ERROR: Syntax error - digits are not allowed";
        }

        if (cleanInput.endsWith(";")) {
            return "ERROR: Syntax error - semicolon at the end of a line is not allowed";
        }

        if (containsCombinedOperators(cleanInput)) {
            return "ERROR: Syntax error - combined operators are not allowed";
        }

        // Semantic-only symbols are allowed to pass through this stage so Stage 3
        // can report them as semantic errors instead of syntax errors.
        if (containsSemanticSymbol(cleanInput)) {
            return cleanInput;
        }

        List<String> tokens = tokenize(cleanInput);
        if (tokens.isEmpty()) {
            return "ERROR: Syntax error - empty statement";
        }

        String firstToken = tokens.get(0);
        if (KEYWORDS.contains(firstToken)) {
            return parseKeywordStatement(tokens, cleanInput);
        }

        if (isIdentifier(firstToken)) {
            return parseAssignment(tokens, cleanInput);
        }

        return "ERROR: Syntax error - invalid start of statement";
    }

    private static String parseKeywordStatement(List<String> tokens, String cleanInput) {
        String keyword = tokens.get(0);

        switch (keyword) {
            case "BEGIN":
            case "END":
                if (tokens.size() != 1) {
                    return "ERROR: Syntax error - " + keyword + " must stand alone";
                }
                return cleanInput;

            case "INTEGER":
            case "INPUT":
                return parseIdentifierList(tokens, cleanInput, keyword);

            case "LET":
                return parseLet(tokens, cleanInput);

            case "WRITE":
                return parseWrite(tokens, cleanInput);

            default:
                return "ERROR: Syntax error - unknown keyword";
        }
    }

    private static String parseIdentifierList(List<String> tokens, String cleanInput, String keyword) {
        if (tokens.size() < 2) {
            return "ERROR: Syntax error - " + keyword + " requires at least one identifier";
        }

        boolean expectIdentifier = true;
        for (int index = 1; index < tokens.size(); index++) {
            String token = tokens.get(index);

            if (expectIdentifier) {
                if (!isIdentifier(token)) {
                    return "ERROR: Syntax error - expected identifier";
                }
            } else if (!token.equals(",")) {
                return "ERROR: Syntax error - expected ','";
            }

            expectIdentifier = !expectIdentifier;
        }

        if (expectIdentifier) {
            return "ERROR: Syntax error - trailing comma is not allowed";
        }

        return cleanInput;
    }

    private static String parseLet(List<String> tokens, String cleanInput) {
        if (tokens.size() < 4) {
            return "ERROR: Syntax error - incomplete LET statement";
        }

        if (!isIdentifier(tokens.get(1))) {
            return "ERROR: Syntax error - expected identifier after LET";
        }

        if (!tokens.get(2).equals("=")) {
            return "ERROR: Syntax error - expected '=' after LET identifier";
        }

        if (!isValidExpression(tokens.subList(3, tokens.size()))) {
            return "ERROR: Syntax error - invalid expression";
        }

        return cleanInput;
    }

    private static String parseWrite(List<String> tokens, String cleanInput) {
        if (tokens.size() < 2) {
            return "ERROR: Syntax error - WRITE requires an expression";
        }

        if (!isValidExpression(tokens.subList(1, tokens.size()))) {
            return "ERROR: Syntax error - invalid WRITE expression";
        }

        return cleanInput;
    }

    private static String parseAssignment(List<String> tokens, String cleanInput) {
        if (tokens.size() < 3) {
            return "ERROR: Syntax error - incomplete assignment";
        }

        if (!isIdentifier(tokens.get(0))) {
            return "ERROR: Syntax error - invalid assignment target";
        }

        if (!tokens.get(1).equals("=")) {
            return "ERROR: Syntax error - expected '=' in assignment";
        }

        if (!isValidExpression(tokens.subList(2, tokens.size()))) {
            return "ERROR: Syntax error - invalid assignment expression";
        }

        return cleanInput;
    }

    private static boolean isValidExpression(List<String> tokens) {
        boolean expectOperand = true;

        for (String token : tokens) {
            if (expectOperand) {
                if (!isIdentifier(token) && !token.equals("(")) {
                    return false;
                }
                expectOperand = false;
                continue;
            }

            if (isOperator(token)) {
                expectOperand = true;
            } else if (!token.equals(")")) {
                return false;
            }
        }

        return !expectOperand;
    }

    private static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        int index = 0;

        while (index < input.length()) {
            char current = input.charAt(index);

            if (Character.isWhitespace(current)) {
                index++;
                continue;
            }

            if (Character.isLetter(current)) {
                int start = index;
                while (index < input.length() && Character.isLetter(input.charAt(index))) {
                    index++;
                }
                tokens.add(input.substring(start, index));
                continue;
            }

            tokens.add(String.valueOf(current));
            index++;
        }

        return tokens;
    }

    private static boolean containsDigit(String input) {
        for (int index = 0; index < input.length(); index++) {
            if (Character.isDigit(input.charAt(index))) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsCombinedOperators(String input) {
        for (int index = 0; index < input.length() - 1; index++) {
            if (isOperator(input.charAt(index)) && isOperator(input.charAt(index + 1))) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsSemanticSymbol(String input) {
        for (int index = 0; index < input.length(); index++) {
            if (SEMANTIC_SYMBOLS.contains(input.charAt(index))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isIdentifier(String token) {
        return token.matches("[A-Za-z]") || token.matches("[a-z]{2,}");
    }

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private static boolean isOperator(char token) {
        return token == '+' || token == '-' || token == '*' || token == '/';
    }
}
