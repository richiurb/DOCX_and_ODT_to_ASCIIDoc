import generator.asciidoc.ASCIIDocGenerator;
import parser.docx.WordParser;
import structure.Element;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        try (InputStream inputStream =
                Files.newInputStream(Paths.get(System.getProperty("user.dir").concat("\\test.docx")))) {
            List<Element> parsedDocument = new WordParser(inputStream).parse();
            new ASCIIDocGenerator("newtest").create(parsedDocument);
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (Exception e) {
            System.out.println("some error:(");
        }
    }
}
