package com.caskit.desktop_app.recording.screen_capture;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class ScreenUtils {


    public static Set<Rectangle> getScreenDimensions() {
        Set<Rectangle> rectangles = new HashSet<>();
        rectangles.addAll(
                Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices())
                        .flatMap(graphicsDevice -> Arrays.stream(graphicsDevice.getConfigurations()))
                        .map(GraphicsConfiguration::getBounds).collect(Collectors.toList()));
        return rectangles;
    }

    public static int getAverageScreenDimension() {
        return (getAverageScreenWidth() + getAverageScreenHeight()) / 2;
    }

    public static int getAverageScreenWidth() {
        return (int) getScreenDimensions().stream().mapToInt(rectangle -> rectangle.width).average().orElse(0);
    }

    public static int getAverageScreenHeight() {
        return (int) getScreenDimensions().stream().mapToInt(rectangle -> rectangle.height).average().orElse(0);
    }

}
