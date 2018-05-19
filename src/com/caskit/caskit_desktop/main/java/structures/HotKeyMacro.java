package structures;


import java.util.Arrays;

public class HotKeyMacro {

    private String[] commandKeys, keys;
    private HotKeyAction hotKeyAction;

    /**
     *
     * @param commandKeys shift, control, alt, and similar keys
     * @param keys q,w,e,r,t,y keys
     */
    public HotKeyMacro (String[] commandKeys, String[] keys, HotKeyAction hotKeyAction) {
        this.commandKeys = commandKeys;
        this.keys = keys;
        this.hotKeyAction = hotKeyAction;
    }

    public String[] getCommandKeys() {
        return commandKeys;
    }

    public String[] getKeys() {
        return keys;
    }

    public void trigger() {
        hotKeyAction.action();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HotKeyMacro)) {
            return false;
        }

        HotKeyMacro hotKeyMacro = (HotKeyMacro) obj;
        return !Arrays.equals(commandKeys, hotKeyMacro.commandKeys) && !Arrays.equals(keys, hotKeyMacro.keys);
    }

    @Override
    public String toString() {
        return Arrays.toString(commandKeys) + " " + Arrays.toString(keys);
    }
}
