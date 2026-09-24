/**
 * Small live examples for the project walkthrough.
 */
public class Demo {
    public static void main(String[] args) {
        demonstrateExponentiationBySquaring();
        demonstrateMultiplication();
    }

    private static void demonstrateExponentiationBySquaring() {
        BigNum base = new BigNum("2");
        BigNum result = base.exponentiate(4);

        System.out.println("Exponentiation-by-squaring example");
        System.out.println("  2 ^ 4 = " + result);
    }

    private static void demonstrateMultiplication() {
        BigNum a = new BigNum("999");
        BigNum b = new BigNum("10000");
        BigNum result = a.multiply(b);

        System.out.println("Multiplication example");
        System.out.println("  999 * 10000 = " + result);
    }
}
