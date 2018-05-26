package com.caskit.desktop_app.ui.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class NotificationController extends FxController implements Initializable {

    private static final double MAX_OPACITY = 0.9;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label label;

    private Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPane.setOpacity(MAX_OPACITY);

        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(anchorPane.opacityProperty(), MAX_OPACITY)),
                new KeyFrame(Duration.millis(2500), new KeyValue(anchorPane.opacityProperty(), 0, Interpolator.DISCRETE))
        );
        timeline.setOnFinished(event -> getStage().hide());
    }

    public void setLabelText(String text) {
        Platform.runLater(() -> {
            if (!getStage().isShowing()) {
                getStage().show();
            }
            label.setText(text);
            timeline.stop();
            timeline.play();
        });
    }


}
