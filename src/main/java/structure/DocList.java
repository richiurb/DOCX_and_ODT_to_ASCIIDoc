package structure;

public class DocList extends Element {
    private final String typeList;

    public DocList(String typeList) {
        super();
        this.typeList = typeList;
    }

    public String getTypeList() {
        return typeList;
    }
}
