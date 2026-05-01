  // Celine: implement Task 4 / Stage 6 code optimization here.
 
package stages;

import java.util.ArrayList;
import java.util.List;

public class CodeOptimization {

    public static String optimize(String generatedCode) {
        if (generatedCode == null || generatedCode.startsWith("ERROR:")) {
            return generatedCode;
        }

        String[] parts = generatedCode.split(";");
        List<String> instructions = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                instructions.add(trimmed);
            }
        }

        instructions = eliminateRedundantLoads(instructions);
        instructions = collapseStoreLoadPairs(instructions);
        instructions = eliminateDeadTemporaries(instructions);

        return String.join("; ", instructions);
    }

    private static List<String> eliminateRedundantLoads(List<String> instructions) {
        List<String> result = new ArrayList<>();
        int i = 0;

        while (i < instructions.size()) {
            String current = instructions.get(i);

            if (current.startsWith("STORE ") && i + 1 < instructions.size()) {
                String storedName = current.substring("STORE ".length()).trim();
                String next = instructions.get(i + 1);

                if (next.equals("LOAD " + storedName)) {
                    result.add(current);
                    i += 2;
                    continue;
                }
            }

            result.add(current);
            i++;
        }

        return result;
    }

    private static List<String> collapseStoreLoadPairs(List<String> instructions) {
        List<String> result = new ArrayList<>();
        int i = 0;

        while (i < instructions.size()) {
            String current = instructions.get(i);

            if (current.startsWith("STORE ") && i + 1 < instructions.size()) {
                String storedName = current.substring("STORE ".length()).trim();
                String next = instructions.get(i + 1);

                if (isTemporary(storedName) && next.equals("LOAD " + storedName)) {
                    i += 2;
                    continue;
                }
            }

            result.add(current);
            i++;
        }

        return result;
    }

    private static List<String> eliminateDeadTemporaries(List<String> instructions) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < instructions.size(); i++) {
            String current = instructions.get(i);

            if (current.startsWith("STORE ")) {
                String storedName = current.substring("STORE ".length()).trim();

                if (isTemporary(storedName) && !usedAfter(storedName, instructions, i + 1)) {
                    continue;
                }
            }

            result.add(current);
        }

        return result;
    }

    private static boolean usedAfter(String name, List<String> instructions, int startIndex) {
        for (int i = startIndex; i < instructions.size(); i++) {
            String[] tokens = instructions.get(i).split("\\s+", 2);
            if (tokens.length == 2 && tokens[1].trim().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isTemporary(String name) {
        return name.matches("t\\d+");
    }
}
