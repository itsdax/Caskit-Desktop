package utils;

import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class FileHelper {

    public static void openDirectory(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openDirectory(String directory) {
        openDirectory(new File(directory));
    }

    public static File saveImage(BufferedImage bufferedImage, String fileName) throws IOException {
        File file = new File(fileName);
        ImageIO.write(bufferedImage, "png", file);
        return file;
    }

}
