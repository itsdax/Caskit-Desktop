package com.caskit.desktop_app.utils;


import com.caskit.desktop_app.app_data.AppData;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class FfmpegLocator {

    private static FfmpegLocator ffmpegLocator;

    private static FfmpegLocator getInstance() {
        return ffmpegLocator != null ? ffmpegLocator : (ffmpegLocator = new FfmpegLocator());
    }

    private FfmpegLocator() {
    }

    public static String getFfmpeg() {
        if (PlatformDetector.isWindows()) {
            return extractFile("/ffmpeg/win32/ffmpeg.exe", "ffmpeg.exe");
        }

        if (PlatformDetector.isMac()) {
            return extractFile("/ffmpeg/macos/ffmpeg", "ffmpeg");
        }

        throw new IllegalStateException("Could not determine operating system.");
    }

    public static String getFfprobe() {
        if (PlatformDetector.isWindows()) {
            return extractFile("/ffmpeg/win32/ffprobe.exe", "ffprobe.exe");
        }

        if (PlatformDetector.isMac()) {
            return extractFile("/ffmpeg/macos/ffprobe", "ffprobe");
        }

        throw new IllegalStateException("Could not determine operating system.");
    }

    private static String getOS() {
        return System.getProperty("os.name", "generic");
    }


    /**
     * @param path
     * @param targetName
     * @return Extracted target path
     */
    private static String extractFile(String path, String targetName) {
        String destination = AppData.getCaskitDirectory() + File.separator + targetName;

        if (new File(destination).exists()) {
            return destination;
        }

        try (
                InputStream inputStream = getInstance().getClass().getResourceAsStream(path);
                OutputStream outStream = new FileOutputStream(new File(destination));
        ) {
            IOUtils.copy(inputStream, outStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return destination;
    }

}
