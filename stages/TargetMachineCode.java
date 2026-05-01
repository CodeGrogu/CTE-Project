package stages;

import java.util.HashMap;
import java.util.Map;

// Stage 7: Target Machine Code in Binary.
// Enoch's stage converts the optimized lower-level instructions from Stage 6
// into a simple binary representation using opcode bits and operand bits.
public class TargetMachineCode {
    private static final Map<String, String> OPCODES = new HashMap<>();

    static {
        OPCODES.put("LOAD", "0001");
        OPCODES.put("STORE", "0010");
        OPCODES.put("ADD", "0011");
        OPCODES.put("SUB", "0100");
        OPCODES.put("MUL", "0101");
        OPCODES.put("DIV", "0110");
    }

    public static String generate(String optimizedCode) {
        if (optimizedCode == null || optimizedCode.startsWith("ERROR:")) {
            return optimizedCode;
        }

        try {
            String[] instructions = optimizedCode.split(";");
            StringBuilder binaryOutput = new StringBuilder();

            for (String instruction : instructions) {
                String cleanInstruction = instruction.trim();
                if (cleanInstruction.isEmpty()) {
                    continue;
                }

                if (binaryOutput.length() > 0) {
                    binaryOutput.append("; ");
                }

                binaryOutput.append(convertInstruction(cleanInstruction));
            }

            if (binaryOutput.length() == 0) {
                throw new IllegalArgumentException("no optimized instructions found");
            }

            return binaryOutput.toString();
        } catch (IllegalArgumentException error) {
            return "ERROR: Target machine code error - " + error.getMessage();
        }
    }

    private static String convertInstruction(String instruction) {
        String[] parts = instruction.split("\\s+");

        if (parts.length != 2) {
            throw new IllegalArgumentException("invalid instruction '" + instruction + "'");
        }

        String opcode = OPCODES.get(parts[0]);
        if (opcode == null) {
            throw new IllegalArgumentException("unsupported instruction '" + parts[0] + "'");
        }

        return opcode + " " + operandToBinary(parts[1]);
    }

    private static String operandToBinary(String operand) {
        if (operand.matches("t\\d+")) {
            int tempNumber = Integer.parseInt(operand.substring(1));
            return toEightBitBinary(100 + tempNumber);
        }

        if (operand.length() == 1 && Character.isLetter(operand.charAt(0))) {
            char letter = operand.charAt(0);
            if (Character.isUpperCase(letter)) {
                return toEightBitBinary(letter - 'A' + 1);
            }
            return toEightBitBinary(letter - 'a' + 27);
        }

        return toEightBitBinary(Math.abs(operand.hashCode()) % 256);
    }

    private static String toEightBitBinary(int value) {
        String binary = Integer.toBinaryString(value & 0xFF);
        return String.format("%8s", binary).replace(' ', '0');
    }
}
