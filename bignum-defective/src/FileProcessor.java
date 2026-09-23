import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Reads an input file one expression per line, evaluates each expression, and
 * echoes the expression followed by its result.
 *
 * @author CS 5040 course staff
 * @version Spring 2023
 */
public class FileProcessor {
    private File infile;

    /**
     * @param filePath
     *            path to the input file
     */
    public FileProcessor(String filePath) {
        this.infile = new File(filePath);
    }


    /**
     * Process every expression in the file. An error in one expression stops
     * that expression only; processing continues with the next line.
     *
     * @param countMode
     *            whether to reset and report counters for each expression
     */
    public void run(boolean countMode) {
        try (Scanner scan = new Scanner(this.infile)) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (countMode) {
                    OpCounter.reset();
                }
                BigNumExpression exp = new BigNumExpression(line);
                System.out.print(exp + " = ");
                try {
                    BigNum result = exp.parseExpression();
                    System.out.println(result.toString());
                }
                catch (IllegalStateException e) {
                    System.out.println();
                }
                if (countMode) {
                    OpCounter.report();
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + this.infile.getPath());
        }
    }

    /**
     * Process every expression without reporting operation counts.
     */
    public void run() {
        this.run(false);
    }
}
