package com.caskit.desktop_app.utils;

import com.caskit.desktop_app.structures.HotKeyMacro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Tracks the Hot Key sequence pressed.
 *
 * Always call initialize() before tracking.
 */
public class HotKeySequence {

    private boolean start;

    private HotKeyMacro hotKeyMacro;
    private List<String> commandKeys, keys, history;

    private long id;

    public HotKeySequence(HotKeyMacro hotKeyMacro) {
        this.hotKeyMacro = hotKeyMacro;
        this.start = false;
        this.id = System.nanoTime();
    }

    public HotKeyMacro getHotKeyMacro() {
        return hotKeyMacro;
    }

    public void start() {
        if (this.start) {
            throw new IllegalStateException("Cannot initialize sequence since it has already started.");
        }
        commandKeys = Arrays.stream(hotKeyMacro.getCommandKeys()).collect(Collectors.toList());
        keys = Arrays.stream(hotKeyMacro.getKeys()).collect(Collectors.toList());
        history = new ArrayList<>();
        this.start = true;
    }

    /**
     * Cleans up references.
     *
     */
    public void stop() {
        this.commandKeys = null;
        this.keys = null;
        this.history = null;
        this.start = false;
    }

    /**
     *
     * @return true if whole sequence is completed.
     */
    public boolean isComplete() {
        if (!start) {
            return false;
        }

        return commandKeys.isEmpty() && keys.isEmpty();
    }

    /**
     *
     * @param key next key pressed.
     * @return false if not following sequence.
     */
    public boolean nextKey(String key) {
        if (!start) {
            throw new IllegalStateException("Uninitialized sequence.");
        }

        if (key == null) {
            return false;
        }

        if (history.contains(key)) {
            return true;
        }

        if (commandKeys.size() > 0) {
            return  processKey(key, commandKeys, history);
        }

        if (keys.size() > 0) {
            return processKey(key, keys, history);
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HotKeySequence that = (HotKeySequence) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    private static boolean processKey(String targetKey, List<String> keys, List<String> history) {
        if (keys.removeIf(s -> s.equals(targetKey))) {
            history.add(targetKey);
            return true;
        }
        return false;
    }

}
