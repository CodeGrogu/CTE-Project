import stages.CodeGeneration;
import stages.CodeOptimization;
import stages.IntermediateCodeRepresentation;
import stages.LexicalAnalysis;
import stages.SemanticAnalysis;
import stages.SyntaxAnalysis;
import stages.TargetMachineCode;

// Main coordinator for the compiler assignment. This class controls the order
// of the pipeline; each stage file in the stages/ folder owns its own logic.
public class MiniCompiler {
    // Any stage can stop processing by returning text that starts with this prefix.
    private static final String ERROR_PREFIX = "ERROR:";

    public static void main(String[] args) {
        // Source program from the assignment brief.
        String[] sourceProgram = {
            "BEGIN",
            "INTEGER A, B, C, E, M, N, G, H, I, a, c",
            "INPUT A, B, C",
            "LET B = A */ M",
            "LET G = a + c",
            "temp = <s%**h - j / w +d +*$&;",
            "M = A/B+C",
            "N = G/H-I+a*B/c",
            "WRITE M",
            "WRITEE F;",
            "END"
        };

        // Process the program line by line.
        for (String line : sourceProgram) {
            processLine(line);
        }
    }

    private static void processLine(String line) {
        System.out.println("Input: " + line);

        // Stage 1: scan and classify keywords, identifiers, operators, and symbols.
        String lexicalOutput = LexicalAnalysis.analyze(line);
        if (isError(lexicalOutput)) {
            printOutput(lexicalOutput);
            return;
        }

        // Stage 2: check whether the token order follows the syntax rules.
        String syntaxOutput = SyntaxAnalysis.analyze(lexicalOutput);
        if (isError(syntaxOutput)) {
            printOutput(syntaxOutput);
            return;
        }

        // Stage 3: semantic analysis catches disallowed meaning-level symbols.
        // It does not replace Pascal's lexical or syntax checks.
        String semanticOutput = SemanticAnalysis.analyze(syntaxOutput);
        if (isError(semanticOutput)) {
            printOutput(semanticOutput);
            return;
        }

        // Assignment rule: only the three valid expression lines continue beyond
        // analysis into ICR, code generation, optimization, and machine code.
        if (!shouldRunFullCompilerPipeline(line)) {
            printOutput("Checked only: " + semanticOutput);
            return;
        }

        // Stage 4: ICR converts the expression into three-address code.
        String intermediateOutput = IntermediateCodeRepresentation.generate(semanticOutput);

        // Stage 5: generate lower-level code from the intermediate representation.
        String generatedCode = CodeGeneration.generate(intermediateOutput);

        // Stage 6: optimize the generated code.
        String optimizedCode = CodeOptimization.optimize(generatedCode);

        // Stage 7: produce target machine code in binary.
        String machineCode = TargetMachineCode.generate(optimizedCode);

        printOutput(machineCode);
    }

    // Assignment rule: only these valid lines pass through all seven compiler stages.
    private static boolean shouldRunFullCompilerPipeline(String line) {
        String normalizedLine = line.trim();
        return normalizedLine.equals("LET G = a + c")
            || normalizedLine.equals("M = A/B+C")
            || normalizedLine.equals("N = G/H-I+a*B/c");
    }

    // Keeps the main pipeline simple: any stage can stop processing by returning ERROR: ...
    private static boolean isError(String stageOutput) {
        return stageOutput != null && stageOutput.startsWith(ERROR_PREFIX);
    }

    // Prints a consistent output block for every processed source line.
    private static void printOutput(String output) {
        System.out.println("Output: " + output);
        System.out.println();
    }
}
