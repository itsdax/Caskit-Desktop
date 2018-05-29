package com.caskit.desktop_app.ui;

import com.caskit.desktop_app.app_data.AppData;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.caskit.desktop_app.ui.controllers.LoginController;

import java.io.IOException;


public class LoginManager {

    public static void show() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(SettingsManager.class.getResource("/login.fxml"));

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            Stage stage = new Stage();

            LoginController controller = loader.getController();
            controller.setStage(stage);

            stage.getIcons().add(new Image(SettingsManager.class.getResourceAsStream("/images/logo_icon.png")));
            stage.setTitle("Login to Caskit");
            stage.setScene(new Scene(root, 300, 450));

            stage.show();

            int prefX = AppData.getPrefLocationSettingX(), prefY = AppData.getPrefLocationSettingY();
            stage.setX(prefX);
            stage.setY(prefY);

        });
    }

}
