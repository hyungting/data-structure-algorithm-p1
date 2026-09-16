/**
 * Instrumentation counters for Part C.
 *
 * DO NOT MODIFY THIS FILE. Submit it exactly as distributed. The grading
 * harness compiles your submission against its own copy, so local changes will
 * be discarded and any code that depends on them will fail to compile.
 *
 * Your job is to call these methods from inside your own arithmetic and list
 * code, at the places described below, and to support the <code>--count</code>
 * command line flag described in the Part C instructions.
 *
 * What each counter means:
 *
 * <ul>
 * <li><b>digitAdd</b> &mdash; one evaluation of a digit-plus-digit-plus-carry
 * expression at one position. Call it once per position in your addition, and
 * once per step of the inner multiplication loop for the carry it adds.
 * <li><b>digitMul</b> &mdash; one single-digit times single-digit product.
 * <li><b>nodeAlloc</b> &mdash; one linked list node created. The easiest correct
 * place for this call is the node constructor itself.
 * <li><b>traverse</b> &mdash; one pointer hop from a node to its successor. Call
 * it once per iteration of every loop that walks a list.
 * </ul>
 *
 * Counters must be incremented by the code that does the work, not estimated
 * afterward. The harness re-runs your program on inputs you have not seen and
 * compares what your counters emit against the numbers in your ANALYSIS.pdf.
 *
 * Whatever convention you choose for borderline cases &mdash; whether the carry
 * add inside multiplication counts as a digitAdd, for instance &mdash; state it
 * in your assumptions and apply it consistently. Being consistent matters more
 * than matching anyone else's convention.
 *
 * @author CS 5040 course staff
 * @version Fall 2026
 */
public final class OpCounter {

    private static long digitAdds;
    private static long digitMuls;
    private static long nodeAllocs;
    private static long traversals;

    private OpCounter() {
        // Static utility class; not instantiable.
    }


    /**
     * Set every counter back to zero. Call this immediately before the work you
     * intend to measure.
     */
    public static void reset() {
        digitAdds = 0;
        digitMuls = 0;
        nodeAllocs = 0;
        traversals = 0;
    }


    /**
     * Record one single-digit addition.
     */
    public static void digitAdd() {
        digitAdds++;
    }


    /**
     * Record one single-digit multiplication.
     */
    public static void digitMul() {
        digitMuls++;
    }


    /**
     * Record the allocation of one list node.
     */
    public static void nodeAlloc() {
        nodeAllocs++;
    }


    /**
     * Record one pointer hop from a node to its successor.
     */
    public static void traverse() {
        traversals++;
    }


    /**
     * @return single-digit additions since the last reset
     */
    public static long getDigitAdds() {
        return digitAdds;
    }


    /**
     * @return single-digit multiplications since the last reset
     */
    public static long getDigitMuls() {
        return digitMuls;
    }


    /**
     * @return list nodes allocated since the last reset
     */
    public static long getNodeAllocs() {
        return nodeAllocs;
    }


    /**
     * @return pointer hops taken since the last reset
     */
    public static long getTraversals() {
        return traversals;
    }


    /**
     * The machine-readable form the grading harness looks for. Do not change
     * the format.
     *
     * @return a single line of the form
     *         <code>#OPCOUNT adds=.. muls=.. allocs=.. traversals=..</code>
     */
    public static String line() {
        return "#OPCOUNT adds=" + digitAdds + " muls=" + digitMuls + " allocs="
            + nodeAllocs + " traversals=" + traversals;
    }


    /**
     * Print {@link #line()} to standard output.
     */
    public static void report() {
        System.out.println(line());
    }
}
