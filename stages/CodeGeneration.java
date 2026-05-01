package stages;

import java.util.ArrayList;
import java.util.List;

// Stage 5: Code Generation.
// This class converts Stage 4 three-address code into simple lower-level code.
// The generated form uses an accumulator style: LOAD a value, apply an
// operation, then STORE the result.
public class CodeGeneration {
    public static String generate(String intermediateOutput) {
        // If an earlier stage reports an error, pass it through unchanged so the
        // compiler pipeline can stop consistently.
        if (intermediateOutput.startsWith("ERROR:")) {
            return intermediateOutput;
        }

        try {
            // Stage 4 separates TAC instructions with semicolons.
            String[] threeAddressInstructions = intermediateOutput.split(";");

            // Store each lower-level instruction in order.
            List<String> generatedInstructions = new ArrayList<>();

            for (String instruction : threeAddressInstructions) {
                String cleanInstruction = instruction.trim();

                // Ignore empty instruction parts caused by extra spaces or separators.
                if (cleanInstruction.isEmpty()) {
                    continue;
                }

                generateInstruction(cleanInstruction, generatedInstructions);
            }

            if (generatedInstructions.isEmpty()) {
                throw new IllegalArgumentException("no intermediate instructions found");
            }

            // Keep one line of output so Stage 6 and Stage 7 receive a simple string.
            return String.join("; ", generatedInstructions);
        } catch (IllegalArgumentException error) {
            // Keep the ERROR: prefix so MiniCompiler can stop the pipeline cleanly.
            return "ERROR: Code generation error - " + error.getMessage();
        }
    }

    private static void generateInstruction(String instruction, List<String> generatedInstructions) {
        // Split once on = to separate the result location from the expression.
        String[] assignmentParts = instruction.split("=", 2);
        if (assignmentParts.length != 2) {
            throw new IllegalArgumentException("expected assignment in '" + instruction + "'");
        }

        String target = assignmentParts[0].trim();
        String expression = assignmentParts[1].trim();

        if (target.isEmpty() || expression.isEmpty()) {
            throw new IllegalArgumentException("assignment target and expression are required");
        }

        String[] expressionParts = expression.split("\\s+");

        if (expressionParts.length == 1) {
            // Direct assignment, for example G = t1.
            generatedInstructions.add("LOAD " + expressionParts[0]);
            generatedInstructions.add("STORE " + target);
            return;
        }

        if (expressionParts.length != 3) {
            throw new IllegalArgumentException("invalid expression in '" + instruction + "'");
        }

        String leftOperand = expressionParts[0];
        String operator = expressionParts[1];
        String rightOperand = expressionParts[2];

        // Binary TAC, for example t1 = A / B.
        generatedInstructions.add("LOAD " + leftOperand);
        generatedInstructions.add(toMachineOperation(operator) + " " + rightOperand);
        generatedInstructions.add("STORE " + target);
    }

    private static String toMachineOperation(String operator) {
        switch (operator) {
            case "+":
                return "ADD";
            case "-":
                return "SUB";
            case "*":
                return "MUL";
            case "/":
                return "DIV";
            default:
                throw new IllegalArgumentException("unsupported operator '" + operator + "'");
        }
    }
}
