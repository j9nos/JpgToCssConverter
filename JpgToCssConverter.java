import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class JpgToCssConverter {
    private static final String HTML_BEGIN = "<style>\n\t#drawing {\n\t\tposition:absolute;\n\t\tbox-shadow:\n";
    private static final String HTML_CONTENT = "\t\t\t%dpx %dpx 1px 1px rgb(%d, %d, %d)%c\n";
    private static final String HTML_END = "\t}\n</style>\n<body>\n\t<div id='drawing'></div>\n</body>";
    private static final String ACCEPTED_FILE_FORMAT = ".jpg";
    private static final String OUTPUT_FILE_FORMAT = ".html";
    private static final int RED_SHIFT = 16;
    private static final int GREEN_SHIFT = 8;

    private JpgToCssConverter() {
    }

    public static void convert(final String url) throws Exception {
        validate(url);
        writeToHtml(extensionSwitch(extensionSwitch(url)), createBufferedImage(url));
    }

    private static void validate(final String url) throws Exception {
        if (!url.toLowerCase().endsWith(ACCEPTED_FILE_FORMAT)) {
            throw new Exception("Invalid url");
        }
    }

    private static String extensionSwitch(final String url) {
        return url.toLowerCases().replaceAll(ACCEPTED_FILE_FORMAT, OUTPUT_FILE_FORMAT);
    }

    private static BufferedImage createBufferedImage(final String url) throws IOException {
        return ImageIO.read(new File(url));
    }

    private static void writeToHtml(final String html, final BufferedImage image) {
        try (final FileWriter fileWriter = new FileWriter(html)) {
            fileWriter.write(HTML_BEGIN);
            final int imageWidth = image.getWidth();
            final int imageHeight = image.getHeight();
            final int lastPixel = imageWidth * imageHeight - 1;
            int currentPixel = 0;
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    fileWriter.write(
                            formatToHtml(x, y, image.getRGB(x, y), getSeparator(lastPixel, currentPixel))
                    );
                    currentPixel++;
                }
            }
            fileWriter.write(HTML_END);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static char getSeparator(final int lastPixel, final int currentPixel) {
        return lastPixel == currentPixel ? ';' : ',';
    }

    private static String formatToHtml(final int x, final int y, final int rgb, final char separator) {
        return String.format(
                HTML_CONTENT,
                x,
                y,
                (rgb >> RED_SHIFT) & 0xFF,
                (rgb >> GREEN_SHIFT) & 0xFF,
                (rgb) & 0xFF,
                separator);
    }

}
