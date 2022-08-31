import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class JPG_TO_CSS_CONVERTER {

    static BufferedImage bufferedImage = null;
    public static void main(String[] args) {
        String inputJPG = "triangle.jpg";
        String outputHTML = "index.html";
        try {
            bufferedImage = loadImage(inputJPG);
            List<Color> pixels = harvestPixels();
            writeIntoHTML(pixels, outputHTML);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    static List<Color> harvestPixels() {
        List<Color> pixels = new ArrayList<>();
        for (int x = 0; x < bufferedImage.getWidth(); ++x) {
            for (int y = 0; y < bufferedImage.getHeight(); ++y) {
                pixels.add(new Color(bufferedImage.getRGB(x, y), false));
            }
        }
        return pixels;
    }

    static void writeIntoHTML(List<Color> pixels, String output) throws IOException {
        FileWriter file = new FileWriter(output);
        file.write("<style>\n\t#drawing {\n\t\tposition:absolute;\n\t\tbox-shadow:\n");
        int currentPixelCounter = 0;
        int x = 0;
        int y = 0;
        char comma = ',';
        for (Color pixel : pixels) {
            if (x == bufferedImage.getWidth()) {
                x = 0;
                y++;
            }
            if (currentPixelCounter + 1 == pixels.size()) {
                comma = ';';
            }
            int r = pixel.getRed();
            int g = pixel.getGreen();
            int b = pixel.getBlue();
            file.write(String.format("\t\t\t%dpx %dpx 1px 1px rgb(%d, %d, %d)%c\n", y, x, r, g, b, comma));
            x++;
            currentPixelCounter++;
        }
        file.write("\t}\n</style>\n<body>\n\t<div id='drawing'>\n</div>\n</body>");
        file.close();
    }
}