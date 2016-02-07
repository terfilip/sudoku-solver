package solver;

/**
 * Created by filipt on 12/05/2015.
 */
public class Main {
    /* A bunch of test cases. Some are from Peter Norvig's article
     * others from attractive chaos.
     */
    public static void main(String[] args) {
        String case0 = "003020600900305001001806400008102900700000008006708200002609500800203009005010300";
        String case1 = "4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
        String case2 = "85...24..72......9..4.........1.7..23.5...9...4...........8..7..17..........36.4.";
        String case3 = "..............3.85..1.2.......5.7.....4...1...9.......5......73..2.1........4...9";
        String case4 = "12.3....435....1....4........54..2..6...7.........8.9...31..5.......9.7.....6...8";
        Solver solver = new Solver();

        try {
            System.out.println("Test Case 0:");
            solver.display(case0, System.out);
            System.out.println("Test Case 1:");
            solver.display(case1, System.out);
            System.out.println("Test Case 2:");
            solver.display(case2, System.out);
            stp("Test Case 3:", solver, case4);
            stp("Test Case 4", solver, case3);

        } catch (RuntimeException e) {
            System.out.println("Attempt to solve invalid grid");
            e.printStackTrace();
        }

    }

    public static double stp(String msg, Solver solver, String grid) throws RuntimeException{
        System.out.println(msg);
        long then = System.nanoTime();
        solver.display(grid, System.out);
        long now = System.nanoTime();
        double secs = ((double) (now - then)) / Math.pow(10, 9);
        System.out.printf("Took %.2f seconds\n", secs);
        return secs;
    }
}
