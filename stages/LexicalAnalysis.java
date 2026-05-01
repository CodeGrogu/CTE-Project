package stages;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

    // Stage 1: Lexical Analysis.
    //Pascal 224084038

public class LexicalAnalysis {

    // Valid keywords
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END"
    ));

    // Allowed operators
    private static final Set<Character> OPERATORS = new HashSet<>(Arrays.asList(
        '+', '-', '*', '/'
    ));

    // Allowed symbols
    private static final Set<Character> SYMBOLS = new HashSet<>(Arrays.asList(
        '=', ',', '(', ')'
    ));

    public static String analyze(String sourceLine) {

        int i = 0;

        while (i < sourceLine.length()) {
            char c = sourceLine.charAt(i);

            // Skip spaces
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // ❌ Invalid character (true lexical error only)
            if (!Character.isLetterOrDigit(c) &&
                !OPERATORS.contains(c) &&
                !SYMBOLS.contains(c)) {

                return "ERROR: Lexical error - Invalid character '" + c + "'";
            }

            // Handle words (keywords / identifiers)
            if (Character.isLetter(c)) {

                StringBuilder token = new StringBuilder();

                while (i < sourceLine.length() &&
                      (Character.isLetterOrDigit(sourceLine.charAt(i)) ||
                       sourceLine.charAt(i) == '_')) {

                    token.append(sourceLine.charAt(i));
                    i++;
                }

                String word = token.toString();

                // ❌ Wrong keyword (e.g. BEGINN)
                if (word.matches("[A-Z]{2,}") && !KEYWORDS.contains(word)) {
                    return "ERROR: Lexical error - '" + word + "' is not a valid keyword";
                }

                continue;
            }

            // Move forward for operators/symbols
            i++;
        }

        // ✅ Success → return clean input for next stage
        return sourceLine.trim();
    }

    // Optional: identifier rule (used in later stages)
    private static boolean isIdentifier(String token) {
        return token.matches("[A-Za-z]") || token.matches("[a-z]{2,}");
    }

    // Simple test
    public static void main(String[] args) {

        String[] testCases = {
            "BEGIN",
            "INTEGER A, B, C",
            "LET X = A + B",
            "LET a = b + c",
            "LET X = A % B",   // goes to semantic stage
            "LET X = 5",       // goes to syntax stage
            "LET X = A +* B",  // goes to syntax stage
            "LET X = A;",      // goes to syntax stage
            "BEGINN",          // lexical error
            "WRITE M",
            "temp = value",
            "LET G = a + c"
        };

        for (String test : testCases) {
            System.out.println("Input: " + test);
            System.out.println("Output: " + analyze(test));
            System.out.println("----------------------");
        }
    }
}