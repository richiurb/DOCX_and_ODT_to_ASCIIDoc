package structure;

public class ListElement extends Element {
    private final int elementLvl;

    public ListElement(Paragraph paragraph, int elementLvl) {
        super();
        this.elementLvl = elementLvl;

        for (var e: paragraph.getChildElements()) {
            this.addChildElement(e);
        }
    }

    public int getElementLvl() {
        return elementLvl;
    }

    public Paragraph getLikeParagraph() {
        Paragraph paragraph = new Paragraph();

        for (var e: getChildElements()) {
            paragraph.addChildElement(e);
        }

        return paragraph;
    }
}
