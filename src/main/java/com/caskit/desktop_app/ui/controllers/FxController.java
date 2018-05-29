package com.caskit.desktop_app.ui.controllers;

import javafx.stage.Stage;


public abstract class FxController {

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
