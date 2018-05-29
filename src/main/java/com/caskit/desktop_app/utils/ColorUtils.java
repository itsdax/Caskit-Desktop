package com.caskit.desktop_app.utils;


import java.awt.Color;


public class ColorUtils {

    public static Color convert(javafx.scene.paint.Color color) {
        return new java.awt.Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
    }

}
