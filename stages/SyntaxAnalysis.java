//pascal

package stages;

import java.util.*;

public class SyntaxAnalysis {
    
    // Grammar rules for the language
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END"
    ));
    
    public static String analyze(String lexicalOutput) {
        StringBuilder result = new StringBuilder();
        
        // TODO(Pascal): This method currently receives the long formatted report from
        // LexicalAnalysis.java. The pipeline will work better if Stage 1 returns either:
        // 1. "ERROR: Lexical error - ..." when lexical analysis fails, or
        // 2. the original clean source line / clean token format when it succeeds.
        result.append("\n====== STAGE 2: SYNTAX ANALYSIS ======\n");
        result.append("Input from Lexical Analysis:\n");
        result.append(lexicalOutput);
        result.append("\n----------------------------------------\n");
        
        // Extract tokens from lexical output
        List<Token> tokens = extractTokens(lexicalOutput);
        
        if (tokens.isEmpty()) {
            // TODO(Pascal): Every error returned to MiniCompiler.java must start with
            // "ERROR:" so processing stops before Stage 3.
            result.append(">>> SYNTAX ERROR: No tokens to analyze\n");
            return result.toString();
        }
        
        result.append("Starting Syntax Analysis...\n");
        result.append("----------------------------------------\n");
        
        // Check the structure based on the first token (statement type)
        String firstToken = tokens.get(0).value;
        
        if (KEYWORDS.contains(firstToken)) {
            // Parse based on keyword
            result.append(parseKeywordStatement(tokens));
        } else if (isIdentifier(firstToken)) {
            // Assignment statement (e.g., "X = A + B")
            result.append(parseAssignmentStatement(tokens));
        } else {
            result.append(">>> SYNTAX ERROR: Invalid statement beginning with '")
                  .append(firstToken).append("'\n");
            return result.toString();
        }
        
        // TODO(Pascal): On success, return the clean original line, not this formatted
        // report. Stage 3 and Stage 4 need
        // input such as "LET G = a + c", "M = A/B+C", or "N = G/H-I+a*B/c".
        // On failure, return "ERROR: Syntax error - ...".
        return result.toString();
    }
    
    /**
     * Parse a keyword-based statement
     */
    private static String parseKeywordStatement(List<Token> tokens) {
        StringBuilder result = new StringBuilder();
        String keyword = tokens.get(0).value;
        
        result.append("Parsing ").append(keyword).append(" statement...\n");
        
        switch (keyword) {
            case "BEGIN":
                result.append(parseBeginStatement(tokens));
                break;
            case "END":
                result.append(parseEndStatement(tokens));
                break;
            case "INTEGER":
                result.append(parseIntegerDeclaration(tokens));
                break;
            case "LET":
                result.append(parseLetStatement(tokens));
                break;
            case "INPUT":
                result.append(parseInputStatement(tokens));
                break;
            case "WRITE":
                result.append(parseWriteStatement(tokens));
                break;
            default:
                result.append(">>> SYNTAX ERROR: Unknown keyword '").append(keyword).append("'\n");
        }
        
        return result.toString();
    }
    
    /**
     * Parse BEGIN statement
     */
    private static String parseBeginStatement(List<Token> tokens) {
        StringBuilder result = new StringBuilder();
        
        if (tokens.size() == 1) {
            result.append("✓ Valid BEGIN statement\n");
            result.append("Derivation: BEGIN → BEGIN\n");
        } else if (tokens.size() > 1) {
            result.append(">>> SYNTAX ERROR: BEGIN statement should have no additional tokens\n");
            result.append("    Found: ");
            for (int i = 1; i < tokens.size(); i++) {
                result.append(tokens.get(i).value).append(" ");
            }
            result.append("\n");
        }
        
        return result.toString();
    }
    
    /**
     * Parse END statement
     */
    private static String parseEndStatement(List<Token> tokens) {
        StringBuilder result = new StringBuilder();
        
        if (tokens.size() == 1) {
            result.append("✓ Valid END statement\n");
            result.append("Derivation: END → END\n");
        } else if (tokens.size() > 1) {
            result.append(">>> SYNTAX ERROR: END statement should have no additional tokens\n");
            result.append("    Found: ");
            for (int i = 1; i < tokens.size(); i++) {
                result.append(tokens.get(i).value).append(" ");
            }
            result.append("\n");
        }
        
        return result.toString();
    }
    
    /**
     * Parse INTEGER declaration (e.g., "INTEGER A, B, C")
     */
    private static String parseIntegerDeclaration(List<Token> tokens) {
        StringBuilder result = new StringBuilder();
        
        result.append("Grammar: INTEGER → INTEGER identifier { , identifier }\n");
        result.append("Derivation:\n");
        result.append("  INTEGER");
        
        if (tokens.size() < 2) {
            result.append("\n>>> SYNTAX ERROR: INTEGER declaration requires at least one identifier\n");
            return result.toString();
        }
        
        int pos = 1;
        boolean needComma = false;
        boolean valid = true;
        
        while (pos < tokens.size()) {
            Token token = tokens.get(pos);
            
            if (needComma) {
                if (token.value.equals(",")) {
                    result.append(" ,");
                    needComma = false;
                } else {
                    result.append("\n>>> SYNTAX ERROR: Expected ',' but found '").append(token.value).append("'\n");
                    valid = false;
                    break;
                }
            } else {
                if (isIdentifier(token.value)) {
                    result.append(" ").append(token.value);
                    needComma = true;
                } else {
                    result.append("\n>>> SYNTAX ERROR: Expected identifier but found '").append(token.value).append("'\n");
                    valid = false;
                    break;
                }
            }
            pos++;
        }
        
        if (valid && !needComma) {
            result.append("\n✓ Valid INTEGER declaration\n");
        } else if (valid && needComma) {
            result.append("\n>>> SYNTAX ERROR: Trailing comma not allowed\n");
        }
        
        return result.toString();
    }
    
    /**
     * Parse LET statement (e.g., "LET X = A + B")
     */
    private static String parseLetStatement(List<Token> tokens) {
        StringBuilder result = new StringBuilder();
        
        result.append("Grammar: LET → LET identifier = expression\n");
        result.append("Derivation:\n");
        result.append("  LET");
        
        if (tokens.size() < 4) {
            result.append("\n>>> SYNTAX ERROR: LET statement incomplete\n");
            result.append("    Expected: LET identifier = expression\n");
            return result.toString();
        }
        
        // Check identifier
        if (!isIdentifier(tokens.get(1).value)) {
            result.append("\n>>> SYNTAX ERROR: Expected identifier after LET, found '")
                  .append(tokens.get(1).value).append("'\n");
            return result.toString();
        }
        result.append(" ").append(tokens.get(1).value);
        
        // Check equals sign
        if (!tokens.get(2).value.equals("=")) {
            result.append("\n>>> SYNTAX ERROR: Expected '=', found '")
                  .append(tokens.get(2).value).append("'\n");
            return result.toString();
        }
        result.append(" =");
        
        // Parse expression (tokens from position 3 onwards)
        result.append(parseExpression(tokens.subList(3, tokens.size())));
        
        return result.toString();
    }
    
    /**
     * Parse INPUT statement (e.g., "INPUT A, B, C")
     */
    private static String parseInputStatement(List<Token> tokens) {
        StringBuilder result = new StringBuilder();
        
        result.append("Grammar: INPUT → INPUT identifier { , identifier }\n");
        result.append("Derivation:\n");
        result.append("  INPUT");
        
        if (tokens.size() < 2) {
            result.append("\n>>> SYNTAX ERROR: INPUT statement requires at least one identifier\n");
            return result.toString();
        }
        
        int pos = 1;
        boolean needComma = false;
        boolean valid = true;
        
        while (pos < tokens.size()) {
            Token token = tokens.get(pos);
            
            if (needComma) {
                if (token.value.equals(",")) {
                    result.append(" ,");
                    needComma = false;
                } else {
                    result.append("\n>>> SYNTAX ERROR: Expected ',' but found '").append(token.value).append("'\n");
                    valid = false;
                    break;
                }
            } else {
                if (isIdentifier(token.value)) {
                    result.append(" ").append(token.value);
                    needComma = true;
                } else {
                    result.append("\n>>> SYNTAX ERROR: Expected identifier but found '").append(token.value).append("'\n");
                    valid = false;
                    break;
                }
            }
            pos++;
        }
        
        if (valid && !needComma) {
            result.append("\n✓ Valid INPUT statement\n");
        } else if (valid && needComma) {
            result.append("\n>>> SYNTAX ERROR: Trailing comma not allowed\n");
        }
        
        return result.toString();
    }
    
    /**
     * Parse WRITE statement (e.g., "WRITE M")
     */
    private static String parseWriteStatement(List<Token> tokens) {
        StringBuilder result = new StringBuilder();
        
        result.append("Grammar: WRITE → WRITE expression\n");
        result.append("Derivation:\n");
        result.append("  WRITE");
        
        if (tokens.size() < 2) {
            result.append("\n>>> SYNTAX ERROR: WRITE statement requires an expression\n");
            return result.toString();
        }
        
        // Parse expression (tokens from position 1 onwards)
        result.append(parseExpression(tokens.subList(1, tokens.size())));
        
        return result.toString();
    }
    
    /**
     * Parse assignment statement (e.g., "X = A + B")
     */
    private static String parseAssignmentStatement(List<Token> tokens) {
        StringBuilder result = new StringBuilder();
        
        result.append("Grammar: Assignment → identifier = expression\n");
        result.append("Derivation:\n");
        
        if (tokens.size() < 3) {
            result.append(">>> SYNTAX ERROR: Assignment statement incomplete\n");
            result.append("    Expected: identifier = expression\n");
            return result.toString();
        }
        
        // Check identifier
        if (!isIdentifier(tokens.get(0).value)) {
            result.append(">>> SYNTAX ERROR: Expected identifier, found '")
                .append(tokens.get(0).value).append("'\n");
            return result.toString();
        }
        result.append(" ").append(tokens.get(0).value);
        
        // Check equals sign
        if (!tokens.get(1).value.equals("=")) {
            result.append("\n>>> SYNTAX ERROR: Expected '=', found '")
             .append(tokens.get(1).value).append("'\n");
            return result.toString();
        }
        result.append(" =");
        
        // Parse expression (tokens from position 2 onwards)
        result.append(parseExpression(tokens.subList(2, tokens.size())));
        
        return result.toString();
    }
    
    /**
     * Parse an arithmetic expression
     * Grammar: expression → term { (+|-) term }
     *         term → factor { (*|/) factor }
     *         factor → identifier | ( expression )
     */
    private static String parseExpression(List<Token> tokens) {
        StringBuilder result = new StringBuilder();
        
        if (tokens.isEmpty()) {
            result.append("\n>>> SYNTAX ERROR: Expression expected but none found\n");
            return result.toString();
        }
        
        result.append("\n  Expression derivation:\n");
        result.append("  E → T");
        
        int pos = 0;
        boolean expectOperand = true;
        boolean valid = true;
        int parenCount = 0;
        
        // Simple recursive descent parsing
        ParseResult parseResult = parseExpressionRecursive(tokens, 0);
        
        if (parseResult.pos < tokens.size()) {
            result.append("\n>>> SYNTAX ERROR: Unexpected tokens after expression: ");
            while (parseResult.pos < tokens.size()) {
                result.append(tokens.get(parseResult.pos).value).append(" ");
                parseResult.pos++;
            }
            result.append("\n");
            valid = false;
        }
        
        if (valid && parseResult.valid) {
            result.append("\n  ✓ Expression is syntactically valid\n");
        } else if (!parseResult.valid) {
            result.append("\n>>> ").append(parseResult.error).append("\n");
        }
        
        return result.toString();
    }
    
    /**
     * Recursive descent parser for expressions
     */
    private static ParseResult parseExpressionRecursive(List<Token> tokens, int pos) {
        // Parse first term
        ParseResult termResult = parseTerm(tokens, pos);
        if (!termResult.valid) return termResult;
        
        // Parse any number of + or - operators followed by terms
        while (termResult.pos < tokens.size()) {
            Token op = tokens.get(termResult.pos);
            if (op.value.equals("+") || op.value.equals("-")) {
                termResult.pos++;
                ParseResult nextTerm = parseTerm(tokens, termResult.pos);
                if (!nextTerm.valid) return nextTerm;
                termResult.pos = nextTerm.pos;
            } else {
                break;
            }
        }
        
        return termResult;
    }
    
    /**
     * Parse a term (product of factors)
     */
    private static ParseResult parseTerm(List<Token> tokens, int pos) {
        // Parse first factor
        ParseResult factorResult = parseFactor(tokens, pos);
        if (!factorResult.valid) return factorResult;
        
        // Parse any number of * or / operators followed by factors
        while (factorResult.pos < tokens.size()) {
            Token op = tokens.get(factorResult.pos);
            if (op.value.equals("*") || op.value.equals("/")) {
                factorResult.pos++;
                ParseResult nextFactor = parseFactor(tokens, factorResult.pos);
                if (!nextFactor.valid) return nextFactor;
                factorResult.pos = nextFactor.pos;
            } else {
                break;
            }
        }
        
        return factorResult;
    }
    
    /**
     * Parse a factor (identifier or parenthesized expression)
     */
    private static ParseResult parseFactor(List<Token> tokens, int pos) {
        if (pos >= tokens.size()) {
            return new ParseResult(false, pos, "Unexpected end of expression");
        }
        
        Token token = tokens.get(pos);
        
        if (isIdentifier(token.value)) {
            // Factor is an identifier
            return new ParseResult(true, pos + 1, null);
        } else if (token.value.equals("(")) {
            // Factor is a parenthesized expression
            ParseResult exprResult = parseExpressionRecursive(tokens, pos + 1);
            if (!exprResult.valid) return exprResult;
            
            // Expect closing parenthesis
            if (exprResult.pos >= tokens.size() || !tokens.get(exprResult.pos).value.equals(")")) {
                return new ParseResult(false, exprResult.pos, "Expected ')'");
            }
            
            return new ParseResult(true, exprResult.pos + 1, null);
        } else {
            return new ParseResult(false, pos, "Expected identifier or '(', found '" + token.value + "'");
        }
    }
    
    /**
     * Check if a token is a valid identifier
     */
    private static boolean isIdentifier(String token) {
        return token.matches("[A-Za-z]") || token.matches("[a-z]{2,}");
    }
    
    /**
     * Extract tokens from lexical analysis output
     */
    private static List<Token> extractTokens(String lexicalOutput) {
        List<Token> tokens = new ArrayList<>();
        String[] lines = lexicalOutput.split("\n");
        
        for (String line : lines) {
            if (line.trim().startsWith("TOKEN#")) {
                // Parse token lines: "TOKEN#1  BEGIN KEYWORD"
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 3) {
                    String tokenValue = parts[1];
                    String tokenType = parts[2];
                    tokens.add(new Token(tokenValue, tokenType));
                }
            }
        }
        
        return tokens;
    }
    
    /**
     * Token class for internal use
     */
    static class Token {
        String value;
        String type;
        
        Token(String value, String type) {
            this.value = value;
            this.type = type;
        }
    }
    
    /**
     * Parse result class
     */
    static class ParseResult {
        boolean valid;
        int pos;
        String error;
        
        ParseResult(boolean valid, int pos, String error) {
            this.valid = valid;
            this.pos = pos;
            this.error = error;
        }
    }
}
