package stages;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

// Stage 4: Intermediate Code Representation.
// This class converts valid assignment expressions into three-address code (TAC).
// TAC is easier for later stages to generate, optimize, and translate to machine code.
public class IntermediateCodeRepresentation {
    public static String generate(String semanticOutput) {
        // The input should already be a valid line that passed Stages 1, 2, and 3.
        // Example input: M = A/B+C
        // Example output: t1 = A / B; t2 = t1 + C; M = t2
        try {
            // Separate the assignment target from the expression on the right side.
            Assignment assignment = parseAssignment(semanticOutput);

            // Break the expression into identifiers and operators.
            List<String> expressionTokens = tokenize(assignment.expression);

            // Convert infix notation into postfix notation so precedence is easier to handle.
            List<String> postfixTokens = toPostfix(expressionTokens);

            // Build the final TAC instructions from the postfix expression.
            return toThreeAddressCode(assignment.target, postfixTokens);
        } catch (IllegalArgumentException error) {
            // Keep the ERROR: prefix so MiniCompiler can stop the pipeline cleanly.
            return "ERROR: ICR error - " + error.getMessage();
        }
    }

    private static Assignment parseAssignment(String line) {
        // Remove outer spaces before checking for LET or splitting on equals.
        String normalizedLine = line.trim();

        // The assignment accepts lines such as LET G = a + c.
        // Stage 4 only needs the assignment part, so LET is removed here.
        if (normalizedLine.startsWith("LET ")) {
            normalizedLine = normalizedLine.substring(4).trim();
        }

        // Split once on = so the left side is the target and the right side is the expression.
        String[] parts = normalizedLine.split("=", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("expected an assignment expression");
        }

        // The target is the variable being assigned to, for example M or N.
        String target = parts[0].trim();

        // The expression is the operation that will be translated to TAC.
        String expression = parts[1].trim();

        // A valid assignment needs both sides.
        if (target.isEmpty() || expression.isEmpty()) {
            throw new IllegalArgumentException("assignment target and expression are required");
        }

        // Store the parsed parts together so later methods receive clear inputs.
        return new Assignment(target, expression);
    }

    private static List<String> tokenize(String expression) {
        // Tokens are stored in reading order, for example A/B+C becomes A, /, B, +, C.
        List<String> tokens = new ArrayList<>();
        int index = 0;

        // Walk through the expression character by character.
        while (index < expression.length()) {
            char current = expression.charAt(index);

            // Spaces are allowed for readability and do not become tokens.
            if (Character.isWhitespace(current)) {
                index++;
                continue;
            }

            // Identifiers are letters. This supports single-letter identifiers and words.
            if (Character.isLetter(current)) {
                int start = index;

                // Group consecutive letters into one identifier token.
                while (index < expression.length() && Character.isLetter(expression.charAt(index))) {
                    index++;
                }
                tokens.add(expression.substring(start, index));
                continue;
            }

            // Operators become their own tokens.
            if (isOperator(String.valueOf(current))) {
                tokens.add(String.valueOf(current));
                index++;
                continue;
            }

            // Anything else should have been rejected earlier, but keep this guard for safety.
            throw new IllegalArgumentException("unsupported token '" + current + "'");
        }

        // Return the full token list for postfix conversion.
        return tokens;
    }

    private static List<String> toPostfix(List<String> tokens) {
        // This uses the shunting-yard idea for the assignment's four operators.
        // Postfix form makes it simple to generate TAC with the correct precedence.
        List<String> output = new ArrayList<>();
        Deque<String> operators = new ArrayDeque<>();

        for (String token : tokens) {
            if (isOperator(token)) {
                // Higher-precedence and equal-precedence operators already on the stack
                // should be emitted first. This gives * and / priority over + and -,
                // while keeping same-precedence operators left-to-right.
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    output.add(operators.pop());
                }

                // Keep the current operator until its operands are ready.
                operators.push(token);
            } else {
                // Identifiers go directly to the postfix output.
                output.add(token);
            }
        }

        // Add any operators still waiting on the stack.
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        // Example: A / B + C becomes A B / C +.
        return output;
    }

    private static String toThreeAddressCode(String target, List<String> postfixTokens) {
        // Each generated instruction is stored as a readable TAC string.
        List<String> instructions = new ArrayList<>();

        // The value stack holds identifiers and temporary variables while reading postfix.
        Deque<String> values = new ArrayDeque<>();

        // Temporary variables are named t1, t2, t3, and so on.
        int tempNumber = 1;

        for (String token : postfixTokens) {
            if (!isOperator(token)) {
                // Operands are pushed until an operator tells us to combine two values.
                values.push(token);
                continue;
            }

            // Every binary operator needs a left and right operand.
            if (values.size() < 2) {
                throw new IllegalArgumentException("invalid expression structure");
            }

            // Because this is postfix, the right operand is on top of the stack.
            String right = values.pop();
            String left = values.pop();

            // Create the next temporary variable for this operation.
            String temp = "t" + tempNumber;
            tempNumber++;

            // Emit one TAC instruction, for example t1 = A / B.
            instructions.add(temp + " = " + left + " " + token + " " + right);

            // The temporary result can now be used by later operations.
            values.push(temp);
        }

        // A valid expression should leave exactly one final value on the stack.
        if (values.size() != 1) {
            throw new IllegalArgumentException("invalid expression structure");
        }

        // Finish by assigning the final temporary value to the original target variable.
        instructions.add(target + " = " + values.pop());

        // The assignment output is a single semicolon-separated TAC string.
        return String.join("; ", instructions);
    }

    private static boolean isOperator(String token) {
        // The assignment only recognizes these arithmetic operators.
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private static int precedence(String operator) {
        // Multiplication and division must run before addition and subtraction.
        if (operator.equals("*") || operator.equals("/")) {
            return 2;
        }

        // Addition and subtraction have lower precedence.
        return 1;
    }

    // Small data holder for the two parts of an assignment statement.
    private static class Assignment {
        // Left side of the assignment, for example G, M, or N.
        private final String target;

        // Right side of the assignment, for example A/B+C.
        private final String expression;

        private Assignment(String target, String expression) {
            this.target = target;
            this.expression = expression;
        }
    }
}
