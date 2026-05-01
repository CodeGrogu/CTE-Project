package stages;

import java.util.ArrayList;
import java.util.List;

// Stage 6: Code Optimization.
// Celine's stage receives the lower-level instruction string from Stage 5.
// Because Stage 5 uses accumulator-style instructions, optimization must be conservative:
// removing the wrong LOAD or STORE can change the result of the calculation.
public class CodeOptimization {

    public static String optimize(String generatedCode) {
        if (generatedCode == null || generatedCode.startsWith("ERROR:")) {
            return generatedCode;
        }

        List<String> instructions = splitInstructions(generatedCode);
        instructions = removeDuplicateConsecutiveLoads(instructions);

        return String.join("; ", instructions);
    }

    private static List<String> splitInstructions(String generatedCode) {
        String[] parts = generatedCode.split(";");
        List<String> instructions = new ArrayList<>();

        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                instructions.add(trimmed);
            }
        }

        return instructions;
    }

    private static List<String> removeDuplicateConsecutiveLoads(List<String> instructions) {
        List<String> optimized = new ArrayList<>();
        String previousInstruction = "";

        for (String instruction : instructions) {
            // This is safe because two identical consecutive LOAD instructions leave
            // the accumulator with the same value as one LOAD instruction.
            if (instruction.startsWith("LOAD ") && instruction.equals(previousInstruction)) {
                continue;
            }

            optimized.add(instruction);
            previousInstruction = instruction;
        }

        return optimized;
    }
}
