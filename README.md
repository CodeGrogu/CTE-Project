# Development of a Mini Compiler System Using Java

This repository contains the documentation and starter Java scaffold for the CTE711S Compiler Techniques group assignment. The assignment is to develop a mini compiler system in Java for the provided V-language-style program, checking errors and translating valid lines through the compiler stages.

The Java files provide a compile-ready staged structure. Task 1, Task 2, Task 3, Task 4, and Task 5 logic is implemented.

## Assignment Summary

The mini compiler must process the given program in two ways:

- Iteratively, line by line.
- All at once.

Only the following valid assignment lines should proceed through all seven compiler stages:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`

All other lines should only be checked for errors and should report the error found, if any.

## Planned Implementation

The planned source layout uses `MiniCompiler.java` as the coordinator. It imports and calls separate stage classes from the `stages/` folder so each team member can work on their assigned stage file without editing the main coordinator unnecessarily.

| Stage | Java File | Owner | Status |
| --- | --- | --- | --- |
| Stage 1: Lexical Analysis | `stages/LexicalAnalysis.java` | Pascal | Implemented |
| Stage 2: Syntax Analysis | `stages/SyntaxAnalysis.java` | Pascal | Implemented |
| Stage 3: Semantic Analysis | `stages/SemanticAnalysis.java` | Jaden | Implemented |
| Stage 4: Intermediate Code Representation | `stages/IntermediateCodeRepresentation.java` | Jaden | Implemented |
| Stage 5: Code Generation | `stages/CodeGeneration.java` | Aloys | Implemented |
| Stage 6: Code Optimization | `stages/CodeOptimization.java` | Celine | Implemented |
| Stage 7: Target Machine Code in Binary | `stages/TargetMachineCode.java` | Enoch | Implemented |

## Pascal's Completed Task

Task 1 covers Stage 1 and Stage 2:

- `LexicalAnalysis.java` validates characters and keyword spelling.
- `SyntaxAnalysis.java` checks statement structure, combined operators, digits, and semicolon-at-end errors.
- Both stages return `ERROR: ...` when processing must stop, or the clean source line when processing can continue.

## Jaden's Completed Task

Task 2 covers Stage 3 and Stage 4:

- `SemanticAnalysis.java` checks for disallowed semantic symbols: `%`, `$`, `&`, `<`, and `>`.
- `IntermediateCodeRepresentation.java` converts valid assignment expressions into three-address code.
- Lexical and syntax errors remain Pascal's responsibility in Stage 1 and Stage 2.

## Aloys's Completed Task

Task 3 covers Stage 5:

- `CodeGeneration.java` converts Stage 4 three-address code into simple lower-level instructions.
- The generated instruction format uses `LOAD`, arithmetic operations (`ADD`, `SUB`, `MUL`, `DIV`), and `STORE`.
- Code generation returns `ERROR: Code generation error - ...` if the intermediate code format is invalid.

## Celine's Completed Task

Task 4 covers Stage 6:

- `CodeOptimization.java` performs conservative optimization on generated lower-level code.
- It removes exact duplicate consecutive `LOAD` instructions, which is safe for accumulator-style code.
- It preserves `STORE` and `LOAD` pairs that are needed to keep temporary values correct.


## Compiler Stages

1. Lexical Analysis (Scanner)
2. Syntax Analysis (Parser)
3. Semantic Analysis (Syntactic Analysis)
4. Intermediate Code Representation (ICR)
5. Code Generation (CG)
6. Code Optimization (CO)
7. Target Machine Code (TMC) in Binary

## Task Split

| Task | Assigned Stages | Owner | Documentation |
| --- | --- | --- | --- |
| Task 1 | Stage 1 and Stage 2 | Pascal | [docs/task-01-stages-1-2.md](docs/task-01-stages-1-2.md) |
| Task 2 | Stage 3 and Stage 4 | Jaden | [docs/task-02-stages-3-4.md](docs/task-02-stages-3-4.md) |
| Task 3 | Stage 5 | Aloys | [docs/task-03-stage-5.md](docs/task-03-stage-5.md) |
| Task 4 | Stage 6 | Celine | [docs/task-04-stage-6.md](docs/task-04-stage-6.md) |
| Task 5 | Stage 7 | Enoch | [docs/task-05-stage-7.md](docs/task-05-stage-7.md) |

## Team Members

All team members are Computer Science software developers.

| No. | Name |
| --- | --- |
| 1 | Jaden |
| 2 | Enoch |
| 3 | Aloys |
| 4 | Pascal |
| 5 | Celine |

## Role Tracking

| No. | Name | Role Played |
| --- | --- | --- |
| 1 | Jaden | Task 2: Stage 3 Semantic Analysis and Stage 4 Intermediate Code Representation |
| 2 | Enoch | Task 5: Stage 7 Target Machine Code in Binary |
| 3 | Aloys | Task 3: Stage 5 Code Generation |
| 4 | Pascal | Task 1: Stage 1 Lexical Analysis and Stage 2 Syntax Analysis |
| 5 | Celine | Task 4: Stage 6 Code Optimization |

## Repository Structure

```text
.
|-- .gitignore
|-- LICENSE
|-- MiniCompiler.java
|-- README.md
|-- stages/
|   |-- CodeGeneration.java
|   |-- CodeOptimization.java
|   |-- IntermediateCodeRepresentation.java
|   |-- LexicalAnalysis.java
|   |-- SemanticAnalysis.java
|   |-- SyntaxAnalysis.java
|   `-- TargetMachineCode.java
`-- docs/
    |-- README.md
    |-- assignment-brief.md
    |-- task-01-stages-1-2.md
    |-- task-02-stages-3-4.md
    |-- task-03-stage-5.md
    |-- task-04-stage-6.md
    `-- task-05-stage-7.md
```

## Documentation

Start with [docs/assignment-brief.md](docs/assignment-brief.md) for the summarized assignment rules, then use the five task documents and the project guide for the compiler-stage work.

## Compile and Run

After installing a JDK, compile and run the scaffold with:

```bash
javac -d out MiniCompiler.java stages/*.java
java -cp out MiniCompiler
```
