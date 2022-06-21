package structure;

import java.util.LinkedList;
import java.util.List;

public abstract class Element {
    protected final List<Element> childElements;

    public Element() {
        childElements = new LinkedList<>();
    }

    public boolean hasChildElements() {
        return !childElements.isEmpty();
    }

    public List<Element> getChildElements() {
        return childElements.stream().toList();
    }

    public void addChildElement(Element element) {
        childElements.add(element);
    }
}
