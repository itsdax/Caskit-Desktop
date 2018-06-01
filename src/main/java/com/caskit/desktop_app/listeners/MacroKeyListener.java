package com.caskit.desktop_app.listeners;

import com.caskit.desktop_app.executors.AsyncTaskHandler;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import com.caskit.desktop_app.structures.HotKeyMacro;
import com.caskit.desktop_app.utils.HotKeySequence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;


public class MacroKeyListener extends AbstractExecutorService implements NativeKeyListener {

    private static MacroKeyListener macroKeyListener;

    public static MacroKeyListener getDefault() {
        return macroKeyListener != null ? macroKeyListener : (macroKeyListener = new MacroKeyListener());
    }

    private static final int MAX_MACRO_SEQUENCE_LIST = 50;
    private Set<HotKeyMacro> hotKeyMacroSet;

    private Set<HotKeySequence> sequences;

    private boolean running = false;

    private MacroKeyListener() {
        this.hotKeyMacroSet = new HashSet<>();
        this.sequences = new HashSet<>();
        running = true;
    }

    public void addHotKeyMacro(HotKeyMacro hotKeyMacro) {
        this.getHotKeyMacroSet().add(hotKeyMacro);
    }

    public void clearHotKeyMacros() {
        this.getHotKeyMacroSet().clear();
    }

    public boolean removeHotKeyMacro(HotKeyMacro hotKeyMacro) {
        return this.getHotKeyMacroSet().remove(hotKeyMacro);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        String key = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
//        System.out.println("Typed " + key);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int stack = getSequences().size();

        if (stack > MAX_MACRO_SEQUENCE_LIST) {
            return;
        }

        String key = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
//        System.out.println("Pressed " + key);

        List<HotKeySequence> removeList = new ArrayList<>();
        for (HotKeySequence hotKeySequence : getSequences()) {
            if (!hotKeySequence.nextKey(key)) {
                removeList.add(hotKeySequence);
                continue;
            }

            if (hotKeySequence.isComplete()) {
                consumeEvent(nativeKeyEvent);
                hotKeySequence.stop();
                removeList.add(hotKeySequence);
                AsyncTaskHandler.submit(() -> hotKeySequence.getHotKeyMacro().trigger());
            }
        }
        getSequences().removeAll(removeList);

        List<HotKeySequence> newSequences = new ArrayList<>();
        for (HotKeyMacro hotKeyMacro : hotKeyMacroSet) {
            HotKeySequence hotKeySequence = new HotKeySequence(hotKeyMacro);
            hotKeySequence.start();

            if (hotKeySequence.nextKey(key)) {
                newSequences.add(hotKeySequence);
            } else {
                continue;
            }

            if (hotKeySequence.isComplete()) {
                consumeEvent(nativeKeyEvent);
                hotKeySequence.stop();
                AsyncTaskHandler.submit(() -> hotKeySequence.getHotKeyMacro().trigger());
            }
        }
        getSequences().addAll(newSequences);


    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        String key = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
//        System.out.println("Released " + key);
        sequences.clear();
    }

    private static void consumeEvent(NativeKeyEvent e) {
        try {
            Field f = NativeInputEvent.class.getDeclaredField("reserved");
            f.setAccessible(true);
            f.setShort(e, (short) 0x01);
        }
        catch (Exception ex) {
            System.out.println("Failed to consume event");
            ex.printStackTrace();
        }
    }

    private synchronized Set<HotKeyMacro> getHotKeyMacroSet() {
        return hotKeyMacroSet;
    }

    private synchronized Set<HotKeySequence> getSequences() {
        return sequences;
    }

    public void shutdown() {
        running = false;
    }

    public List<Runnable> shutdownNow() {
        running = false;
        return new ArrayList<Runnable>(0);
    }

    public boolean isShutdown() {
        return !running;
    }

    public boolean isTerminated() {
        return !running;
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return true;
    }

    public void execute(Runnable r) {
        r.run();
    }
}
