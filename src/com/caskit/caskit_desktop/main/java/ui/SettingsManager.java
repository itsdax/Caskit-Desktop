package ui;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import app_data.AppData;
import ui.controllers.SettingController;

import java.io.IOException;


class SettingsManager {

    private SettingsManager() {

    }

    static void show() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(SettingsManager.class.getResource("../settings.fxml"));

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            Stage stage = new Stage();

            SettingController controller = loader.getController();
            controller.setStage(stage);

            stage.getIcons().add(new Image(SettingsManager.class.getResourceAsStream("../images/logo_icon.png")));
            stage.setTitle("Settings");
            stage.setScene(new Scene(root, 450, 350));

            stage.show();

            int prefX = AppData.getPrefLocationSettingX(), prefY = AppData.getPrefLocationSettingY();
            stage.setX(prefX);
            stage.setY(prefY);

        });
    }

}
