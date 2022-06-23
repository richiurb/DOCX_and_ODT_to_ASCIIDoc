package structure;

public class Header extends Element {
    private final int importance;

    public Header(int importance) {
        super();
        this.importance = importance;
    }

    public int getImportance() {
        return importance;
    }
}
