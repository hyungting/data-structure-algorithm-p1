#!/usr/bin/env bash
# Generate Part C raw measurements. Run from any directory with:
#   bash run_measurements.sh

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DATA_DIR="$SCRIPT_DIR/partc_data"
BIN_DIR="$SCRIPT_DIR/bin"
OUTPUT_FILE="$SCRIPT_DIR/measurements.csv"
LOG_DIR="$SCRIPT_DIR/measurement_logs"
TRIALS=5
SIZES=(25 100 400 1600 3000)

if [[ -e "$OUTPUT_FILE" ]]; then
    echo "Refusing to overwrite existing $OUTPUT_FILE" >&2
    exit 1
fi

if [[ ! -f "$BIN_DIR/BigNumArithmetic.class" ]]; then
    echo "Compile the project before measuring: javac -d bin ..." >&2
    exit 1
fi

if [[ ! -f "$DATA_DIR/exp_sweep.txt" ]]; then
    echo "Missing $DATA_DIR/exp_sweep.txt. Run GenOperands first." >&2
    exit 1
fi

mkdir -p "$LOG_DIR"

for size in "${SIZES[@]}"; do
    for operation in add mul; do
        if [[ ! -f "$DATA_DIR/${operation}_${size}.txt" ]]; then
            echo "Missing $DATA_DIR/${operation}_${size}.txt" >&2
            exit 1
        fi
    done
done

printf '%s\n' \
    'operation,input_file,operand_digits,base,exponent,trial,digit_adds,digit_muls,node_allocs,traversals,wall_seconds' \
    > "$OUTPUT_FILE"

run_measurement() {
    local operation="$1"
    local input_file="$2"
    local input_label="$3"
    local operand_digits="$4"
    local base="$5"
    local exponent="$6"
    local trial="$7"
    local output_log
    local time_log
    local counts
    local adds
    local muls
    local allocs
    local traversals
    local wall_seconds

    output_log="$LOG_DIR/${input_label%.txt}_trial${trial}.out"
    time_log="$LOG_DIR/${input_label%.txt}_trial${trial}.time"

    if [[ -e "$output_log" || -e "$time_log" ]]; then
        echo "Refusing to overwrite existing log for $input_label trial $trial" >&2
        exit 1
    fi

    echo "${operation} ${input_label}, trial ${trial}" >&2
    /usr/bin/time -p -o "$time_log" \
        java -cp "$BIN_DIR" BigNumArithmetic "$input_file" --count \
        > "$output_log"

    counts="$(awk '
        /^#OPCOUNT / {
            for (i = 2; i <= NF; i++) {
                split($i, pair, "=");
                if (pair[1] == "adds") adds = pair[2];
                if (pair[1] == "muls") muls = pair[2];
                if (pair[1] == "allocs") allocs = pair[2];
                if (pair[1] == "traversals") traversals = pair[2];
            }
        }
        END {
            if (adds == "" || muls == "" || allocs == "" || traversals == "") {
                exit 1;
            }
            print adds, muls, allocs, traversals;
        }
    ' "$output_log")"
    read -r adds muls allocs traversals <<< "$counts"
    wall_seconds="$(awk '$1 == "real" { print $2 }' "$time_log")"

    if [[ -z "$wall_seconds" ]]; then
        echo "Could not read wall-clock time for $input_label" >&2
        exit 1
    fi

    printf '%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n' \
        "$operation" "$input_label" "$operand_digits" "$base" "$exponent" \
        "$trial" "$adds" "$muls" "$allocs" "$traversals" "$wall_seconds" \
        >> "$OUTPUT_FILE"

}

for size in "${SIZES[@]}"; do
    for trial in $(seq 1 "$TRIALS"); do
        run_measurement "add" "$DATA_DIR/add_${size}.txt" "add_${size}.txt" \
            "$size" "" "" "$trial"
    done
done

for size in "${SIZES[@]}"; do
    for trial in $(seq 1 "$TRIALS"); do
        run_measurement "multiply" "$DATA_DIR/mul_${size}.txt" "mul_${size}.txt" \
            "$size" "" "" "$trial"
    done
done

while IFS= read -r expression; do
    read -r base exponent _ <<< "$expression"
    exponent_input="$(mktemp "${TMPDIR:-/tmp}/bignum-exp.XXXXXX")"
    printf '%s\n' "$expression" > "$exponent_input"

    for trial in $(seq 1 "$TRIALS"); do
        run_measurement "exponentiate" "$exponent_input" "exp_${exponent}.txt" \
            "" "$base" "$exponent" "$trial"
    done

    rm -f "$exponent_input"
done < "$DATA_DIR/exp_sweep.txt"

echo "Wrote $OUTPUT_FILE" >&2
