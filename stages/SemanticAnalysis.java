package stages;

// Stage 3: Semantic Analysis.
// This class checks meaning-level rules after Pascal's lexical and syntax stages.
// It does not check token spelling, combined operators, digits, or semicolon placement.
public class SemanticAnalysis {
    // The assignment says these symbols are not allowed in source lines.
    // If any of them appear after syntax analysis, this stage reports a semantic error.
    private static final char[] DISALLOWED_SYMBOLS = {'%', '$', '&', '<', '>'};

    public static String analyze(String syntaxOutput) {
        // The input is the line returned by SyntaxAnalysis.
        // At this point, Stage 1 and Stage 2 should already have handled their own errors.

        // Check each disallowed symbol one by one so the error message can name it clearly.
        for (char symbol : DISALLOWED_SYMBOLS) {
            // indexOf returns -1 when the symbol is not present in the line.
            if (syntaxOutput.indexOf(symbol) >= 0) {
                // MiniCompiler stops the pipeline when a stage returns text starting with ERROR:.
                return "ERROR: Semantic error - disallowed symbol '" + symbol + "' found";
            }
        }

        // If no semantic error was found, pass the line unchanged to Stage 4.
        return syntaxOutput;
    }
}
