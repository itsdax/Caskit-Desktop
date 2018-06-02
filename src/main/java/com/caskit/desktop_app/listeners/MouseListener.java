package com.caskit.desktop_app.listeners;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import com.caskit.desktop_app.ui.Notification;

import java.awt.Point;


public class MouseListener implements NativeMouseInputListener {

    private static MouseListener mouseListener;

    private Point mousePosition;

    public static MouseListener getDefault() {
        return mouseListener != null ? mouseListener : (mouseListener = new MouseListener());
    }

    private MouseListener() {
        this.mousePosition = new Point(0, 0);
    }

    public Point getMousePosition() {
        return mousePosition;
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
//        Point point = nativeMouseEvent.getPoint();
//        System.out.println(point);
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        Point point = nativeMouseEvent.getPoint();
//        System.out.println("Pressed: " + point + " with button " + nativeMouseEvent.getButton());
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        Point point = nativeMouseEvent.getPoint();
//        System.out.println("Released: " + point + " with button " + nativeMouseEvent.getButton());
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        this.mousePosition = nativeMouseEvent.getPoint();
        Notification.setPosition(nativeMouseEvent.getX(), nativeMouseEvent.getY());
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
        this.mousePosition = nativeMouseEvent.getPoint();
        Notification.setPosition(nativeMouseEvent.getX(), nativeMouseEvent.getY());
    }
}
