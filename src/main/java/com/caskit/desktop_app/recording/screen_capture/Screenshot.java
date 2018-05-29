package com.caskit.desktop_app.recording.screen_capture;

import com.caskit.desktop_app.utils.FileHelper;
import com.caskit.desktop_app.utils.PlatformDetector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Screenshot {

    private static Screenshot imageCapture;

    private static Screenshot getInstance() {
        return imageCapture != null ? imageCapture : (imageCapture = new Screenshot());
    }

    private Robot robot;
    private BufferedImage cursor, cursorMac;

    private Screenshot() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        try {
            this.cursor = ImageIO.read(getClass().getResourceAsStream("/images/cursor.png"));
            this.cursorMac = ImageIO.read(getClass().getResourceAsStream("/images/cursor_mac.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage create(int x, int y, int width, int height) {
        return create(false, x, y, width, height);
    }

    public static File create(boolean showMouse, int x, int y, int width, int height, String fileName) throws IOException {
        if (width * height < 4) {
            return null;
        }
        return FileHelper.saveImage(create(showMouse, x, y, width, height), fileName);
    }

    public static BufferedImage create(boolean showMouse, int x, int y, int width, int height) {
        BufferedImage bufferedImage = getInstance().robot.createScreenCapture(new Rectangle(x, y, width, height));

        if (showMouse) {
            PointerInfo pointer = MouseInfo.getPointerInfo();
            int mouseX = (int) pointer.getLocation().getX() - x;
            int mouseY = (int) pointer.getLocation().getY() - y;
            ((Graphics2D) bufferedImage.getGraphics()).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            if (PlatformDetector.isWindows()) {
                bufferedImage.getGraphics().drawImage(getInstance().cursor, mouseX, mouseY, null);
            }
            if (PlatformDetector.isMac()) {
                bufferedImage.getGraphics().drawImage(getInstance().cursorMac, mouseX, mouseY, null);
            }
        }

        return bufferedImage;
    }



}
