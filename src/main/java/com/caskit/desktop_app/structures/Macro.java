package com.caskit.desktop_app.structures;


import com.caskit.desktop_app.utils.PlatformDetector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Macro {

    // Windows specific
    CTRL_SHIFT_SPACE    (true, false, new String[] {"Ctrl", "Shift"},       new String[]{"Space"}),
    CTRL_ALT_SPACE      (true, false, new String[] {"Ctrl", "Alt"},         new String[]{"Space"}),
    CTRL_SHIFT_A        (true, false, new String[] {"Ctrl", "Shift"},       new String[]{"A"}),
    CTRL_SHIFT_Z        (true, false, new String[] {"Ctrl", "Shift"},       new String[]{"Z"}),
    CTRL_SHIFT_C        (true, false, new String[] {"Ctrl", "Shift"},       new String[]{"C"}),
    CTRL_SHIFT_V        (true, false, new String[] {"Ctrl", "Shift"},       new String[]{"V"}),
    ALT_SPACE           (true, false, new String[] {"Alt"},                 new String[]{"Space"}),
    ALT_C               (true, false, new String[] {"Alt"},                 new String[]{"C"}),
    ALT_V               (true, false, new String[] {"Alt"},                 new String[]{"V"}),
    PRINT_SCREEN        (true, false, new String[] {},                      new String[]{"Print Screen"}),

    // Mac Specific
    CMD_OPTION_SPACE    (false, true, new String[] {"⌘", "⌥"},              new String[]{"␣"}),
    OPTION_SPACE        (false, true, new String[] {"⌥"},                   new String[]{"␣"}),
    CONTROL_SPACE       (false, true, new String[] {"⌃"},                   new String[]{"␣"}),
    CONTROL_A           (false, true, new String[] {"⌃"},                   new String[]{"A"}),
    CONTROL_Z           (false, true, new String[] {"⌃"},                   new String[]{"Z"}),
    CONTROL_C           (false, true, new String[] {"⌃"},                   new String[]{"C"}),
    CONTROL_V           (false, true, new String[] {"⌃"},                   new String[]{"V"}),

    // Windows and Mac


    ;

    private boolean windows, mac;
    private String[] controlKeys, keys;

    Macro(boolean windows, boolean mac, String[] controlKeys, String[] keys) {
        this.windows = windows;
        this.mac = mac;
        this.controlKeys = controlKeys;
        this.keys = keys;
    }

    public HotKeyMacro generateHotKeyMacro(HotKeyAction hotKeyAction) {
        return new HotKeyMacro(this.controlKeys, this.keys, hotKeyAction);
    }

    public static List<Macro> windowsOnly() {
        return Arrays.stream(values()).filter(macro -> macro.windows).collect(Collectors.toList());
    }

    public static List<Macro> macOnly() {
        return Arrays.stream(values()).filter(macro -> macro.mac).collect(Collectors.toList());
    }

    public static List<Macro> platformSpecific() {
        if (PlatformDetector.isMac()) {
            return macOnly();
        }

        if (PlatformDetector.isWindows()) {
            return windowsOnly();
        }

        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return super.toString().replaceAll("_", " + ");
    }

}
