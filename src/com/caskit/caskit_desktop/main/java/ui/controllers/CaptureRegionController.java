package ui.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.Color;
import java.net.URL;
import java.util.ResourceBundle;


public class CaptureRegionController extends FxController implements Initializable {

    private Stage stage;

    @FXML
    private Pane region;

    @FXML
    private ImageView imageView;

    private ChangeListener<Number> changeListenerWidth, changeListenerHeight;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        region.setMouseTransparent(false);
        region.setPickOnBounds(false);

        imageView.setImage(new Image(getClass().getResourceAsStream("../../images/logo.png")));
        imageView.setOpacity(0);

        changeListenerWidth = (observable, oldValue, newValue) -> {
            double height = getStage().getHeight() / 2D;
            double width = getStage().getWidth() / 2D;
            double calculatedWidth = Math.min(height, width);

            imageView.setFitWidth(calculatedWidth);
            imageView.setLayoutX((getStage().getWidth() / 2) - (calculatedWidth/2));
        };

        changeListenerHeight = (observable, oldValue, newValue) -> {
            double height = getStage().getHeight() / 2D;
            double width = getStage().getWidth() / 2D;
            double calculatedHeight = Math.min(height, width);

            imageView.setFitHeight(calculatedHeight);
            imageView.setLayoutY((getStage().getHeight() / 2) - (calculatedHeight/2));
        };

    }

    public void setIconOpacity(double opacity) {
        Platform.runLater(() -> imageView.setOpacity(opacity));
    }

    public void setSelectionStyling(Color backgroundColor, Color borderColor, String borderRadius) {

        region.setStyle(
                "-fx-background-color: " + colorToRGBA(backgroundColor) + ";" +
                "-fx-background-radius: " + borderRadius + ";" +
                "-fx-border-color: " + colorToRGBA(borderColor) + ";" +
                "-fx-border-radius: " + borderRadius + ";" +
                "-fx-border-width: 2px; " +
                "-fx-border-style: dashed;"
        );
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        getStage().widthProperty().addListener(changeListenerWidth);
        getStage().heightProperty().addListener(changeListenerHeight);
    }

    public Stage getStage() {
        return stage;
    }

    private static String colorToRGBA(Color color) {
        return "rgba(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha()/255D + ")";
    }

}
