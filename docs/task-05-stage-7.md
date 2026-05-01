# Task 5: Stage 7

## Assigned Stage

7. Target Machine Code (TMC) in Binary

## Assigned Team Member

Enoch

## Assigned Java File

- `stages/TargetMachineCode.java`

## Purpose

Task 5 implements the final compiler stage required by the assignment.

## Stage Responsibility

Target Machine Code in Binary represents the final output stage for valid source lines after analysis, intermediate representation, code generation, and optimization have completed.

The implementation belongs in `stages/TargetMachineCode.java`.

## Input

Stage 7 receives semicolon-separated optimized instructions from Stage 6.

Example:

```text
LOAD A; DIV B; STORE t1
```

## Binary Format

Each target-machine instruction uses:

- a 4-bit opcode
- an 8-bit operand code

Supported opcode mappings:

| Instruction | Opcode |
| --- | --- |
| `LOAD` | `0001` |
| `STORE` | `0010` |
| `ADD` | `0011` |
| `SUB` | `0100` |
| `MUL` | `0101` |
| `DIV` | `0110` |

Operand mappings:

- Uppercase identifiers `A` to `Z` use codes `00000001` to `00011010`.
- Lowercase identifiers `a` to `z` use codes `00011011` onward.
- Temporary values such as `t1`, `t2`, and `t3` use codes starting from decimal 101.

Example output:

```text
0001 00000001; 0110 00000010; 0010 01100101
```

If Stage 7 receives invalid optimized instructions, it returns an error beginning with `ERROR: Target machine code error - ...`.

## Stage Boundary

Only valid lines should reach Target Machine Code in Binary:

- `LET G = a + c`
- `M = A/B+C`
- `N = G/H-I+a*B/c`
