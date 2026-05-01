package stages;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// Stage 1: Lexical Analysis.
    //Pascal 224084038

public class LexicalAnalysis {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END"
    ));

    private static final Set<Character> OPERATORS = new HashSet<>(Arrays.asList(
        '+', '-', '*', '/'
    ));

    private static final Set<Character> SYMBOLS = new HashSet<>(Arrays.asList(
        '=', ',', ';', '(', ')'
    ));

    private static final Set<Character> SEMANTIC_SYMBOLS = new HashSet<>(Arrays.asList(
        '%', '$', '&', '<', '>'
    ));

    public static String analyze(String sourceLine) {
        int index = 0;

        while (index < sourceLine.length()) {
            char current = sourceLine.charAt(index);

            if (Character.isWhitespace(current)) {
                index++;
                continue;
            }

            // Digits are allowed through lexical analysis so Stage 2 can report
            // the assignment's required syntax error for numbers.
            if (Character.isDigit(current)) {
                index++;
                continue;
            }

            if (Character.isLetter(current)) {
                int start = index;
                while (index < sourceLine.length() && Character.isLetter(sourceLine.charAt(index))) {
                    index++;
                }

                String word = sourceLine.substring(start, index);
                if (isInvalidUppercaseKeyword(word)) {
                    return "ERROR: Lexical error - '" + word + "' is not a valid keyword";
                }

                continue;
            }

            // Operators and normal symbols are valid lexical units. Stage 2 decides
            // whether they are in a valid order.
            if (OPERATORS.contains(current) || SYMBOLS.contains(current)) {
                index++;
                continue;
            }

            // %, $, &, <, and > are assignment-specific semantic symbols. They must
            // pass this stage so Stage 3 can report the semantic error correctly.
            if (SEMANTIC_SYMBOLS.contains(current)) {
                index++;
                continue;
            }

            return "ERROR: Lexical error - invalid character '" + current + "'";
        }

        return sourceLine.trim();
    }

    private static boolean isInvalidUppercaseKeyword(String word) {
        return word.matches("[A-Z]{2,}") && !KEYWORDS.contains(word);
    }
}
