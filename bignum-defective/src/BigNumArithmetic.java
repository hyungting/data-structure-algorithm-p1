/**
 * Entry point for the infinite precision arithmetic package.
 *
 * On my honor:
 *
 * - I have not used source code obtained from another current or former
 * student, or any other unauthorized source, either modified or unmodified.
 *
 * - All source code and documentation used in my program is either my original
 * work, or was derived by me from the source code published in the textbook
 * for this course.
 *
 * - I have not discussed coding details about this project with anyone other
 * than my partner (in the case of a joint submission), instructor, ACM/UPE
 * tutors or the TAs assigned to this course. I understand that I may discuss
 * the concepts of this program with other students, and that another student
 * may help me debug my program so long as neither of us writes anything during
 * the discussion or modifies any computer file during the discussion.
 *
 * - I have violated neither the spirit nor letter of this restriction.
 *
 * @author YOUR NAME HERE
 * @version Spring 2023
 */
public class BigNumArithmetic {

    /**
     * @param args
     *            args[0] is the path to the input file
     */
    public static void main(String[] args) {
        boolean countMode = false;
        String filePath;
        if (args.length == 1) {
            filePath = args[0];
        }
        else if (args.length == 2) {
            if (args[0].equals("--count")) {
                countMode = true;
                filePath = args[1];
            }
            else if (args[1].equals("--count")) {
                countMode = true;
                filePath = args[0];
            }
            else {
                throw new IllegalArgumentException(
                    "Expected 2 arguments: --count and a file name.");
            }
        }
        else {
            throw new IllegalArgumentException(
                "Expected 1 or 2 argument: a file name and whether to use --count mode.");
        }

        if (countMode) {
            OpCounter.reset();
        }

        FileProcessor fp = new FileProcessor(filePath);
        fp.run();

        if (countMode) {
            OpCounter.report();
        }
    }
}
