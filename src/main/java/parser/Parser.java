package parser;

import structure.Element;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface Parser {
    List<Element> parse() throws IOException;
}
