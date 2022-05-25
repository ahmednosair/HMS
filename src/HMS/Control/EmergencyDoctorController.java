package HMS.Control;

import HMS.Model.EmergencyDoctor;
import HMS.Model.IDNamePair;
import HMS.Model.User;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmergencyDoctorController implements UserController {

    public TextField pendingOrdersPatientID;
    public Button showPendingOrders;
    public Button endOrder;
    public TableView<PendingOrder> pendingOrdersTable;
    public TableColumn<PendingOrder, String> pendingOrderIDCol;
    public TableColumn<PendingOrder, String> pendingOrderMedNameCol;
    public TableColumn<PendingOrder, String> pendingOrderDoseCol;
    public TableColumn<PendingOrder, String> pendingOrderFacilityCol;
    public TableColumn<PendingOrder, String> pendingOrderDoctorCol;

    public TextField ordersPatientID;
    public Button showOrders;
    public TableView<Order> ordersTable;
    public TableColumn<Order, Integer> orderIDCol;
    public TableColumn<Order, String> orderMedNameCol;
    public TableColumn<Order, String> orderDoseCol;
    public TableColumn<Order, String> orderFacilityCol;
    public TableColumn<Order, String> orderDoctorCol;
    public ComboBox<String> searchMethod;
    public TextField patientSearch;
    public Button search;
    public TextField patientID;
    public TextField patientName;
    public TextField patientNationalID;
    public TextField patientDateOfBirth;
    public TextField patientGender;
    public TextField patientPhone;
    public TextField patientNationality;
    public TextField patientMarital;
    public TextField patientAddress;
    public TextField patientEmail;
    public TextField patientAllergies;
    public TextField patientBloodType;
    public TextField patientBirthPlace;


    private EmergencyDoctor user;
    private AutoCompletionBinding<String> searchAuto;

    public static class PendingOrder {
        private final Integer id;
        private final String medName;
        private final String dose;
        private final String facility;
        private final String doctor;

        public PendingOrder(Integer id, String medName, String dose, String facility, String doctor) {
            this.id = id;
            this.medName = medName;
            this.dose = dose;
            this.facility = facility;
            this.doctor = doctor;
        }

        public Integer getId() {
            return id;
        }

        public String getMedName() {
            return medName;
        }

        public String getDose() {
            return dose;
        }

        public String getFacility() {
            return facility;
        }

        public String getDoctor() {
            return doctor;
        }
    }

    public static class Order {
        private final Integer id;
        private final String medName;
        private final String dose;
        private final String facility;
        private final String doctor;

        public Order(Integer id, String medName, String dose, String facility, String doctor) {
            this.id = id;
            this.medName = medName;
            this.dose = dose;
            this.facility = facility;
            this.doctor = doctor;
        }

        public Integer getId() {
            return id;
        }

        public String getMedName() {
            return medName;
        }

        public String getDose() {
            return dose;
        }

        public String getFacility() {
            return facility;
        }

        public String getDoctor() {
            return doctor;
        }


    }


    public void initialize() {

        showPendingOrders.disableProperty().bind(pendingOrdersPatientID.textProperty().isEmpty());
        endOrder.disableProperty().bind(Bindings.isEmpty(pendingOrdersTable.getSelectionModel().getSelectedItems()));
        pendingOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        pendingOrderMedNameCol.setCellValueFactory(new PropertyValueFactory<>("medName"));
        pendingOrderDoseCol.setCellValueFactory(new PropertyValueFactory<>("dose"));
        pendingOrderFacilityCol.setCellValueFactory(new PropertyValueFactory<>("facility"));
        pendingOrderDoctorCol.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        search.disableProperty().bind(patientSearch.textProperty().isEmpty());
        showOrders.disableProperty().bind(ordersPatientID.textProperty().isEmpty());
        orderIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderMedNameCol.setCellValueFactory(new PropertyValueFactory<>("medName"));
        orderDoseCol.setCellValueFactory(new PropertyValueFactory<>("dose"));
        orderFacilityCol.setCellValueFactory(new PropertyValueFactory<>("facility"));
        orderDoctorCol.setCellValueFactory(new PropertyValueFactory<>("doctor"));

    }

    @Override
    public void initialize(User user) {
        this.user = (EmergencyDoctor) user;
    }


    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content, new ButtonType("موافق"));
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.setTitle("خطأ");
        alert.setHeaderText(header);
        alert.show();
    }

    private void showConfirmationAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, new ButtonType("موافق"));
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.setTitle("تأكيد");
        alert.setHeaderText(header);
        alert.show();
    }

    @FXML
    private void refreshSearchField() {
        switch (searchMethod.getValue()) {
            case "بالرقم":
            case "برقم الهوية":
                if (searchAuto != null) {
                    searchAuto.dispose();
                    searchAuto = null;
                }
                break;
            case "بالاسم":
                searchAuto = TextFields.bindAutoCompletion(patientSearch,
                        request -> user.getPatientNamesWith(request.getUserText()));
                searchAuto.setMaxWidth(patientSearch.getWidth());
                break;
        }
        patientSearch.setText("");
    }

    @FXML
    private void searchPatient() {
        String colName;
        switch (searchMethod.getValue()) {
            case "بالرقم":
                colName = "id";
                break;
            case "برقم الهوية":
                colName = "nationalID";
                break;
            case "بالاسم":
                colName = "name";
                break;
            default:
                colName = "";
        }
        Map<String, String> patient = user.getPatientBy(colName, patientSearch.getText());
        if (patient.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "لا يوجد مريض مطابق للبيانات!\nالرجاء التأكد من صحة " +
                    "البيانات.", new ButtonType("موافق"));
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            alert.setTitle("خطأ");
            alert.setHeaderText("خطأ في البحث عن مريض");
            alert.show();
        } else {
            showPatientInfo(patient);
        }
    }

    private void showPatientInfo(Map<String, String> patient) {
        patientID.setText(patient.get("id"));
        patientName.setText(patient.get("name"));
        patientNationalID.setText(patient.get("nationalID"));
        patientDateOfBirth.setText(patient.get("dateOfBirth"));
        patientGender.setText(patient.get("gender"));
        patientPhone.setText(patient.get("phone"));
        patientNationality.setText(patient.get("nationality"));
        patientMarital.setText(patient.get("marital"));
        patientAddress.setText(patient.get("address"));
        patientEmail.setText(patient.get("email"));
        patientAllergies.setText(patient.get("allergies"));
        patientBloodType.setText(patient.get("bloodType"));
        patientBirthPlace.setText(patient.get("birthPlace"));

    }

    @FXML
    private void clearPatient() {
        patientID.setText("");
        patientName.setText("");
        patientNationalID.setText("");
        patientDateOfBirth.setText("");
        patientGender.setText("");
        patientPhone.setText("");
        patientNationality.setText("");
        patientMarital.setText("");
        patientAddress.setText("");
        patientEmail.setText("");
        patientAllergies.setText("");
        patientBloodType.setText("");
        patientBirthPlace.setText("");
    }

    public void searchPendingOrders() {
        List<PendingOrder> pendingOrders = user.getPaidPendingOrdersOf(Integer.parseInt(pendingOrdersPatientID.getText()));
        if (pendingOrders == null) {
            showErrorAlert("خطأ في عرض الطلبات", "يرجي مراجعة البيانات والتأكد من اتصال الشبكة");
            return;
        }
        pendingOrdersTable.setItems(FXCollections.observableList(pendingOrders));
    }

    public void clearPendingOrders() {
        pendingOrdersTable.getItems().clear();

    }

    public void endOrder() {
        if (user.endEmergencyOrder(pendingOrdersTable.getSelectionModel().getSelectedItem().getId())) {
            showConfirmationAlert("تم انهاء الطلب بنجاح", "تم انهاء طلب تنفيذ الادوية بنجاح");
            pendingOrdersTable.getItems().remove(pendingOrdersTable.getSelectionModel().getSelectedItem());
            return;
        }
        showErrorAlert("خطأ في انهاء الطلب", "لم يتم انهاء الطلب بنجاح برجاء مراجعة البيانات");
    }


    public void searchOrders() {
        List<Order> orders = user.getPaidOrdersOf(Integer.parseInt(ordersPatientID.getText()));
        if (orders == null) {
            showErrorAlert("خطأ في عرض الطلبات", "يرجي مراجعة البيانات والتأكد من اتصال الشبكة");
            return;
        }
        ordersTable.setItems(FXCollections.observableList(orders));

    }


    public static class EmergencyOrder {
        private final int id;
        private final SimpleStringProperty medicineName;
        private final SimpleStringProperty facilityName;
        private final int cost;

        public EmergencyOrder(int id, String medicineName, String facilityName, int cost) {
            this.id = id;
            this.medicineName = new SimpleStringProperty(medicineName);
            this.facilityName = new SimpleStringProperty(facilityName);
            this.cost = cost;
        }

        public int getId() {
            return id;
        }

        public String getMedicineName() {
            return medicineName.get();
        }

        public SimpleStringProperty medicineNameProperty() {
            return medicineName;
        }

        public String getFacilityName() {
            return facilityName.get();
        }

        public SimpleStringProperty facilityNameProperty() {
            return facilityName;
        }

        public int getCost() {
            return cost;
        }
    }
}
