//Pascal

package stages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LexicalAnalysis {
    
    // Valid keywords
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END"
    ));
    
    // Valid operators
    private static final Set<Character> OPERATORS = new HashSet<>(Arrays.asList('+', '-', '*', '/'));
    
    // Valid symbols
    private static final Set<Character> SYMBOLS = new HashSet<>(Arrays.asList('=', ','));
    
    // Forbidden characters
    private static final Set<Character> FORBIDDEN = new HashSet<>(Arrays.asList('%', '$', '&', '<', '>', ';'));
    
    public static String analyze(String sourceLine) {
        StringBuilder output = new StringBuilder();
        List<String> tokens = new ArrayList<>();
        int tokenCount = 0;
        
        output.append("\n====== STAGE 1: LEXICAL ANALYSIS ======\n");
        output.append("Source line: \"").append(sourceLine).append("\"\n");
        output.append("----------------------------------------\n");
        
        int i = 0;
        while (i < sourceLine.length()) {
            char c = sourceLine.charAt(i);
            
            // Skip whitespace
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }
            
            // Check for forbidden characters FIRST (Assignment requirement)
            if (FORBIDDEN.contains(c)) {
                output.append(">>> LEXICAL ERROR: Forbidden character '").append(c)
                .append("' found at position ").append(i + 1).append("\n");
                output.append("    Symbols %, $, &, <, >, ; are not allowed.\n");
                return output.toString();
            }
            
            // Check for digits (not allowed - syntax error per assignment)
            if (Character.isDigit(c)) {
                output.append(">>> LEXICAL ERROR: Digit '").append(c)
                    .append("' found at position ").append(i + 1).append("\n");
                output.append("    Digits 0-9 are not allowed in this language.\n");
                return output.toString();
            }
            
            tokenCount++;
            
            // Handle keywords and identifiers
            if (Character.isLetter(c)) {
                StringBuilder token = new StringBuilder();
                while (i < sourceLine.length() && (Character.isLetterOrDigit(sourceLine.charAt(i)) || 
                        sourceLine.charAt(i) == '_')) {
                    token.append(sourceLine.charAt(i));
                    i++;
                }
                String tokenStr = token.toString();
                
                // Check for misspelled keyword (uppercase but not in KEYWORDS)
                if (tokenStr.matches("[A-Z]{2,}") && !KEYWORDS.contains(tokenStr)) {
                    output.append(">>> LEXICAL ERROR: '").append(tokenStr)
                    .append("' is not a recognised keyword.\n");
                    output.append("    Did you mean a valid keyword?\n");
                    return output.toString();
                }
                
                // Classify the token
                String type = classifyToken(tokenStr);
                output.append(String.format("TOKEN#%-3d %-15s %s\n", 
                           tokenCount, tokenStr, type));
                tokens.add(tokenStr);
            }
            // Handle operators
            else if (OPERATORS.contains(c)) {
                // Check for combined operators (e.g., +*, -/, */)
                if (i + 1 < sourceLine.length() && OPERATORS.contains(sourceLine.charAt(i + 1))) {
                    output.append(">>> SYNTAX ERROR: Combined operators '")
                          .append(c).append(sourceLine.charAt(i + 1))
                          .append("' are not allowed.\n");
                    return output.toString();
                }
                output.append(String.format("TOKEN#%-3d %-15s %s\n", 
                           tokenCount, c, "OPERATOR"));
                tokens.add(String.valueOf(c));
                i++;
            }
            // Handle symbols
            else if (SYMBOLS.contains(c)) {
                String symbolType = (c == '=') ? "assignment" : "separator";
                output.append(String.format("TOKEN#%-3d %-15s %s (%s)\n", 
                           tokenCount, c, "SYMBOL", symbolType));
                tokens.add(String.valueOf(c));
                i++;
            }
            // Handle any other character
            else {
                output.append(">>> LEXICAL ERROR: Invalid character '").append(c)
                      .append("' at position ").append(i + 1).append("\n");
                return output.toString();
            }
        }
        
        // Check for semicolon at end of line (Syntax error per assignment)
        if (sourceLine.trim().endsWith(";")) {
            output.append(">>> SYNTAX ERROR: Semicolon ';' at end of line is not allowed.\n");
            return output.toString();
        }
        
        // Summary
        output.append("----------------------------------------\n");
        output.append("LEXICAL ANALYSIS COMPLETED SUCCESSFULLY\n");
        output.append("Total tokens: ").append(tokenCount).append("\n");
        
        return output.toString();
    }
    
    /**
     * Classifies a token based on assignment rules
     */
    private static String classifyToken(String token) {
        // Keywords (uppercase)
        if (KEYWORDS.contains(token)) {
            return "KEYWORD";
        }
        // Identifiers: single letters A-Z, a-z OR multi-letter words in lowercase
        if (token.matches("[A-Za-z]") || token.matches("[a-z]{2,}")) {
            return "IDENTIFIER";
        }
        // Multi-letter uppercase that's not a keyword (already caught as error)
        if (token.matches("[A-Z]{2,}")) {
            return "INVALID_KEYWORD";
        }
        return "UNKNOWN";
    }
    
    /**
     * Simple test method
     */
    public static void main(String[] args) {
        // Test cases based on assignment requirements
        String[] testCases = {
            "BEGIN",                    // Valid keyword
            "INTEGER A, B, C",         // Valid declaration
            "LET X = A + B",           // Valid assignment
            "LET a = b + c",           // Valid lowercase identifiers
            "LET X = A % B",           // Forbidden % character
            "LET X = 5",               // Digit not allowed
            "LET X = A +* B",          // Combined operators
            "LET X = A;",              // Semicolon at end
            "BEGINN",                  // Misspelled keyword
            "WRITE M",                 // Valid keyword
            "temp = value",            // Valid lowercase identifier
            "LET G = a + c"            // Valid expression
        };
        
        System.out.println("=========================================");
        System.out.println("  LEXICAL ANALYSIS TESTER");
        System.out.println("=========================================");
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n--- TEST CASE " + (i + 1) + " ---");
            System.out.println("Input: " + testCases[i]);
            String result = analyze(testCases[i]);
            System.out.println(result);
        }
    }
}