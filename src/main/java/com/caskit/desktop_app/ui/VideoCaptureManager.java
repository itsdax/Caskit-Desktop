package com.caskit.desktop_app.ui;


import com.caskit.desktop_app.callback.CaptureCallback;
import com.caskit.desktop_app.callback.FileCallback;
import com.caskit.desktop_app.callback.GeneralCallback;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.caskit.desktop_app.app_data.AppData;
import com.caskit.desktop_app.recording.audio_capture.InputDevices;
import com.caskit.desktop_app.recording.video_capture.VideoCapturer;
import com.caskit.desktop_app.structures.CaptureStatus;
import com.caskit.desktop_app.ui.controllers.VideoControlsController;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

class VideoCaptureManager {

    private Stage stage;
    private VideoControlsController videoControlsController;
    private VideoCapturer videoCapturer;

    private int x, y, width, height;
    private CaptureCallback captureCallback;
    private FileCallback fileCallback;

    private boolean initialized, recording, closed;

    VideoCaptureManager(int x, int y, int width, int height, CaptureCallback captureCallback,
                        GeneralCallback onRecordButtonClicked, GeneralCallback onStopButtonClicked, GeneralCallback onCancelButtonClicked,
                        FileCallback fileCallback) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.captureCallback = captureCallback;
        this.fileCallback = fileCallback;

        this.initialized = false;
        this.recording = false;

        Platform.runLater(() -> {
            this.stage = new Stage();
            stage.getIcons().add(new Image(SettingsManager.class.getResourceAsStream("/images/logo.png")));

            FXMLLoader loader = new FXMLLoader(SettingsManager.class.getResource("/video_tools.fxml"));

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            stage.initStyle(StageStyle.TRANSPARENT);

            Scene scene = new Scene(root, 350, 55);
            scene.setFill(null);
            stage.setScene(scene);

            stage.setAlwaysOnTop(true);

            videoControlsController = loader.getController();
            videoControlsController.setStage(stage);
            videoControlsController.setLabelText("Initializing...");

            videoControlsController.setOnRecordClickedCallback(onRecordButtonClicked);
            videoControlsController.setOnStopClickedCallback(onStopButtonClicked);
            videoControlsController.setOnCancelClickedCallback(onCancelButtonClicked);

            stage.show();
            stage.setOnCloseRequest(event -> {
                close(CaptureStatus.USER_FORCE_CLOSE);
            });

            stage.setX(x + width - (width/2) - (scene.getWidth()/2));
            stage.setY(y + height + 5);

            stage.requestFocus();
            videoControlsController.setLabelText("Waiting for user input");
            initialized = true;
            closed = false;
        });
    }

    synchronized void startRecording() {
        if (closed) {
            throw new IllegalStateException("Capture manager is closed.");
        }
        if (!initialized) {
            throw new IllegalStateException("Capture manager has not initialized...");
        }
        if (recording) {
            throw new IllegalStateException("Recording has already started!");
        }
        initialized = true;
        recording = true;
        try {
            videoCapturer = new VideoCapturer(
                    AppData.showMouse(),
                    AppData.getWorkingDirectory() + File.separator + UUID.randomUUID().toString() + ".mp4",
                    AppData.isIncludeAudio() ? InputDevices.getTargetLineFromName(AppData.getLineInfo()) : null,
                    AppData.getFPS(),
                    x + 2, y + 2, width - 4, height - 4,
                    progress -> {
                        Notification.display("Processing... (" +  (progress.frame) + ")");
                    });
            videoCapturer.start();
        } catch (Exception e) {
            e.printStackTrace();
            close(CaptureStatus.FAIL);
        }
    }

    void cancelRecording() {

    }

    void stopRecording() {
        if (!initialized) {
            return;
        }
        recording = false;
        if (videoCapturer != null) {
            if (videoCapturer.isStarted()) {
                videoCapturer.stop(file -> fileCallback.trigger(file));
                close(CaptureStatus.SUCCESS);
            } else {
                close(CaptureStatus.USER_FORCE_CLOSE);
            }
        } else {
            close(CaptureStatus.USER_FORCE_CLOSE);
        }
    }

    void exit() {
        if (isRecording()) {
            stopRecording();
        }
        close(null);
    }

    boolean isRecording() {
        return recording;
    }

    void setLabel(String label) {
        if (!initialized) {
            return;
        }
        videoControlsController.setLabelText(label);
    }

    private void close(CaptureStatus captureStatus) {
        closed = true;
        if (!initialized) {
            return;
        }
        if (stage != null) {
            Platform.runLater(() -> stage.close());
        }

        if (captureStatus != null) {
            CaptureCallback callback = captureCallback;
            if (callback != null) {
                callback.trigger(captureStatus);
            }
        }
    }

}
