package HMS.Control;

import HMS.Model.IDNamePair;
import HMS.Model.Receptionist;
import HMS.Model.User;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReceptionistController implements UserController {


    public TextField servicePatientID;
    public ComboBox<IDNamePair> serviceFacility;
    public ComboBox<IDNamePair> serviceDoctor;
    public SearchableComboBox<IDNamePair> service;
    public Button serviceRegister;
    public TableView<Bill> billsTable;
    public TableColumn<Bill, String> billIDCol;
    public TableColumn<Bill, String> amountCol;
    public TableColumn<Bill, String> paidAmountCol;
    public TableColumn<Bill, String> remAmountCol;
    public Button pay;
    public Button searchBills;
    public TextField billPatientID;
    public TextField payAmount;
    public TextField patientDebit;
    public TextField patientID;
    public TextField patientName;
    public TextField patientNationalID;
    public DatePicker patientDateOfBirth;
    public ComboBox<String> patientGender;
    public TextField patientPhone;
    public TextField patientNationality;
    public TextField patientMarital;
    public TextField patientAddress;
    public TextField patientEmail;
    public TextField patientAllergies;
    public TextField patientBloodType;
    public TextField patientBirthPlace;
    public Button addNewPatient;
    public TextField patientSearch;
    public Button updatePatientDetails;
    public TableView<ClinicDoctorController.ProvidedService> pendingServicesTable;
    public TableColumn<ClinicDoctorController.ProvidedService, String> serviceNameCol;
    public TableColumn<ClinicDoctorController.ProvidedService, String> serviceFacilityNameCol;
    public TableView<EmergencyDoctorController.EmergencyOrder> pendingEmergencyOrdersTable;
    public TableColumn<EmergencyDoctorController.EmergencyOrder, String> medicineNameCol;
    public TableColumn<EmergencyDoctorController.EmergencyOrder, String> facilityNameCol;
    public Button registerBill;
    public ComboBox<String> searchMethod;
    public Button search;
    public Button billPrint;
    private Receptionist user;
    private AutoCompletionBinding<String> searchAuto;


    public static class PatientInfo {
        private int id;
        private String name;
        private String nationalID;
        private String gender;
        private LocalDate dateOfBirth;
        private File IDScan;
        private String address;
        private String email;
        private String phone;
        private String nationality;
        private String marital;
        private String allergies;
        private String bloodType;
        private String birthPlace;

        public PatientInfo() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNationalID() {
            return nationalID;
        }

        public void setNationalID(String nationalID) {
            this.nationalID = nationalID;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }


        public File getIDScan() {
            return IDScan;
        }

        public void setIDScan(File IDScan) {
            this.IDScan = IDScan;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getMarital() {
            return marital;
        }

        public void setMarital(String marital) {
            this.marital = marital;
        }

        public String getAllergies() {
            return allergies;
        }

        public void setAllergies(String allergies) {
            this.allergies = allergies;
        }

        public String getBloodType() {
            return bloodType;
        }

        public void setBloodType(String bloodType) {
            this.bloodType = bloodType;
        }

        public String getBirthPlace() {
            return birthPlace;
        }

        public void setBirthPlace(String birthPlace) {
            this.birthPlace = birthPlace;
        }


    }

    @Override
    public void initialize(User user) {
        this.user = (Receptionist) user;
    }

    @FXML
    public void initialize() {
        search.disableProperty().bind(Bindings.or(searchMethod.valueProperty().isNull(), patientSearch.textProperty().isEmpty()));
        addNewPatient.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(patientID.textProperty(), patientName.textProperty(), patientNationalID.textProperty(), patientDateOfBirth.valueProperty(), patientGender.valueProperty(), patientPhone.textProperty(), patientNationality.textProperty(), patientMarital.textProperty(), patientAddress.textProperty(), patientEmail.textProperty(), patientAllergies.textProperty(), patientBirthPlace.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return !patientID.getText().isEmpty() || patientName.getText().isEmpty() || patientNationalID.getText().isEmpty() || patientDateOfBirth.getValue() == null || patientGender.getValue() == null || patientPhone.getText().isEmpty() || patientNationality.getText().isEmpty() || patientMarital.getText().isEmpty() || patientAddress.getText().isEmpty() || patientEmail.getText().isEmpty() || patientAllergies.getText().isEmpty() || patientBirthPlace.getText().isEmpty() ;
            }
        });
        updatePatientDetails.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(patientID.textProperty(), patientName.textProperty(), patientNationalID.textProperty(), patientDateOfBirth.valueProperty(), patientGender.valueProperty(), patientPhone.textProperty(), patientNationality.textProperty(), patientMarital.textProperty(), patientAddress.textProperty(), patientEmail.textProperty(), patientAllergies.textProperty(), patientBirthPlace.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return patientID.getText().isEmpty() || patientName.getText().isEmpty() || patientNationalID.getText().isEmpty() || patientDateOfBirth.getValue() == null || patientGender.getValue() == null || patientPhone.getText().isEmpty() || patientNationality.getText().isEmpty() || patientMarital.getText().isEmpty() || patientAddress.getText().isEmpty() || patientEmail.getText().isEmpty() || patientAllergies.getText().isEmpty() || patientBirthPlace.getText().isEmpty() ;
            }
        });
        searchBills.disableProperty().bind(billPatientID.textProperty().isEmpty());
        pay.disableProperty().bind(Bindings.or(payAmount.textProperty().isEmpty(), Bindings.isEmpty(billsTable.getSelectionModel().getSelectedItems())));
        billsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        billIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paidAmountCol.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        remAmountCol.setCellValueFactory(new PropertyValueFactory<>("remAmount"));
        serviceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        serviceFacilityNameCol.setCellValueFactory(new PropertyValueFactory<>("facilityName"));
        medicineNameCol.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        facilityNameCol.setCellValueFactory(new PropertyValueFactory<>("facilityName"));
        registerBill.disableProperty().bind(Bindings.and(Bindings.isEmpty(pendingServicesTable.getSelectionModel().getSelectedItems()), Bindings.isEmpty(pendingEmergencyOrdersTable.getSelectionModel().getSelectedItems())));
        serviceRegister.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(servicePatientID.textProperty(), serviceFacility.valueProperty(), serviceDoctor.valueProperty(), service.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return servicePatientID.getText().isEmpty() || serviceFacility.getValue() == null || serviceDoctor.getValue() == null || service.getValue() == null;
            }
        });
        pendingServicesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        pendingEmergencyOrdersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        billPrint.disableProperty().bind(billsTable.getSelectionModel().selectedItemProperty().isNull());

    }


    private PatientInfo packPatientInfo() {
        PatientInfo patientInfo = new PatientInfo();
        if (!patientID.getText().isEmpty()) {
            patientInfo.setId(Integer.parseInt(patientID.getText()));
        }
        patientInfo.setAllergies(patientAllergies.getText());
        patientInfo.setBloodType(patientBloodType.getText());
        patientInfo.setName(patientName.getText());
        patientInfo.setNationalID(patientNationalID.getText());
        patientInfo.setGender(patientGender.getValue());
        patientInfo.setDateOfBirth(patientDateOfBirth.getValue());
        patientInfo.setAddress(patientAddress.getText());
        patientInfo.setEmail(patientEmail.getText());
        patientInfo.setPhone(patientPhone.getText());
        patientInfo.setNationality(patientNationality.getText());
        patientInfo.setMarital(patientMarital.getText());
        patientInfo.setBirthPlace(patientBirthPlace.getText());
        return patientInfo;
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
    private void addPatient() {
        int id = user.addPatient(packPatientInfo());
        if (id == -1) {
            showErrorAlert("خطأ في اضافة مريض جديد", "لا يمكن اضافة المريض!\nالرجاء التأكد من صحة البيانات.");
        } else {
            showConfirmationAlert("تم اضافة المريض بنجاح", "رقم المريض: " + id);
            patientID.setText(Integer.toString(id));
        }
    }

    @FXML
    private void updatePatient() {
        if (user.updatePatient(packPatientInfo())) {
            showConfirmationAlert("تم تحديث بيانات المريض بنجاح", "تم تحديث بيانات المريض بنجاح");
            return;
        }
        showErrorAlert("خطأ في تحديث بيانات المريض", "لا يمكن تحديث بيانات المريض!\nالرجاء التأكد من صحة " +
                "البيانات.");
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
                searchAuto = TextFields.bindAutoCompletion(patientSearch, request -> user.getPatientNamesWith(request.getUserText()));
                searchAuto.setMaxWidth(patientSearch.getWidth());
                break;
        }
        patientSearch.setText("");
    }

    @FXML
    private void searchPatient() {
        PatientInfo info;
        switch (searchMethod.getValue()) {
            case "بالرقم":
                info = user.getPatientByID(Integer.parseInt(patientSearch.getText()));
                break;
            case "برقم الهوية":
                info = user.getPatientByNationalID(patientSearch.getText());
                break;
            case "بالاسم":
                info = user.getPatientByName(patientSearch.getText());
                break;
            default:
                info = null;
        }
        if (info == null) {
            showErrorAlert("خطأ في البحث عن مريض", "لا يوجد مريض مطابق لهذه البيانات");
            return;
        }
        showPatientInfo(info);
    }

    private void showPatientInfo(PatientInfo info) {
        patientID.setText(Integer.toString(info.getId()));
        patientName.setText(info.getName());
        patientNationalID.setText(info.getNationalID());
        patientDateOfBirth.setValue(info.getDateOfBirth());
        patientDateOfBirth.getEditor().setText(info.getDateOfBirth().toString());
        patientGender.setValue(info.getGender());
        patientPhone.setText(info.getPhone());
        patientNationality.setText(info.getNationality());
        patientMarital.setText(info.getMarital());
        patientAddress.setText(info.getAddress());
        patientEmail.setText(info.getEmail());
        patientAllergies.setText(info.getAllergies());
        patientBloodType.setText(info.getBloodType());
        patientBirthPlace.setText(info.getBirthPlace());
    }

    @FXML
    private void clearPatient() {
        patientID.setText("");
        patientName.setText("");
        patientNationalID.setText("");
        patientDateOfBirth.getEditor().setText("");
        patientGender.setValue(null);
        patientPhone.setText("");
        patientNationality.setText("");
        patientMarital.setText("");
        patientAddress.setText("");
        patientEmail.setText("");
        patientAllergies.setText("");
        patientBloodType.setText("");
        patientBirthPlace.setText("");
    }

    public void registerService() {
        String id = user.registerService(Integer.parseInt(servicePatientID.getText()), serviceDoctor.getValue().getId(), service.getValue().getId(), serviceFacility.getValue().getId());
        if (id == null) {
            showErrorAlert("خطأ في تسجيل الخدمة!", "خطأ في تسجيل الخدمة.\nالرجاء التأكد من صحة البيانات.");
        } else {
            showConfirmationAlert("تم تسجيل الخدمة بنجاح!", "رقم الخدمة: " + id + "\nبرجاء دفع تكلفة الخدمة بالاستقبال");
        }
    }

    public void refreshFacilities() {
        serviceFacility.setItems(FXCollections.observableList(user.getAllServiceFacilities()));
    }

    public void refreshServices() {
        if (serviceFacility.getValue() != null) {
            serviceDoctor.setItems(FXCollections.observableList(user.getFacilityEmployees(serviceFacility.getValue().getId())));
            service.setItems(FXCollections.observableList(user.getServices(serviceFacility.getValue().getId())));
        }
    }

    public void clearBills() {
        billPatientID.setEditable(true);
        billsTable.getItems().clear();
        pendingServicesTable.getItems().clear();
        pendingEmergencyOrdersTable.getItems().clear();
        billPatientID.setText("");
        patientDebit.setText("");
    }

    public void clearServices() {
        servicePatientID.setText("");
        serviceFacility.setValue(null);
        service.setValue(null);
        serviceDoctor.setValue(null);
    }


    public void showPatientPaySet() {
        List<Bill> bills = user.getPatientBills(Integer.parseInt(billPatientID.getText()));
        List<ClinicDoctorController.ProvidedService> pendingServices = user.getPendingProvidedServices(Integer.parseInt(billPatientID.getText()));
        List<EmergencyDoctorController.EmergencyOrder> pendingEmergencyOrders = user.getPendingEmergencyOrders(Integer.parseInt(billPatientID.getText()));
        if (bills == null) {
            showErrorAlert("خطأ في عرض بيانات الدفع!", "خطأ في عرض بيانات الدفع.\nالرجاء التأكد من صحة البيانات.");
        } else {
            billPatientID.setEditable(false);
            billsTable.setItems(FXCollections.observableList(bills));
            pendingServicesTable.setItems(FXCollections.observableList(pendingServices));
            pendingEmergencyOrdersTable.setItems(FXCollections.observableArrayList(pendingEmergencyOrders));
            patientDebit.setText(user.calculateDebit(bills));
        }
    }

    @FXML
    private void issueBill() {
        int billID = user.issueBill(Integer.parseInt(billPatientID.getText()), pendingServicesTable.getSelectionModel().getSelectedItems(), pendingEmergencyOrdersTable.getSelectionModel().getSelectedItems());
        if (billID == -1) {
            showErrorAlert("خطأ في اصدار الفاتورة", "لم يتم اصدار فاتورة برجاء التأكد من صحة البيانات");
        } else {
            showConfirmationAlert("تم اصدار الفاتورة بنجاح", "رقم الفاتورة هو " + billID);
            showPatientPaySet();

        }
    }

    public void payBill() {
        Bill selected = billsTable.getSelectionModel().getSelectedItem();
        if (selected.getRemAmount() < Integer.parseInt(payAmount.getText())) {
            showErrorAlert("خطأ في دفع المبلغ", "برجاء ادخال مبلغ اقل من أو يساوي المبلغ المتبقي");
        } else if (user.updateAmount(selected.getId(), Integer.parseInt(payAmount.getText()))) {
            showConfirmationAlert("تم الدفع بنجاح", "تمت عملية الدفع بنجاح.");
            showPatientPaySet();
        } else {
            showErrorAlert("خطأ في دفع المبلغ", "لا يمكن اتمام عملية الدفع!\nالرجاء التأكد من صحة البيانات.");

        }
    }


    public void printBill() {
        JasperPrint bill = user.printBill(billsTable.getSelectionModel().getSelectedItem().getId());
        if (bill == null) {
            showErrorAlert("خطأ في الطباعة", "لا يمكن طباعة الفاتورة برجاء التأكد من توصيل الطابعة بشكل صحيح");
        } else {
            Thread thread = new Thread(() -> {
                boolean success;
                try {
                    success = JasperPrintManager.printReport(bill, true);
                } catch (JRException e) {
                    success = false;
                }
                if (success) {
                    Platform.runLater(() -> showConfirmationAlert("تمت الطباعة بنجاح", "تم ارسال الفاتورة الي الطابعة بنجاح")
                    );
                } else {
                    Platform.runLater(() -> showErrorAlert("خطأ في الطباعة", "لا يمكن طباعة الفاتورة برجاء التأكد من توصيل الطابعة بشكل صحيح"));
                }
            });
            thread.start();
        }
    }

    public static class Bill {

        private final SimpleIntegerProperty id;
        private final SimpleIntegerProperty amount;
        private final SimpleIntegerProperty paidAmount;
        private final SimpleIntegerProperty remAmount;

        public Bill(int id, int amount,
                    int paidAmount, int remAmount) {
            this.id = new SimpleIntegerProperty(id);
            this.amount = new SimpleIntegerProperty(amount);
            this.paidAmount = new SimpleIntegerProperty(paidAmount);
            this.remAmount = new SimpleIntegerProperty(remAmount);
        }

        public int getId() {
            return id.get();
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }

        public int getAmount() {
            return amount.get();
        }

        public SimpleIntegerProperty amountProperty() {
            return amount;
        }

        public int getPaidAmount() {
            return paidAmount.get();
        }

        public SimpleIntegerProperty paidAmountProperty() {
            return paidAmount;
        }

        public int getRemAmount() {
            return remAmount.get();
        }

        public SimpleIntegerProperty remAmountProperty() {
            return remAmount;
        }
    }

}
