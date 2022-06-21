package generator.asciidoc;

import generator.Generator;
import structure.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ASCIIDocGenerator implements Generator {
    private final String fileName;

    public ASCIIDocGenerator(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void create(List<Element> structure) throws IOException {
        try(FileWriter writer = new FileWriter(fileName + ".adoc", false)) {
            writer.write(generateContent(structure));
        }
    }

    private String generateContent(List<Element> structure) {
        StringBuffer result = new StringBuffer();

        for (var e: structure) {
            if (e instanceof Paragraph currentParagraph) {
                result.append(generateParagraph(currentParagraph));
            }

            if (e instanceof DocList currentDocList) {
                result.append(generateDocList(currentDocList));
            }

            if (e instanceof Table currentTable) {
                result.append(generateTable(currentTable));
            }

            result.append("\n\n");
        }

        return result.toString();
    }

    private StringBuffer generateDocList(DocList docList) {
        StringBuffer resultDocList = new StringBuffer();

        List<Element> childElements = docList.getChildElements();
        for (var e: childElements) {
            resultDocList.append(". ");
            resultDocList.append(generateParagraph((Paragraph) e));
            resultDocList.append("\n");
        }

        return resultDocList;
    }

    private StringBuffer generateTable(Table table) {
        StringBuffer resultTable = new StringBuffer("|===\n");
        int count = 0;

        List<Element> rows = table.getChildElements();

        for (var row: rows) {
            List<Element> cells = row.getChildElements();

            for (var cell: cells) {
                resultTable.append("|");

                for (var paragraph: cell.getChildElements()) {
                    resultTable.append(generateParagraph((Paragraph) paragraph));
                }

                if (count > 0) resultTable.append("\n");
            }

            resultTable.append("\n");
            count++;
        }

        resultTable.append("|===");
        return resultTable;
    }

    private StringBuffer generateParagraph(Paragraph paragraph) {
        StringBuffer resultParagraph = new StringBuffer();

        List<Element> childElements = paragraph.getChildElements();
        for (var ce:
                childElements) {
            if (ce instanceof Text text) {
                resultParagraph.append(generateText(text));
            }

            if (ce instanceof Image image) {
                resultParagraph.append(generateImage(image));
            }
        }

        return resultParagraph;
    }

    private StringBuffer generateText(Text text) {
        StringBuffer resultText = new StringBuffer();

        String quotedSymbols = "";
        if (text.isBold()) quotedSymbols += "**";
        if (text.isItalic()) quotedSymbols += "__";

        resultText.append(quotedSymbols).append(text.getContent()).append(quotedSymbols);

        return resultText;
    }

    private StringBuffer generateImage(Image image) {
        return new StringBuffer(image.getImageText());
    }
}