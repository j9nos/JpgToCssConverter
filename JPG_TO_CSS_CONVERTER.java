import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class JPG_TO_CSS_CONVERTER {

    private static BufferedImage bufferedImage = null;

    public static void main(String[] args) {

        String inputJPG = "triangle.jpg";
        String outputHTML = "index.html";

        try {

            loadImage(inputJPG);
            writeIntoHTML(outputHTML);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    static void loadImage(String inputJPG) throws IOException {
        bufferedImage = ImageIO.read(new File(inputJPG));
    }

    static void writeIntoHTML(String outputHTML) throws IOException {
        FileWriter file = new FileWriter(outputHTML);
        file.write("<style>\n\t#drawing {\n\t\tposition:absolute;\n\t\tbox-shadow:\n");
        int i=0;
        for (int x = 0; x < bufferedImage.getWidth(); ++x) {
            for (int y = 0; y < bufferedImage.getHeight(); ++y) {
                Color currentPixel = new Color(bufferedImage.getRGB(x, y), false);
                char commaOrSemicolon = i+1 == bufferedImage.getWidth()*bufferedImage.getHeight() ? ';' : ',';
                file.write(String.format("\t\t\t%dpx %dpx 1px 1px rgb(%d, %d, %d)%c\n", x, y, currentPixel.getRed(),
                        currentPixel.getGreen(), currentPixel.getBlue(), commaOrSemicolon));
                        i++;
            }
        }
        file.write("\t}\n</style>\n<body>\n\t<div id='drawing'>\n</div>\n</body>");
        file.close();
    }
}