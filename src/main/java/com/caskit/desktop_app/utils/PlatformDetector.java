package com.caskit.desktop_app.utils;

import org.apache.commons.lang3.SystemUtils;

public class PlatformDetector {

    public static boolean isMac() {
        return SystemUtils.IS_OS_MAC;
    }

    public static boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }

}
