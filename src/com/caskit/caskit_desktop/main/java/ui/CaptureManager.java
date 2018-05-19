package ui;


import caskit_api.CaskitApi;
import caskit_api.data.Content;
import caskit_api.data.Token;
import executors.AsyncTaskHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import app_data.AppData;
import recording.screen_capture.ScreenUtils;
import recording.screen_capture.Screenshot;
import structures.CaptureViewState;
import ui.controllers.CaptureRegionController;
import utils.ClipboardUtils;
import utils.Timing;
import utils.UrlHelper;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CaptureManager implements NativeMouseInputListener, NativeKeyListener {

    private static CaptureManager captureView;

    public static CaptureManager getDefault() {
        return captureView != null ? captureView : (captureView = new CaptureManager());
    }

    private Stage previousStage;
    private int initialX, initialY;
    private Point mousePosition;
    private boolean video;

    private CaptureViewState captureState;
    private CaptureRegionController captureController;

    private long lastResize;
    private final double resizeDelay;

    private VideoCaptureManager videoCaptureManager;

    private CaptureManager() {
        this.previousStage = null;
        this.initialX = 0;
        this.initialY = 0;
        this.mousePosition = new Point(0, 0);
        this.captureState = CaptureViewState.NONE;
        this.lastResize = 0L;
        this.resizeDelay = 1000D / 60D;
    }

    public void openScreenshotView() {
        start(false);
    }

    public void openVideoView() {
        start(true);
    }

    private CaptureViewState getCaptureState() {
        return captureState;
    }

    private synchronized void exit() {
        if (captureState == CaptureViewState.NONE) {
            return;
        }

        if (previousStage != null) {
            Platform.runLater(() -> previousStage.close());
        }

        if (videoCaptureManager != null) {
            videoCaptureManager.exit();
        }

        captureState = CaptureViewState.NONE;
        System.out.println("Exit capture mode");
    }

    private synchronized void start(boolean video) {
        if (!AppData.isInitialized()) {
            System.out.println("Application has not initialized yet.");
            return;
        }

        if (captureState != CaptureViewState.NONE) {
            throw new IllegalStateException("Already recording!");
        }

        this.video = video;
        System.out.println("Triggered " + (video ? "video" : "screenshot") + " capture!");
        this.captureState = CaptureViewState.WAITING_FOR_CLICK_REGION;
        launchView();
    }

    private void launchView() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.getIcons().add(new Image(SettingsManager.class.getResourceAsStream("../images/logo.png")));

            previousStage = stage;
            FXMLLoader loader = new FXMLLoader(SettingsManager.class.getResource("../capture.fxml"));

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            stage.initStyle(StageStyle.TRANSPARENT);

            System.out.println("Generating scene...");
            Scene scene = new Scene(root, 60, 60);
            scene.setFill(null);
            stage.setScene(scene);

            stage.setAlwaysOnTop(true);
            stage.setOnCloseRequest(event -> exit());
            stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue && getCaptureState() != CaptureViewState.RECORDING) {
                    System.out.println("Capture focus lost!");
                    exit();
                }
            });

            captureController = loader.getController();
            captureController.setStage(stage);
            captureController.setSelectionStyling(new Color(0, 0, 0, 1), new Color(120, 120, 120, 120), "50%");

            stage.show();
            positionStageNearMouse();
            stage.requestFocus();
        });
    }

    private synchronized void moveStage() {
        if (Timing.timeSince(lastResize) < resizeDelay) {
            return;
        }
        this.lastResize = System.currentTimeMillis();

        switch (captureState) {
            case WAITING_FOR_CLICK_REGION:
                positionStageNearMouse();
                break;

            case WAITING_FOR_RELEASE_REGION:
                int width = Math.max(1, Math.abs(initialX - mousePosition.x));
                int height = Math.max(1, Math.abs(initialY - mousePosition.y));
                int x = Math.min(mousePosition.x, initialX);
                int y = Math.min(mousePosition.y, initialY);
                Platform.runLater(() -> {
                    if (previousStage != null) {
                        previousStage.setWidth(width);
                        previousStage.setHeight(height);
                        previousStage.setX(x);
                        previousStage.setY(y);
                    }
                });
                break;
        }
    }

    private synchronized void positionStageNearMouse() {
        if (captureState != CaptureViewState.WAITING_FOR_CLICK_REGION) {
            return;
        }
        Platform.runLater(() -> {
            previousStage.setX(mousePosition.x - (ScreenUtils.getAverageScreenDimension() * 0.02));
            previousStage.setY(mousePosition.y - (ScreenUtils.getAverageScreenDimension() * 0.02));
            previousStage.setWidth((ScreenUtils.getAverageScreenDimension() * 0.04));
            previousStage.setHeight((ScreenUtils.getAverageScreenDimension() * 0.04));
        });
    }

    private void onRecordButtonClicked() {
        AsyncTaskHandler.submit(() -> {
            int countdown = AppData.getRecordingDelay();
            for (int i = countdown; i > 0; i--) {
                videoCaptureManager.setLabel("Starting in " + i + "...");
                Timing.sleep(1000);
            }
            try {
                videoCaptureManager.startRecording();
            } catch (Exception e) {
                e.printStackTrace();
                //TODO: error handle
            }
            captureController.setSelectionStyling(new Color(0, 0, 0, 0), new Color(255, 2, 8, 175), "0");

            long start = System.currentTimeMillis();
            while (videoCaptureManager.isRecording()) {
                videoCaptureManager.setLabel(Timing.formatDuration(System.currentTimeMillis() - start));
                Timing.sleep(1000);
            }

        });
    }

    private void onStopButtonClicked() {
        exit();
    }

    private void onCancelButtonClicked() {
        exit();
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        String key = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
        if (key != null && (key.equals("Escape") || key.equals("Esc"))) {
            exit();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {

    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        if (captureState == CaptureViewState.WAITING_FOR_CLICK_REGION) {
            this.initialX = nativeMouseEvent.getX();
            this.initialY = nativeMouseEvent.getY();
            captureState = CaptureViewState.WAITING_FOR_RELEASE_REGION;
            moveStage();
            CaptureRegionController controller = captureController;
            if (controller != null) {
                controller.setSelectionStyling(new Color(0, 0, 0, 0), new Color(120, 120, 120, 120), "0");
            }
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        if (captureState != CaptureViewState.WAITING_FOR_RELEASE_REGION) {
            return;
        }

        if (video) {
            captureState = CaptureViewState.RECORDING;
            videoCaptureManager = new VideoCaptureManager((int) previousStage.getX(), (int) previousStage.getY(), (int) previousStage.getWidth(), (int) previousStage.getHeight(),
                    captureStatus -> exit(),
                    this::onRecordButtonClicked,
                    this::onStopButtonClicked,
                    this::onCancelButtonClicked,
                    this::handleFilePostCreation);
        } else {
            Platform.runLater(() -> {
                previousStage.hide();
                try {
                    File file = Screenshot.create(
                            AppData.showMouse(),
                            (int) previousStage.getX(),
                            (int) previousStage.getY(),
                            (int) previousStage.getWidth(),
                            (int) previousStage.getHeight(),
                            AppData.getWorkingDirectory() + File.separator + UUID.randomUUID().toString() + ".png");
                    handleFilePostCreation(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exit();
            });
        }
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        this.mousePosition = nativeMouseEvent.getPoint();
        moveStage();
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
        this.mousePosition = nativeMouseEvent.getPoint();
        moveStage();
    }

    private void handleFilePostCreation(File file) {
        if (AppData.isUploadToCaskit()) {
            Token token = AppData.getToken();
            Notification.display("Uploading file...");
            Content content = token != null ? CaskitApi.get().upload(file, token.getToken()) : CaskitApi.get().upload(file);

            if (content == null) {
                Notification.display("Error on upload.");
                return;
            }

            ClipboardUtils.setClipboard(UrlHelper.getURL(content, AppData.useDirectUrl()));
            Notification.display("Copied to clipboard!");
            UrlHelper.open(content, AppData.useDirectUrl());
        }
    }

}
