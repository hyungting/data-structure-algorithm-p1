/**
 * An arbitrary precision non-negative integer stored as a linked list of
 * decimal digits, lowest order digit first.
 *
 * @author CS 5040 course staff
 * @version Spring 2023
 */
public class BigNum {

    private LinkedList<Integer> listRep;

    /** Good
     * Create a new BigNum from the given String representation.
     *
     * @param stringRep
     *            the number written most significant digit first
     */
    public BigNum(String stringRep) {
        this.listRep = BigNum.newFromString(stringRep);
        this.stripLeadingZeroes();
    }


    /** Good
     * Create a new BigNum that takes ownership of the given digit list.
     *
     * @param listRep
     *            digits, lowest order first
     */
    public BigNum(LinkedList<Integer> listRep) {
        this.listRep = listRep;
        this.stripLeadingZeroes();
    }


    /** Good but trigger LinkedList.add defect
     * Utility method that takes a String and converts it into LinkedList
     * representation. Digits are stored in reverse order, i.e., the lowest
     * order digit is the head.
     *
     * Example: newFromString("100") =&gt; 0 -&gt; 0 -&gt; 1
     *
     * @param strRep
     *            The string representation of the number
     * @return The number as a LinkedList of Integer
     */
    private static LinkedList<Integer> newFromString(String strRep) {
        LinkedList<Integer> bigNum = new LinkedList<Integer>();
        for (int i = strRep.length() - 1; i >= 0; i--) {
            bigNum.add(Character.getNumericValue(strRep.charAt(i)));
        }

        if (bigNum.getSize() == 0) {
            bigNum.add(0);
        }

        return bigNum;
    }


    /** Good but trigger LinkedList.truncateAfter defect
     * Remove the high order zero digits from this number, leaving a single
     * zero digit if the number is zero.
     */
    public void stripLeadingZeroes() {
        LinkedNode<Integer> lastSeenNonZero = null;
        LinkedNode<Integer> current = this.listRep.getHead();
        while (current != null) {
            if (current.getData() != 0) {
                lastSeenNonZero = current;
            }

            current = current.getNext();
        }

        if (lastSeenNonZero != null) {
            this.listRep.truncateAfter(lastSeenNonZero);
        }
        else {
            this.listRep = BigNum.newFromString("0");
        }
    }


    /** Good
     * @return the digit list backing this number, lowest order digit first
     */
    public LinkedList<Integer> getListRep() {
        return this.listRep;
    }


    @Override
    public String toString() {
        return new StringBuilder(this.listRep.toString()).reverse().toString();
    }


    /** Good
     * Add the specified BigNum to this BigNum.
     *
     * @param other
     *            the number to add
     * @return a new BigNum holding the sum
     */
    public BigNum add(BigNum other) {
        return this.addHelper(this.listRep.getHead(), other.listRep.getHead(),
            0, new LinkedList<Integer>());
    }


    private BigNum addHelper(
        LinkedNode<Integer> dig1,
        LinkedNode<Integer> dig2,
        int carry,
        LinkedList<Integer> result) {
        if (dig1 == null && dig2 == null) {
            // There are no digits left to add.
            // Add the carried over value if any.
            if (carry != 0) {
                result.add(carry);
            }
            return new BigNum(result);
        }
        else if (dig1 == null) {
            // The first number has run out of digits.
            int sum = dig2.getData() + carry;
            result.add(sum % 10);
            return this.addHelper(null, dig2.getNext(), sum / 10, result);
        }
        else if (dig2 == null) {
            // The second number has run out of digits.
            int sum = dig1.getData() + carry;
            result.add(sum % 10);
            return this.addHelper(dig1.getNext(), null, sum / 10, result);
        }
        else {
            // Both numbers still have digits to add.
            int sum = dig1.getData() + dig2.getData() + carry;
            result.add(sum % 10);
            return this.addHelper(dig1.getNext(), dig2.getNext(), sum / 10,
                result);
        }
    }


    /**
     * Multiply this BigNum by the specified BigNum.
     *
     * @param bigNum2
     *            the multiplier
     * @return a new BigNum holding the product
     */
    public BigNum multiply(BigNum bigNum2) {
        BigNum initialResult = new BigNum("0");
        return this.multiplyNumberWithNumber(this.listRep.getHead(),
            bigNum2.listRep.getHead(), 0, initialResult);
    }


    private BigNum multiplyNumberWithNumber(
        LinkedNode<Integer> dig1,
        LinkedNode<Integer> dig2,
        int tens,
        BigNum resultSoFar) {
        if (dig2 == null) {
            return resultSoFar;
        }
        else {
            LinkedList<Integer> digitMultiplicationResult =
                new LinkedList<Integer>();
            for (int i = 0; i < tens; i++) {
                digitMultiplicationResult.add(0);
            }

            resultSoFar = resultSoFar.add(multiplyDigitWithNumber(dig1, dig2
                .getData(), digitMultiplicationResult, 0));
            return multiplyNumberWithNumber(dig1, dig2.getNext(), tens + 1,
                resultSoFar);
        }
    }


    private BigNum multiplyDigitWithNumber(
        LinkedNode<Integer> dig1,
        int dig2,
        LinkedList<Integer> result,
        int carry) {
        if (dig1 == null) {
            if (carry != 0) {
                result.add(carry);
            }
            return new BigNum(result);
        }
        else {
            int product = dig1.getData() * dig2 + carry;
            result.add(product % 10);
            return multiplyDigitWithNumber(dig1.getNext(), dig2, result, product
                / 10);
        }
    }


    /** TODO: returns when (exponent % 2 == 0) is wrong
     * Raise this BigNum to the given power using exponentiation by squaring.
     *
     * @param exponent
     *            a non-negative exponent
     * @return a new BigNum holding the result
     */
    public BigNum exponentiate(int exponent) {
        return exponentiateHelper(this, exponent);
    }


    private BigNum exponentiateHelper(BigNum base, int exponent) {
        if (exponent == 0) {
            return new BigNum("1");
        }

        if (exponent == 1) {
            return base;
        }

        if (exponent % 2 == 0) {
            BigNum res = exponentiateHelper(base, exponent / 2);
            return res.multiply(base);
        }
        else {
            BigNum res = exponentiateHelper(base, (exponent - 1) / 2);
            return res.multiply(res).multiply(base);
        }
    }


    /**
     * @return true if this number is even
     */
    public boolean isEven() {
        return this.listRep.getHead().getData() % 2 == 0;
    }


    /**
     * Apply a binary operator to two operands given in string form.
     *
     * @param op1
     *            the left operand
     * @param op2
     *            the right operand
     * @param operator
     *            one of "+", "*", or "^"
     * @return the result of the operation
     */
    public static BigNum doOperation(String op1, String op2, String operator) {
        BigNum first = new BigNum(op1);
        if (operator.equals("^")) {
            // op2 is guaranteed to fit in an integer
            int exp = Integer.parseInt(op2);
            return first.exponentiate(exp);
        }
        else {
            BigNum second = new BigNum(op2);
            switch (operator) {
                case "+":
                    return first.add(second);
                case "*":
                    return first.multiply(second);
                default:
                    throw new IllegalArgumentException("\"" + operator
                        + "\" is not a supported operator.");
            }
        }
    }


    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BigNum)) {
            return false;
        }

        return this.listRep.equals(((BigNum)other).listRep);
    }


    @Override
    public int hashCode() {
        return this.listRep.hashCode();
    }
}
