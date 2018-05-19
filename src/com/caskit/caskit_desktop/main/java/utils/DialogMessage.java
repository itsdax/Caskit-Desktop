package utils;


import callback.GeneralCallback;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DialogMessage {

    public static void show(StackPane stackPane, String title, String message) {
        show(stackPane, title, message, null);
    }

    public static void show(StackPane stackPane, String title, String message, GeneralCallback callback) {
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.getStylesheets().add(DialogMessage.class.getResource("../style.css").toExternalForm());
        jfxDialogLayout.setHeading(new Text(title));
        jfxDialogLayout.setBody(new Text(message));

        JFXButton button = new JFXButton("Okay");
        button.getStyleClass().add("caskit-button");
        jfxDialogLayout.setActions(button);

        JFXDialog dialog = new JFXDialog(stackPane, jfxDialogLayout, JFXDialog.DialogTransition.TOP);
        dialog.show();

        button.requestFocus();

        button.setOnAction(event1 -> {
            if (callback != null) {
                callback.trigger();
            }
            dialog.close();
        });
    }

}
