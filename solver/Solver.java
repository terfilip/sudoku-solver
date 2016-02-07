package solver;

import java.io.*;
import java.util.*;

public class Solver {

    private static final int SIZE = 81;
    private static final String DIGITS = "123456789";
    private static final String ROWS = "ABCDEFGHI";
    private static final List<String> squares = cross(ROWS, DIGITS);
    private static final Map<String, List<List<String>>> units = new HashMap<String, List<List<String>>>(SIZE);
    private static final Map<String, Set<String>> peers = new HashMap<String, Set<String>>(SIZE);
    private static final List<List<String>> unitlist = new ArrayList<List<String>>(27);

    private static List<String> cross(String first, String second) {
        int len = first.length() * second.length();
        List<String> res = new ArrayList<String>(len);

        for (int i = 0; i < first.length(); ++i)
            for (int j = 0; j < second.length(); ++j)
                res.add(first.substring(i, i + 1) + second.substring(j, j + 1));

        return res;
    }

    static {
        for (int i = 0; i < DIGITS.length(); ++i)
            unitlist.add(cross(ROWS, DIGITS.substring(i, i + 1)));

        for (int i = 0; i < ROWS.length(); ++i)
            unitlist.add(cross(ROWS.substring(i, i + 1), DIGITS));

        String[] tmp1 = {"ABC", "DEF", "GHI"};
        String[] tmp2 = {"123", "456", "789"};

        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                unitlist.add(cross(tmp1[i], tmp2[j]));

        for (String s : squares) {
            List<List<String>> tmp3 = new ArrayList<List<String>>();
            Set<String> tmp4 = new HashSet<String>();
            for (List<String> u : unitlist) {
                if (u.contains(s)) {
                    tmp3.add(u);
                }
            }
            units.put(s, tmp3);

            for (List<String> l : units.get(s)) {
                for (String u : l)
                    if (!u.equals(s))
                        tmp4.add(u);
            }
            peers.put(s, tmp4);
        }
    }

    public Solver() {}

    private GridValues search(GridValues values) throws RuntimeException{
        if (!values.flag()) {
            return values;
        }

        boolean solved = true;

        for (String digits : values.values()) {
            if (digits.length() != 1) {
                solved = false;
                break;
            }
        }

        if (solved) {
            return values;
        }

        String minSquare = "";
        int minLen = 9;

        for (String square : squares) {
            String tmp = values.get(square);

            if ((tmp.length() > 1) && (tmp.length() < minLen)) {
                minSquare = square;
                minLen = tmp.length();
            }
        }

        for (char d : values.get(minSquare).toCharArray()) {
            GridValues res = new GridValues(values, values.flag());
            res = search(assign(res, minSquare, Character.toString(d)));
            if (res.flag()) {
                return res;
            }
        }
        //If we get here it means all the values were false, therefore the input grid is invalid
        values.setFlag();
        return values;
    }

    private GridValues parseGrid(String grid) {
        GridValues values = new GridValues(SIZE);

        for (String s : squares) {
            values.put(s, DIGITS);
        }

        for (int i = 0; i < grid.length(); ++i) {
            String cur = grid.substring(i, i + 1);
            if (DIGITS.contains(cur) && !assign(values, squares.get(i), cur).flag()) {
                return values;
            }
        }

        return values;
    }

    private GridValues assign(GridValues values, String s, String d) {
        String otherValues = values.get(s).replace(d, "");

        for (char d2 : otherValues.toCharArray()) {
            if (!eliminate(values, s, Character.toString(d2))) {
                return values;
            }
        }

        return values;
    }

    private boolean eliminate(GridValues values, String s, String d) {

        if (!values.get(s).contains(d))
            return true;

        String newd = values.get(s).replace(d, "");
        values.put(s, newd);

        if (values.get(s).length() == 0) {
            return values.setFlag();
        }
        else if (values.get(s).length() == 1) {
            String d2 = values.get(s);

            for (String s2 : peers.get(s)) {
                if (!eliminate(values, s2, d2)) {
                    return values.setFlag();
                }
            }
        }

        for (List<String> u : units.get(s)) {

            List<String> dplaces = new ArrayList<String>();

            for (String ss : u) {
                if (values.get(ss).contains(d))
                    dplaces.add(ss);
            }

            if (dplaces.size() == 0) {
                return values.setFlag();
            }
            else if (dplaces.size() == 1)
                if (!assign(values, dplaces.get(0), d).flag()) {
                    return false;
                }
        }
        return true;
    }

    public Map<String, String> solutionGrid(String grid) throws RuntimeException{
        GridValues values = search(parseGrid(grid));
        if (!values.flag()) {
            throw new RuntimeException("Unsolvable grid");
        }

        return values.values;

    }

    public String solve(String grid) throws RuntimeException{
        return grid2line(solutionGrid(grid));
    }

    public String grid2line(Map<String, String> values) {
        String line = "";
        for (String s :squares) {
            line += values.get(s);
        }
        return line;
    }

    public void display(String grid, PrintStream outf) throws RuntimeException{
        outf.println(grid2square(solve(grid)));
    }

    public String grid2square(String line) {
        String separator = "------+------+------";

        String res = "";
        for (int i = 0; i < line.length(); ++i) {
            int ii = i + 1;
            res += line.substring(i, i + 1) + " ";
            if (ii % 3 == 0) {
                res += "|";
            }
            if (ii % 9 == 0) {
                res += "\n";
            }
            if (ii % 27 == 0 && (i != line.length() - 1)) {
                res += separator + "\n";
            }
        }
        return res;

    }

    public String grid2str(Map<String, String> values) {
        int width = 0;

        for (String s : squares) {
            int lentmp = values.get(s).length();
            if (lentmp > width) {
                width = lentmp;
            }
        }
        ++width;

        String separator = "";
        for (int i = 0; i < 3; ++i) {
            char[] dashes = new char[width * 3];
            Arrays.fill(dashes, '-');
            String dsh = new String(dashes);
            separator += (i != 2) ? (dsh + "+") : dsh;
        }

        String res = "";

        for (char rr : ROWS.toCharArray()) {
            String row = "";
            String r = Character.toString(rr);
            for (char cc : DIGITS.toCharArray()) {
                String c = Character.toString(cc);
                row += values.get(r+c) + " " + ("36".contains(c) ? "|" : "");
            }
            res += (row + "\n") + ("CF".contains(r) ? separator + "\n" : "");
        }

        return res;
    }
}
