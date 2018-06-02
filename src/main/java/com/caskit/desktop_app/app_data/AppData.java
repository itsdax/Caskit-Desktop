package com.caskit.desktop_app.app_data;


import com.caskit.desktop_app.caskit_api.data.Token;
import com.caskit.desktop_app.listeners.MacroKeyListener;
import net.harawata.appdirs.AppDirsFactory;
import com.caskit.desktop_app.ui.CaptureManager;
import com.caskit.desktop_app.ui.CaskitTrayApp;
import com.caskit.desktop_app.utils.Jsonable;

import java.io.File;
import java.util.prefs.Preferences;

public class AppData {

    public static final String CASKIT_APP_DIR = AppDirsFactory.getInstance().getUserDataDir("CaskitDesktop", "default", "Caskit");

    private static boolean initialized = false;
    private static final String USER_PREF_KEY = "caskit-user-preferences";

    private static CaskitTrayApp caskitTrayApp;

    public static void initialize() {
        CaskitPreferences caskitPreferences = getUserPreferences();
        MacroKeyListener.getDefault().clearHotKeyMacros();
        MacroKeyListener.getDefault().addHotKeyMacro(caskitPreferences.getScreenshotMacro().generateHotKeyMacro(() -> CaptureManager.getDefault().openScreenshotView()));
        MacroKeyListener.getDefault().addHotKeyMacro(caskitPreferences.getRecordMacro().generateHotKeyMacro(() -> CaptureManager.getDefault().openVideoView()));
        initialized = true;
    }

    public static void bind(CaskitTrayApp caskitTrayApp) {
        AppData.caskitTrayApp = caskitTrayApp;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void update() {
        initialize();
    }

    public static void save(CaskitPreferences caskitPreferences) {
        Preferences.userRoot().put(USER_PREF_KEY, caskitPreferences.toJSON());
    }

    public static CaskitPreferences getUserPreferences() {
        String data = Preferences.userRoot().get(USER_PREF_KEY, null);

        if (data == null) {
            return CaskitPreferences.DEFAULT;
        }

        try {
            return Jsonable.fromJSON(data, CaskitPreferences.class);
        } catch (Exception e) {
            e.printStackTrace();
            return CaskitPreferences.DEFAULT;
        }
    }

    public static void setToken(Token token) {
        if (token == null) {
            remove("token");
            caskitTrayApp.updateUserView();
            return;
        }
        put("token", token.toJSON());
        caskitTrayApp.updateUserView();
    }

    public static Token getToken() {
        try {
            return Jsonable.fromJSON(get("token"), Token.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static File getCaskitDirectory() {
        File file = new File(CASKIT_APP_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getDefaultCaptureDirectory() {
        File file = new File(CASKIT_APP_DIR + File.separator + "capture");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static boolean useDirectUrl() {
        return getUserPreferences().isUseDirectURL();
    }

    public static boolean isUploadToCaskit() {
        return getUserPreferences().isUploadToCaskit();
    }

    public static boolean isOpenAfterFinished() {
        return getUserPreferences().isOpenAfterComplete();
    }

    public static String getWorkingDirectory() {
        return getUserPreferences().getWorkingDirectory();
    }

    public static boolean isIncludeAudio() {
        return getUserPreferences().isIncludeAudio();
    }

    public static boolean showMouse() {
        return getUserPreferences().isVisualizeCursor();
    }

    public static String getLineInfo() {
        return getUserPreferences().getAudioInput();
    }

    public static int getFPS() {
        return getUserPreferences().getFps();
    }

    public static int getRecordingDelay() {
        return getUserPreferences().getRecordingDelay();
    }

    public static int getPrefLocationSettingX() {
        String settingX = get("settingX");
        try {
            return Integer.parseInt(settingX);
        } catch (Exception e) {
            return 100;
        }
    }

    public static int getPrefLocationSettingY() {
        String settingY = get("settingY");
        try {
            return Integer.parseInt(settingY);
        } catch (Exception e) {
            return 100;
        }
    }

    public static void setPrefLocationSettingX(int x) {
        put("settingX", x + "");
    }

    public static void setPrefLocationSettingY(int y) {
        put("settingY", y + "");
    }

    public static void put(String key, String value) {
        Preferences.userRoot().put("caskit-" + key, value);
    }

    public static String get(String key) {
        return Preferences.userRoot().get("caskit-" + key, null);
    }

    public static void remove(String key) {
        Preferences.userRoot().remove("caskit-" + key);
    }
}
