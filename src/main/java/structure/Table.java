package structure;

import java.util.List;

public class Table extends Element {

    public Table() {
        super();
    }

    public int countRows() {
        return childElements.size();
    }
}
