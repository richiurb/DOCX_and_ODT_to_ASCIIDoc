package generator;

import structure.Element;

import java.io.IOException;
import java.util.List;

public interface Generator {
    void create(List<Element> structure) throws IOException;
}
