package HMS.Control;


import HMS.Model.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LogInController {

    public PasswordField passwordField;
    public TextField usernameField;
    public Button loginButton;


    @FXML
    public void initialize() {
        loginButton.disableProperty().bind(Bindings.or(passwordField.textProperty().isEmpty(), usernameField.textProperty().isEmpty()));
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "اسم مستخدم او كلمة مرور خاطئة\nيرجي التأكد من صحة البيانات", new ButtonType("موافق"));
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.setTitle("خطأ");
        alert.setHeaderText("خطأ في تسجيل الدخول");
        alert.show();
    }

    private void clearLogInForm() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public void logIn() {
        User user = UserFactory.getUser(usernameField.getText(), passwordField.getText());
        if (user == null) {
            showErrorAlert();
            clearLogInForm();
            return;
        }
        Stage newWindow = constructNewWindow(user);
        if (newWindow == null) {
            showErrorAlert();
            clearLogInForm();

            return;
        }
        ((Stage) usernameField.getScene().getWindow()).close();
        newWindow.show();
    }

    private Stage constructNewWindow(User user) {
        FXMLLoader fxmlLoader = getUI(user);
        if (fxmlLoader == null) {
            return null;
        }
        Stage newWindow = new Stage();
        newWindow.setTitle("نظام ادارة المستشفي");
        newWindow.setScene(new Scene(fxmlLoader.getRoot()));
        newWindow.setMaximized(true);
        ((UserController) fxmlLoader.getController()).initialize(user);
        return newWindow;
    }

    private FXMLLoader getUI(User user) {
        FXMLLoader loader = null;
        if (user instanceof ClinicDoctor) {
            loader = new FXMLLoader(getClass().getResource("../View/clinicDoctor.fxml"));
        } else if (user instanceof EmergencyDoctor) {
            loader = new FXMLLoader(getClass().getResource("../View/emergency.fxml"));
        } else if (user instanceof LabDoctor) {
            loader = new FXMLLoader(getClass().getResource("../View/lab.fxml"));
        }  else if (user instanceof Receptionist) {
            loader = new FXMLLoader(getClass().getResource("../View/receptionist.fxml"));
        } else if (user instanceof Admin) {
            loader = new FXMLLoader(getClass().getResource("../View/admin.fxml"));
        }
        if (loader == null) {
            return null;
        }
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println(e);
            loader = null;
        }
        return loader;

    }
}
