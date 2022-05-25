package HMS.Control;

import HMS.Model.User;
import javafx.event.ActionEvent;

public class AdminController implements UserController {
    User user;

    @Override
    public void initialize(User user) {
        this.user = user;
    }

    public void refreshHistorySearchField(ActionEvent actionEvent) {
    }

    public void historySearchPatient(ActionEvent actionEvent) {
    }

    public void clearHistory(ActionEvent actionEvent) {
    }

    public void clearSearch(ActionEvent actionEvent) {
    }

    public void search(ActionEvent actionEvent) {
    }
}
