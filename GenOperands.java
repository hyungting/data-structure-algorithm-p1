import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;

/**
 * Operand generator for CS 5040 Project 1.
 *
 * <p>
 * Two modes:
 * </p>
 * <ul>
 * <li><b>ladder</b> (default) builds the Part C measurement inputs: addition
 * and multiplication at a range of operand sizes, plus an exponent sweep.</li>
 * <li><b>largemult</b> regenerates the instructor data file
 * {@code LargeMultiplication.txt} and prints the product fingerprints that
 * {@code DefectTest} asserts on, so the file and the test are always
 * regenerated together.</li>
 * </ul>
 *
 * <p>
 * Everything is seeded through {@link java.util.Random}, so the same command
 * always produces the same files on any Java implementation.
 * </p>
 *
 * <p>
 * Usage:
 * </p>
 *
 * <pre>
 * javac GenOperands.java
 * java GenOperands ladder --out partc_data
 * java GenOperands ladder --sizes 25 100 400 --out small
 * java GenOperands largemult --out . --fingerprints
 * </pre>
 *
 * <p>
 * Options: {@code --sizes N [N ...]} operand digit counts (default 25 100 400
 * 1600 3000); {@code --exponents K} sweep 2^1..2^K (default 12);
 * {@code --base B} base for the sweep (default 2); {@code --seed S}
 * reproducibility seed (default 20230205); {@code --out DIR} output directory
 * (default the working directory); {@code --fingerprints} print the length and
 * end digits of each product.
 * </p>
 *
 * <p>
 * <b>Why 3000 digits is the ceiling.</b> The starter's addition,
 * multiplication, and partial-product helpers all recurse once per digit, so
 * stack depth grows with operand length. Multiplication overflows the default
 * JVM stack somewhere between 3800 and 4000 digits, and addition between about
 * 4980 and 5170. The default ladder stops at 3000 to leave roughly 25%
 * headroom. Sizes past that will throw {@code StackOverflowError} until those
 * helpers are converted to iteration, which is item 6 of the Part C analysis.
 * </p>
 *
 * <p>
 * This tool uses {@link java.math.BigInteger} to compute the fingerprints. That
 * is deliberate and is not a model for the assignment: the point of the project
 * is to implement big-number arithmetic yourself, and using BigInteger in a
 * submission would defeat it. Here it serves as an independent check on the
 * BigNum implementation.
 * </p>
 *
 * @author CS 5040 course staff
 * @version Spring 2023
 */
public final class GenOperands {

    /** Default operand digit counts for the Part C ladder. */
    private static final int[] LADDER_SIZES = {25, 100, 400, 1600, 3000};

    /** Operand length past which the starter code overflows the stack. */
    private static final int STACK_CEILING = 3800;

    /** Operand length used by the large multiplication data file. */
    private static final int LARGE_SIZE = 1600;

    /** Number of expressions in the large multiplication data file. */
    private static final int LARGE_COUNT = 2;

    /** Seed fixed for the large multiplication file, independent of --seed. */
    private static final long LARGE_SEED = 20230205L;

    private GenOperands() {
        // Not instantiable.
    }


    /**
     * Builds one decimal string with no leading zero.
     *
     * @param rng
     *            the source of randomness
     * @param digits
     *            how many digits to produce, at least 1
     * @return a decimal string of exactly that many digits
     */
    private static String digits(Random rng, int digits) {
        StringBuilder sb = new StringBuilder(digits);
        sb.append((char)('1' + rng.nextInt(9)));
        for (int i = 1; i < digits; i++) {
            sb.append((char)('0' + rng.nextInt(10)));
        }
        return sb.toString();
    }


    /**
     * Writes one file, one expression per line.
     *
     * @param dir
     *            the output directory
     * @param name
     *            the file name
     * @param lines
     *            the expressions to write
     * @throws IOException
     *             if the file cannot be written
     */
    private static void write(File dir, String name, String[] lines)
        throws IOException {
        File out = new File(dir, name);
        PrintWriter writer = new PrintWriter(out, "UTF-8");
        try {
            for (String line : lines) {
                writer.println(line);
            }
        }
        finally {
            writer.close();
        }
        System.out.println("  " + out.getPath() + "  (" + lines.length
            + " expression(s))");
    }


    /**
     * Builds the Part C measurement ladder.
     *
     * @param opts
     *            the parsed command line
     * @throws IOException
     *             if any file cannot be written
     */
    private static void buildLadder(Options opts) throws IOException {
        opts.out.mkdirs();
        Random rng = new Random(opts.seed);

        for (int n : opts.sizes) {
            if (n > STACK_CEILING) {
                System.err.println("warning: size " + n + " exceeds the ~"
                    + STACK_CEILING + "-digit recursion ceiling of the starter "
                    + "code; expect StackOverflowError");
            }
        }

        System.out.println("addition ladder:");
        for (int n : opts.sizes) {
            String a = digits(rng, n);
            String b = digits(rng, n);
            write(opts.out, "add_" + n + ".txt", new String[] {a + " " + b
                + " +"});
        }

        System.out.println("multiplication ladder:");
        for (int n : opts.sizes) {
            String a = digits(rng, n);
            String b = digits(rng, n);
            write(opts.out, "mul_" + n + ".txt", new String[] {a + " " + b
                + " *"});
        }

        System.out.println("exponent sweep:");
        String[] sweep = new String[opts.exponents];
        for (int k = 1; k <= opts.exponents; k++) {
            sweep[k - 1] = opts.base + " " + (1L << k) + " ^";
        }
        write(opts.out, "exp_sweep.txt", sweep);

        System.out.println();
        System.out.println("Run each file five times rather than duplicating "
            + "lines inside a file:");
        System.out.println("  for i in 1 2 3 4 5; do java BigNumArithmetic "
            + new File(opts.out, "mul_400.txt").getPath() + " --count; done");
        System.out.println("The counters reset per expression, so repeated "
            + "runs give you the five");
        System.out.println("trials Part C asks for while keeping one "
            + "measurement per line of output.");
    }


    /**
     * Regenerates the large multiplication data file and, optionally, the
     * fingerprints that DefectTest asserts on.
     *
     * <p>
     * The seed, operand size, expression count, and digit alphabet are fixed
     * here rather than taken from the command line. Changing any of them
     * changes the products, which would silently invalidate the assertions in
     * {@code DefectTest.testLargeMultiplicationPerformance}. If you do change
     * one, run with {@code --fingerprints} and paste the printed lines into
     * that test.
     * </p>
     *
     * @param opts
     *            the parsed command line
     * @throws IOException
     *             if the file cannot be written
     */
    private static void buildLargeMult(Options opts) throws IOException {
        opts.out.mkdirs();
        Random rng = new Random(LARGE_SEED);

        String[] lines = new String[LARGE_COUNT];
        BigInteger[] products = new BigInteger[LARGE_COUNT];
        for (int i = 0; i < LARGE_COUNT; i++) {
            String a = digits(rng, LARGE_SIZE);
            String b = digits(rng, LARGE_SIZE);
            lines[i] = a + " " + b + " *";
            products[i] = new BigInteger(a).multiply(new BigInteger(b));
        }

        write(opts.out, "LargeMultiplication.txt", lines);

        if (opts.fingerprints) {
            System.out.println();
            System.out.println("fingerprints for "
                + "DefectTest.testLargeMultiplicationPerformance:");
            for (int i = 0; i < products.length; i++) {
                String text = products[i].toString();
                System.out.println("        assertAnswer(actual[" + i + "], "
                    + text.length() + ", \"" + text.substring(0, 20) + "\", \""
                    + text.substring(text.length() - 20) + "\");");
            }
        }
    }


    /**
     * Program entry point.
     *
     * @param args
     *            the command line; see the class comment
     * @throws IOException
     *             if any output file cannot be written
     */
    public static void main(String[] args) throws IOException {
        Options opts = Options.parse(args);
        if (opts == null) {
            System.err.println("usage: java GenOperands [ladder|largemult] "
                + "[--sizes N ...] [--exponents K] [--base B] [--seed S] "
                + "[--out DIR] [--fingerprints]");
            return;
        }

        if ("largemult".equals(opts.mode)) {
            buildLargeMult(opts);
        }
        else {
            buildLadder(opts);
        }
    }


    /**
     * Parsed command line options.
     */
    private static final class Options {

        /** Either "ladder" or "largemult". */
        private String mode = "ladder";

        /** Operand digit counts for the ladder. */
        private int[] sizes = LADDER_SIZES;

        /** Highest power of two in the exponent sweep. */
        private int exponents = 12;

        /** Base used in the exponent sweep. */
        private int base = 2;

        /** Seed for the ladder. */
        private long seed = 20230205L;

        /** Output directory. */
        private File out = new File(".");

        /** Whether to print product fingerprints. */
        private boolean fingerprints;

        private Options() {
            // Populated by parse.
        }


        /**
         * Parses the command line.
         *
         * @param args
         *            the raw arguments
         * @return the parsed options, or null if the command line is invalid
         */
        private static Options parse(String[] args) {
            Options o = new Options();
            int i = 0;
            if (i < args.length && !args[i].startsWith("--")) {
                o.mode = args[i++];
                if (!"ladder".equals(o.mode) && !"largemult".equals(o.mode)) {
                    System.err.println("unknown mode: " + o.mode);
                    return null;
                }
            }

            while (i < args.length) {
                String flag = args[i++];
                if ("--fingerprints".equals(flag)) {
                    o.fingerprints = true;
                }
                else if ("--sizes".equals(flag)) {
                    int start = i;
                    while (i < args.length && !args[i].startsWith("--")) {
                        i++;
                    }
                    if (i == start) {
                        System.err.println("--sizes needs at least one value");
                        return null;
                    }
                    o.sizes = new int[i - start];
                    for (int k = 0; k < o.sizes.length; k++) {
                        o.sizes[k] = Integer.parseInt(args[start + k]);
                    }
                }
                else if (i >= args.length) {
                    System.err.println(flag + " needs a value");
                    return null;
                }
                else if ("--exponents".equals(flag)) {
                    o.exponents = Integer.parseInt(args[i++]);
                }
                else if ("--base".equals(flag)) {
                    o.base = Integer.parseInt(args[i++]);
                }
                else if ("--seed".equals(flag)) {
                    o.seed = Long.parseLong(args[i++]);
                }
                else if ("--out".equals(flag)) {
                    o.out = new File(args[i++]);
                }
                else {
                    System.err.println("unknown option: " + flag);
                    return null;
                }
            }
            return o;
        }
    }
}
