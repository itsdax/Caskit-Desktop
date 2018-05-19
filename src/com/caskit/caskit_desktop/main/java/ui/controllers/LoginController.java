package ui.controllers;

import app_data.AppData;
import caskit_api.CaskitApi;
import caskit_api.data.Token;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import utils.DialogMessage;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController extends FxController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXTextField loginField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginClicked(null);
            }
        });
    }

    @FXML
    void cancelClicked(ActionEvent event) {
        getStage().close();
    }

    @FXML
    void loginClicked(ActionEvent event) {
        try {
            Token token = CaskitApi.get().login(loginField.getText(), passwordField.getText());
            AppData.setToken(token);
            getStage().close();
        } catch (Exception e) {
            DialogMessage.show(stackPane, "Error", e.getMessage());
        }
    }

}
