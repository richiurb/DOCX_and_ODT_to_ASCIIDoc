package parser.docx;

import org.apache.poi.xwpf.usermodel.*;
import parser.Parser;
import structure.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class WordParser implements Parser {
    private final InputStream inputStream;
    private DocList docList;

    public WordParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public List<Element> parse() throws IOException {
        XWPFDocument document = new XWPFDocument(inputStream);

        List<IBodyElement> docElements = document.getBodyElements();

        return parseElements(docElements);
    }

    private List<Element> parseElements(List<IBodyElement> docElements) {
        List<Element> result = new LinkedList<>();

        String previousTypeList = "";
        BigInteger previousIdList = new BigInteger(String.valueOf(0));

        for (var de:
                docElements) {
            if (de instanceof XWPFParagraph currentElement) {
                String currentTypeList = currentElement.getNumFmt();
                String currentStyle = currentElement.getStyle();

                if (currentTypeList != null) {
                    if (previousIdList == null || !currentElement.getNumID().equals(previousIdList)) {
                        if (previousIdList != null) {
                            result.add(docList);
                        }

                        docList = new DocList(currentTypeList);
                    }

                    docList.addChildElement(
                            new ListElement(
                                    parseParagraph(currentElement),
                                    currentElement.getNumIlvl().intValue()
                            )
                    );
                } else {
                    if (previousTypeList != null && !previousTypeList.isEmpty()) {
                        result.add(docList);
                    }

                    if (currentElement.getStyle() != null &&
                            (Pattern.matches("\\d", currentElement.getStyle()) ||
                                    Pattern.matches("a\\d", currentElement.getStyle()))) {
                        int importance;

                        if (currentStyle.charAt(0) == 'a') {
                            importance = Integer.parseInt(String.valueOf(currentStyle.charAt(1))) - 2;
                        } else {
                            importance = Integer.parseInt(currentStyle);
                        }

                        Header header = new Header(importance);

                        header.addChildElement(parseParagraph(currentElement));
                        result.add(header);
                    } else {
                        result.add(parseParagraph(currentElement));
                    }
                }

                previousTypeList = currentTypeList;
                previousIdList = currentElement.getNumID();
            }

            if (de instanceof XWPFTable currentElement) {
                result.add(parseTable(currentElement));
            }
        }

        if (previousTypeList != null && !previousTypeList.isEmpty()) {
            result.add(docList);
        }

        return result;
    }

    private Table parseTable(XWPFTable xwpfTable) {
        Table table = new Table();

        List<XWPFTableRow> rows = xwpfTable.getRows();
        for (var row:
                rows) {
            TableRow rowResult = new TableRow();

            List<XWPFTableCell> cells = row.getTableCells();

            for (var cell:
                    cells) {
                TableCell cellResult = new TableCell();

                List<IBodyElement> paragraphs = new ArrayList<>(cell.getParagraphs());
                List<Element> parsedCells = parseElements(paragraphs);

                for (var pc:
                        parsedCells) {
                    cellResult.addChildElement(pc);
                }

                rowResult.addChildElement(cellResult);
            }

            table.addChildElement(rowResult);
        }
        return table;
    }

    private Paragraph parseParagraph(XWPFParagraph xwpfParagraph) {
        List<XWPFRun> listRuns = xwpfParagraph.getRuns();
        Paragraph paragraph = new Paragraph();

        for (var er:
                listRuns) {
            List<XWPFPicture> pictures = er.getEmbeddedPictures();
            if (pictures.size() > 0) {
                for (var p:
                        pictures) {
                    paragraph.addChildElement(parseImage(p));
                }
                continue;
            }

            paragraph.addChildElement(parseRun(er));
        }
        return paragraph;
    }

    private Text parseRun(XWPFRun xwpfRun) {
        String content = xwpfRun.text();
        String color = xwpfRun.getColor();
        boolean bold = xwpfRun.isBold();
        boolean italic = xwpfRun.isItalic();
        boolean strike = xwpfRun.isStrikeThrough();

        return new Text(content, color, bold, italic, strike);
    }

    private Image parseImage(XWPFPicture picture) {
        XWPFPictureData pictureData = picture.getPictureData();
        return new Image(pictureData.getData(), picture.getDescription());
    }
}
