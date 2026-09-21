/**
 * A single Reverse Polish Notation expression, together with the stack machine
 * that evaluates it.
 *
 * @author CS 5040 course staff
 * @version Spring 2023
 */
public class BigNumExpression {
    private String[] expression;
    private Stack<String> operandStack;

    /** TODO: add tab
     * Create an expression from one line of the input file.
     *
     * @param exp
     *            the raw line, already trimmed
     */
    public BigNumExpression(String exp) {
        this.expression = exp.split(" +");
        this.operandStack = new Stack<String>();
    }


    /**
     * Evaluate the expression.
     *
     * @return the value of the expression
     */
    public BigNum parseExpression() {
        for (String current : this.expression) {
            current = current.trim();
            if (current.isEmpty()) {
                continue;
            }

            if (isOperator(current)) {
                // Operands come off the stack in reverse order, so hand them to
                // doOperation swapped to preserve the written ordering.
                String op2 = this.operandStack.pop();
                String op1 = this.operandStack.pop();
                if (op1 == null || op2 == null) {
                    throw new IllegalStateException(
                        "Ran out of operands, but still have operators.");
                }
                BigNum result = BigNum.doOperation(op1, op2, current);
                this.operandStack.push(result.toString());
            }
            else {
                this.operandStack.push(current);
            }
        }

        if (this.operandStack.isEmpty()) {
            throw new IllegalStateException("The expression is empty.");
        }

        String top = this.operandStack.pop();
        if (!this.operandStack.isEmpty()) {
            throw new IllegalStateException(
                "After parsing the entire expression, there were leftover "
                    + "operands. Please check the provided expression.");
        }

        return new BigNum(top);
    }


    /**
     * @param o
     *            a token from the expression
     * @return true if the token is one of the supported operators
     */
    public static boolean isOperator(String o) {
        return o.equals("+") || o.equals("*") || o.equals("^");
    }


    /**
     * Render the expression for echoing to stdout. Operands are printed in
     * normalized form, i.e. with high order zeros removed, which is what the
     * sample output file shows.
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (String token : this.expression) {
            if (token.isEmpty()) {
                continue;
            }

            if (out.length() > 0) {
                out.append(' ');
            }

            if (isOperator(token)) {
                out.append(token);
            }
            else {
                out.append(new BigNum(token).toString());
            }
        }

        return out.toString();
    }
}
