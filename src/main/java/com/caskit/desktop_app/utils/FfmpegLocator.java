package com.caskit.desktop_app.utils;


public class FfmpegLocator {

    private static FfmpegLocator ffmpegLocator;
    private static FfmpegLocator getInstance() {return ffmpegLocator != null ? ffmpegLocator : (ffmpegLocator = new FfmpegLocator());}
    private FfmpegLocator() {}

    public static String getFfmpeg() {
        if (PlatformDetector.isWindows()) {
            return getInstance().getClass().getResource("/ffmpeg/win32/ffmpeg.exe").getPath();
        }

        if (PlatformDetector.isMac()) {
            return getInstance().getClass().getResource("/ffmpeg/macos/ffmpeg").getPath();
        }

        throw new IllegalStateException("Could not determine operating system.");
    }

    public static String getFfprobe() {
        if (PlatformDetector.isWindows()) {
            return getInstance().getClass().getResource("/ffmpeg/win32/ffprobe.exe").getPath();
        }

        if (PlatformDetector.isMac()) {
            return getInstance().getClass().getResource("/ffmpeg/macos/ffprobe").getPath();
        }

        throw new IllegalStateException("Could not determine operating system.");
    }

    private static String getOS() {
        return System.getProperty("os.name", "generic");
    }
}
