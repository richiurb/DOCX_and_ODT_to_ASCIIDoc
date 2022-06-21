package structure;

public class Image extends Element {
    private final byte[] data;
    private final String imageText;

    public Image(byte[] data, String imageText) {
        super();
        this.data = data;
        this.imageText = imageText;
    }

    public String getImageText() {
        return imageText;
    }
}
