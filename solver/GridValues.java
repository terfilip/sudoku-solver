package solver;

import java.util.Collection;
import java.util.HashMap;

class GridValues {

    private boolean flag;
    public HashMap<String, String> values;

    public GridValues() {
        super();
        flag = true;
    }

    public GridValues(int n) {
        values = new HashMap<String, String>(n);
        flag = true;
    }

    public GridValues(GridValues gv, boolean flag) {
        values = new HashMap<String, String>(gv.values);
        this.flag = flag;
    }

    public boolean setFlag() {
        return flag = false;
    }

    public boolean flag() {
        return flag;
    }

    public String get(String K) {
        return values.get(K);
    }

    public void put(String K, String V) {
        values.put(K, V);
    }

    public Collection<String> values() {
        return values.values();
    }

}
