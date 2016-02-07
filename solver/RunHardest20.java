package solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RunHardest20 {

    public static void main(String[] args) throws IOException{

        Solver solver = new Solver();

        String[] sudokus = new String[20];

        BufferedReader freader = new BufferedReader(new FileReader("test_data/hardest_20.txt"));

        for (int i = 0; i < sudokus.length; ++i)
            sudokus[i] = freader.readLine().trim();

        long then = System.nanoTime();

        try {
            for (int i = 0; i < 50; ++i) {
                for (int j = 0; j < 20; ++j) {
                    solver.solve(sudokus[j]);
                }
            }
        } catch (RuntimeException e) {
            System.out.println("FAILED");
        }

        long now = System.nanoTime();

        double secs = ((double) (now - then)) / Math.pow(10, 9);
        System.out.printf("Took %.2f seconds\n", secs);
    }
}
