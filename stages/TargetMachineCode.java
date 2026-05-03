package stages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Stage 7: Target Machine Code (TMC) in Binary.
// This class turns the accumulator-style Stage 6 output into a tiny binary
// representation so the compiler has one final, readable machine-code form.
public class TargetMachineCode {
    // Each instruction mnemonic gets a fixed 4-bit opcode.
    private static final Map<String, String> OPCODES = new HashMap<>();

    static {
        OPCODES.put("LOAD", "0001");
        OPCODES.put("ADD", "0010");
        OPCODES.put("SUB", "0011");
        OPCODES.put("MUL", "0100");
        OPCODES.put("DIV", "0101");
        OPCODES.put("STORE", "0110");
    }

    public static String generate(String optimizedCode) {
        // If an earlier stage already reported an error, keep it unchanged.
        if (optimizedCode == null || optimizedCode.startsWith("ERROR:")) {
            return optimizedCode;
        }

        try {
            List<String> instructions = splitInstructions(optimizedCode);
            List<String> binaryInstructions = new ArrayList<>();

            for (String instruction : instructions) {
                binaryInstructions.add(encodeInstruction(instruction));
            }

            if (binaryInstructions.isEmpty()) {
                throw new IllegalArgumentException("no optimized instructions found");
            }

            return String.join("; ", binaryInstructions);
        } catch (IllegalArgumentException error) {
            return "ERROR: Target machine code error - " + error.getMessage();
        }
    }

    private static List<String> splitInstructions(String optimizedCode) {
        String[] parts = optimizedCode.split(";");
        List<String> instructions = new ArrayList<>();

        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                instructions.add(trimmed);
            }
        }

        return instructions;
    }

    private static String encodeInstruction(String instruction) {
        // Stage 6 produces simple two-part instructions like "LOAD A".
        String[] parts = instruction.split("\\s+", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("invalid instruction '" + instruction + "'");
        }

        String mnemonic = parts[0].trim();
        String operand = parts[1].trim();

        if (mnemonic.isEmpty() || operand.isEmpty()) {
            throw new IllegalArgumentException("instruction and operand are required");
        }

        String opcode = OPCODES.get(mnemonic);
        if (opcode == null) {
            throw new IllegalArgumentException("unsupported instruction '" + mnemonic + "'");
        }

        return opcode + " " + encodeOperand(operand);
    }

    private static String encodeOperand(String operand) {
        // Each character is converted to 8-bit ASCII so identifiers and temporaries
        // both produce a binary-looking final output.
        StringBuilder binary = new StringBuilder();

        for (int index = 0; index < operand.length(); index++) {
            if (index > 0) {
                binary.append(' ');
            }

            binary.append(toEightBitBinary(operand.charAt(index)));
        }

        return binary.toString();
    }

    private static String toEightBitBinary(char value) {
        String binary = Integer.toBinaryString(value);
        StringBuilder paddedBinary = new StringBuilder();

        for (int index = binary.length(); index < 8; index++) {
            paddedBinary.append('0');
        }

        paddedBinary.append(binary);
        return paddedBinary.toString();
    }
}
