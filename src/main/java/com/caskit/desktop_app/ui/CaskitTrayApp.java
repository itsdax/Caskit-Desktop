package com.caskit.desktop_app.ui;


import com.caskit.desktop_app.caskit_api.data.Token;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.Separator;
import dorkbox.systemTray.SystemTray;
import com.caskit.desktop_app.app_data.AppData;
import com.caskit.desktop_app.utils.FileHelper;
import com.caskit.desktop_app.utils.UrlHelper;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Image;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CaskitTrayApp {

    private Menu menu;

    private Menu accountMenu;

    private MenuItem screenshotButton;
    private MenuItem videoButton;
    private MenuItem folderButton;
    private MenuItem settingsButton;
    private MenuItem exitButton;

    private MenuItem loginButton;
    private MenuItem profileButton;
    private MenuItem profileSettingButton;

    public CaskitTrayApp() {


        SystemTray systemTray = SystemTray.get();
        if (systemTray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }

        try {
            systemTray.setImage(loadImage("/images/logo.png", systemTray.getTrayImageSize()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        systemTray.setTooltip("Caskit");

        menu = systemTray.getMenu();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        accountMenu = new Menu("Not logged in", loadImage("/images/logo.png", systemTray.getMenuImageSize()));

        loginButton = new MenuItem("Login", loadImage("/images/tray_icons/key.png", systemTray.getMenuImageSize()), e -> LoginManager.show());
        profileButton = new MenuItem("Profile", loadImage("/images/tray_icons/earth-globe.png", systemTray.getMenuImageSize()));
        profileSettingButton = new MenuItem("Settings", loadImage("/images/tray_icons/phone-book.png", systemTray.getMenuImageSize()));

        accountMenu.add(profileButton);
        accountMenu.add(profileSettingButton);
        accountMenu.add(loginButton);

        profileButton.setEnabled(false);
        profileButton.setCallback(e -> {
            try {
                UrlHelper.openWebpage(new URI("https://caskit.io/profile/" + AppData.getToken().getUsername()));
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        });
        profileSettingButton.setCallback(e -> {
            try {
                UrlHelper.openWebpage(new URI("https://caskit.io/settings/"));
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        screenshotButton = new MenuItem("Take Screenshot", loadImage("/images/tray_icons/screenshot.png", systemTray.getMenuImageSize()), e -> CaptureManager.getDefault().openScreenshotView());
        videoButton = new MenuItem("Capture Video", loadImage("/images/tray_icons/movie.png", systemTray.getMenuImageSize()), e -> CaptureManager.getDefault().openVideoView());
        folderButton = new MenuItem("Open Folder", loadImage("/images/tray_icons/folder.png", systemTray.getMenuImageSize()), e -> FileHelper.openDirectory(AppData.getDefaultCaptureDirectory()));
        settingsButton = new MenuItem("Settings", loadImage("/images/tray_icons/cogwheel.png", systemTray.getMenuImageSize()), e -> SettingsManager.show());
        exitButton = new MenuItem("Exit", loadImage("/images/tray_icons/cancel.png", systemTray.getMenuImageSize()), e -> exit());

        menu.add(accountMenu);
        menu.add(new Separator());
        menu.add(screenshotButton);
        menu.add(videoButton);
        menu.add(folderButton);
        menu.add(settingsButton);
        menu.add(new Separator());
        menu.add(exitButton);

        updateUserView();
    }

    public void updateUserView() {
        Token token = AppData.getToken();

        if (token == null) {
            accountMenu.setText("Not logged in");
            loginButton.setText("Login");
            profileButton.setEnabled(false);
            profileSettingButton.setEnabled(false);
            loginButton.setCallback(e -> LoginManager.show());
            loginButton.setImage(loadImage("/images/tray_icons/key.png", SystemTray.get().getMenuImageSize()));
            return;
        }

        accountMenu.setText("Account: " + token.getUsername());
        profileButton.setEnabled(true);
        profileSettingButton.setEnabled(true);
        loginButton.setText("Logout");
        loginButton.setImage(loadImage("/images/tray_icons/exit.png", SystemTray.get().getMenuImageSize()));
        loginButton.setCallback(e -> AppData.setToken(null));
    }


    public void exit() {
        System.exit(0);
    }

    private Image loadImage(String resourcePath, int size) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(resourcePath)).getScaledInstance(size, -1, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println(resourcePath + " could not be found. Using default image.");
            try {
                return ImageIO.read(getClass().getResourceAsStream("/images/logo.png")).getScaledInstance(size, -1, Image.SCALE_SMOOTH);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

}
