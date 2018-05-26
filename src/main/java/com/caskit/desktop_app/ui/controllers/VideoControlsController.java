package com.caskit.desktop_app.ui.controllers;

import com.caskit.desktop_app.callback.GeneralCallback;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;


public class VideoControlsController extends FxController implements Initializable {

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton recordButton;

    @FXML
    private Label label;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private ImageView logoImage;

    private GeneralCallback onRecordClickedCallback;
    private GeneralCallback onCancelClickedCallback;
    private GeneralCallback onStopClickedCallback;

    private boolean recording;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setFocusTraversable(false);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(new Color(0,0,0,0.75));
        label.setText("");
        mainPane.setEffect(dropShadow);

        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"), 24, 24, true, true);
        logoImage.setImage(image);
        logoImage.setFitHeight(24);
        logoImage.setFitWidth(24);
        logoImage.setSmooth(true);
    }

    public void setOnRecordClickedCallback(GeneralCallback onRecordClickedCallback) {
        this.onRecordClickedCallback = onRecordClickedCallback;
    }

    public void setOnCancelClickedCallback(GeneralCallback onCancelClickedCallback) {
        this.onCancelClickedCallback = onCancelClickedCallback;
    }

    public void setOnStopClickedCallback(GeneralCallback onStopClickedCallback) {
        this.onStopClickedCallback = onStopClickedCallback;
    }

    public void setLabelText(String text) {
        Platform.runLater(() -> label.setText(text));
    }

    @FXML
    void cancelButtonClicked(ActionEvent event) {
        onCancelClickedCallback.trigger();
    }

    @FXML
    void recordButtonClicked(ActionEvent event) {
        cancelButton.setVisible(false);
        if (recording) {
            recordButton.setText("Done!");
            onStopClickedCallback.trigger();
        } else {
            recordButton.setText("Stop");
            onRecordClickedCallback.trigger();
        }
        recording = !recording;
    }

    public boolean isRecording() {
        return recording;
    }

}
