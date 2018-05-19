package ui.controllers;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import app_data.AppData;
import app_data.CaskitPreferences;
import recording.audio_capture.InputDevices;
import structures.Macro;
import utils.DialogMessage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingController extends FxController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private Tab keybindingsTab;

    @FXML
    private JFXToggleButton visualizeCursorCheckbox;

    @FXML
    private JFXComboBox<String> audioInputCombobox;

    @FXML
    private JFXToggleButton directUrlCheckbox;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXSlider fpsSlider;

    @FXML
    private JFXComboBox<Macro> screenshotHotkeyCombobox;

    @FXML
    private JFXToggleButton audioCheckbox;

    @FXML
    private JFXToggleButton openAfterCompleteCheckbox;

    @FXML
    private JFXSlider recordingDelaySlider;

    @FXML
    private JFXComboBox<Macro> recordHotkeyCombobox;

    @FXML
    private Tab generalTab;

    @FXML
    private Tab videoTab;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXTextField workingDirectoryField;

    @FXML
    private JFXToggleButton uploadCheckbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        screenshotHotkeyCombobox.setItems(FXCollections.observableArrayList(Macro.platformSpecific()));
        recordHotkeyCombobox.setItems(FXCollections.observableArrayList(Macro.platformSpecific()));
        audioInputCombobox.setItems(FXCollections.observableArrayList(InputDevices.getLineNames()));

        rootPane.heightProperty().addListener((observable, oldValue, newValue) -> rootAnchorPane.setPrefHeight(newValue.doubleValue()));
        rootPane.widthProperty().addListener((observable, oldValue, newValue) -> rootAnchorPane.setPrefWidth(newValue.doubleValue()));

        workingDirectoryField.setOnMouseClicked(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = new File(workingDirectoryField.getText() != null ? workingDirectoryField.getText() : AppData.CASKIT_APP_DIR);
            file.mkdirs();
            directoryChooser.setInitialDirectory(file);
            directoryChooser.setTitle("Select directory");
            File selectedFile = directoryChooser.showDialog(getStage());
            if (selectedFile != null) {
                workingDirectoryField.setText(selectedFile.getAbsolutePath());
            }
        });

        load(AppData.getUserPreferences());
    }


    @FXML
    void saveAction(ActionEvent event) {
        save();
        AppData.update();
        AppData.setPrefLocationSettingX((int) getStage().getX());
        AppData.setPrefLocationSettingY((int) getStage().getY());

        DialogMessage.show(rootPane, "Success", "Saved preferences!", () -> getStage().hide());
    }

    @FXML
    void cancelClicked(ActionEvent event) {
        getStage().hide();
    }

    private void load(CaskitPreferences caskitPreferences) {
        screenshotHotkeyCombobox.getSelectionModel().select(caskitPreferences.getScreenshotMacro());
        recordHotkeyCombobox.getSelectionModel().select(caskitPreferences.getRecordMacro());

        audioCheckbox.setSelected(caskitPreferences.isIncludeAudio());
        audioInputCombobox.getSelectionModel().select(caskitPreferences.getAudioInput());
        fpsSlider.setValue(caskitPreferences.getFps());
        recordingDelaySlider.setValue(caskitPreferences.getRecordingDelay());

        workingDirectoryField.setText(caskitPreferences.getWorkingDirectory());
        openAfterCompleteCheckbox.setSelected(caskitPreferences.isOpenAfterComplete());
        directUrlCheckbox.setSelected(caskitPreferences.isUseDirectURL());
        visualizeCursorCheckbox.setSelected(caskitPreferences.isVisualizeCursor());
        uploadCheckbox.setSelected(caskitPreferences.isUploadToCaskit());
    }

    private void save() {
        CaskitPreferences caskitPreferences = new CaskitPreferences(
                screenshotHotkeyCombobox.getSelectionModel().getSelectedItem(),
                recordHotkeyCombobox.getSelectionModel().getSelectedItem(),
                audioCheckbox.isSelected(),
                audioInputCombobox.getSelectionModel().getSelectedItem(),
                (int) fpsSlider.getValue(),
                (int) recordingDelaySlider.getValue(),
                workingDirectoryField.getText(),
                openAfterCompleteCheckbox.isSelected(),
                directUrlCheckbox.isSelected(),
                visualizeCursorCheckbox.isSelected(),
                uploadCheckbox.isSelected()
        );
        AppData.save(caskitPreferences);
    }

}
