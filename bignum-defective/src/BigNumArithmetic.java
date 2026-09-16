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
        if (args.length != 1) {
            throw new IllegalArgumentException(
                "Expected exactly 1 argument: a file name.");
        }

        String filePath = args[0];
        FileProcessor fp = new FileProcessor(filePath);
        fp.run();
    }
}
