package structure;

public class Image extends Element {
    private final byte[] data;
    private final String imageText;

    public Image(byte[] data, String imageText) {
        super();
        this.data = data;

        if (imageText == null || imageText.isEmpty()) {
            this.imageText = "Рисунок";
        } else {
            this.imageText = imageText;
        }
    }

    public String getImageText() {
        return imageText;
    }
}
