package com.caskit.desktop_app.ui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.caskit.desktop_app.ui.controllers.NotificationController;
import com.caskit.desktop_app.utils.Timing;

import java.io.IOException;


public class Notification {

    private static Notification notification;

    private static Notification getInstance() {
        return notification != null ? notification : (notification = new Notification());
    }

    private NotificationController notificationController;
    private Stage stage;
    private long lastMoved;
    private double delay;

    private Notification() {
        lastMoved = System.currentTimeMillis();
        delay = 1000D/60D;
        initialize();
    }

    private void initialize() {
        Platform.runLater(() -> {
            stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));

            FXMLLoader loader = new FXMLLoader(SettingsManager.class.getResource("/notification.fxml"));

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            stage.initStyle(StageStyle.TRANSPARENT);

            stage.setMaxHeight(0);
            stage.setMaxWidth(0);

            notificationController = loader.getController();
            notificationController.setStage(stage);

            Scene scene = new Scene(root, 150, 25);
            scene.setFill(null);
            stage.setScene(scene);

            stage.setX(10);
            stage.setY(10);

            stage.setAlwaysOnTop(true);
        });

    }

    public static void setPosition(int x, int y) {
        getInstance().moveStage(x + 25, y + 10);
    }

    public static void display(String text) {
        getInstance().displayMessage(text);
    }


    private void moveStage(int x, int y) {
        if (Timing.timeSince(lastMoved) < delay) {
            return;
        }
        lastMoved = System.currentTimeMillis();
        Platform.runLater(() -> {
            getInstance().stage.setX(x);
            getInstance().stage.setY(y);
        });
    }


    private void displayMessage(String text) {
        notificationController.setLabelText(text);
    }

}
