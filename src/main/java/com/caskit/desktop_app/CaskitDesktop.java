package com.caskit.desktop_app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import com.caskit.desktop_app.listeners.MacroKeyListener;
import com.caskit.desktop_app.listeners.MouseListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import com.caskit.desktop_app.app_data.AppData;
import com.caskit.desktop_app.ui.CaptureManager;
import com.caskit.desktop_app.ui.CaskitTrayApp;

import java.util.logging.Level;
import java.util.logging.Logger;


public class CaskitDesktop extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        primaryStage.hide();
    }


    public static void main(String[] args) {
        Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(MacroKeyListener.getDefault());
        GlobalScreen.setEventDispatcher(MacroKeyListener.getDefault());

        GlobalScreen.addNativeMouseListener(MouseListener.getDefault());
        GlobalScreen.addNativeMouseMotionListener(MouseListener.getDefault());

        GlobalScreen.addNativeKeyListener(CaptureManager.getDefault());
        GlobalScreen.addNativeMouseListener(CaptureManager.getDefault());
        GlobalScreen.addNativeMouseMotionListener(CaptureManager.getDefault());

        CaskitTrayApp caskitTrayApp = new CaskitTrayApp();
        AppData.bind(caskitTrayApp);
        AppData.initialize();

        launch(args);
    }


}