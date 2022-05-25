package HMS.Control;

import HMS.Model.ClinicDoctor;
import HMS.Model.IDNamePair;
import HMS.Model.User;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClinicDoctorController implements UserController {

    public TableView<Service> servicesTable;
    public TableColumn<Service, String> serviceNameCol;
    public TableColumn<Service, String> serviceIDCol;
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
    public TextArea notes;
    public Button registerPres;
    public Button printPres;
    public TextField presPatientID;
    public TextField presVisitID;
    public SearchableComboBox<IDNamePair> medicine;
    public Button addMedicine;
    public Button deleteMedicine;
    public TableView<Medicine> medicinesTable;
    public TableColumn<Medicine, String> medicineNameCol;
    public TableColumn<Medicine, String> doseCol;
    public TextField recipeID;
    public TextField dose;
    public TextField servicePatientID;
    public ComboBox<IDNamePair> serviceFacility;
    public ComboBox<IDNamePair> serviceDoctor;
    public SearchableComboBox<IDNamePair> service;
    public Button registerService;
    public TextField patientSearch;
    public ComboBox<String> searchMethod;
    public Button search;
    public Button updateNotes;
    public Button endService;
    public TextField emergentPatientID;
    public TextField emergentVisitID;
    public SearchableComboBox<IDNamePair> emergentMedicine;
    public TextField emergentDose;
    public Button registerEmergent;
    public TableView<LabController.LabService> labServicesTable;
    public TableColumn<LabController.LabService, String> labProvidedServiceIDCol;
    public TableColumn<LabController.LabService, String> labProvidedServiceNameCol;
    public TableColumn<LabController.LabService, String> labProvidedServiceDateCol;
    public TableColumn<LabController.LabService, String> labProvidedServiceResultDateCol;
    public Button showLabServices;
    public TextField labServicesPatientID;
    public Button saveLabResult;
    public Button printLabResult;
    public Button openLabResult;
    public ComboBox<String> historySearchMethod;
    public TextField historyPatientSearchKey;
    public Button historySearch;
    public TextField historyPatientID;
    public TextField historyPatientName;
    public TextField historyPatientNationalID;
    public TextField historyPatientDateOfBirth;
    public TextField historyPatientGender;
    public TextField historyPatientPhone;
    public TextField historyPatientNationality;
    public TextField historyPatientMarital;
    public TextField historyPatientAddress;
    public TextField historyPatientEmail;
    public TextField historyPatientAllergies;
    public TextField historyPatientBloodType;
    public TextField historyPatientBirthPlace;
    public TableView<HistoryService> historyServicesTable;
    public TableColumn<HistoryService, String> historyServiceIDCol;
    public TableColumn<HistoryService, String> historyServiceNameCol;
    public TableColumn<HistoryService, String> historyServiceFacility;
    public TableColumn<HistoryService, String> historyServiceDoctor;
    public TableColumn<HistoryService, String> historyServiceDate;
    public TableColumn<HistoryService, PopOverLabel> historyServiceNotes;
    public TableView<HistoryEmergency> historyEmergencyTable;
    public TableColumn<HistoryEmergency, Integer> historyEmergencyOrderID;
    public TableColumn<HistoryEmergency, Integer> historyEmergencyOrderVisitID;
    public TableColumn<HistoryEmergency, String> historyEmergencyOrderFacility;
    public TableColumn<HistoryEmergency, String> historyEmergencyOrderDoctor;
    public TableColumn<HistoryEmergency, String> historyEmergencyOrderDate;
    public TableColumn<HistoryEmergency, String> historyEmergencyOrderMedicine;
    public TableColumn<HistoryEmergency, String> historyEmergencyOrderDose;
    public TableView<HistoryRecipe> historyRecipesTable;
    public TableColumn<HistoryRecipe, Integer> historyRecipeID;
    public TableColumn<HistoryRecipe, Integer> historyRecipeVisitID;
    public TableColumn<HistoryRecipe, String> historyRecipeFacility;
    public TableColumn<HistoryRecipe, String> historyRecipeDoctor;
    public TableColumn<HistoryRecipe, String> historyRecipeDate;
    public TableView<HistoryRecipeDetails> historyRecipeDetailsTable;
    public TableColumn<HistoryRecipeDetails, String> historyRecipeMedicine;
    public TableColumn<HistoryRecipeDetails, String> historyRecipeDose;
    private ClinicDoctor user;
    private AutoCompletionBinding<String> searchAuto;
    private AutoCompletionBinding<String> historySearchAuto;

    public static class PopOverLabel extends Label {
        private PopOver popOver;

        private void initialize() {
            Label popOverContent = new Label();
            popOver = new PopOver(popOverContent);
            popOverContent.setMaxWidth(300);
            popOverContent.setMaxHeight(500);
            popOverContent.setWrapText(true);
            popOverContent.setPadding(new Insets(10));
            popOverContent.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            popOverContent.textProperty().bind(this.textProperty());
            this.setOnMouseEntered(event -> popOver.show(this));
            this.setOnMouseExited(event -> popOver.hide());
        }

        public PopOverLabel() {
            initialize();
        }

        public PopOverLabel(String text) {
            super(text);
            initialize();

        }

        public PopOverLabel(String text, Node graphic) {
            super(text, graphic);
            initialize();

        }
    }

    public static class HistoryRecipe {
        private final Integer id;
        private final Integer visitID;
        private final String facility;
        private final String doctor;
        private final String date;
        private final List<HistoryRecipeDetails> details;

        public HistoryRecipe(int id, Integer visitID, String facility, String doctor, String date, List<HistoryRecipeDetails> details) {
            this.id = id;
            this.visitID = visitID;
            this.facility = facility;
            this.doctor = doctor;
            this.date = date;
            this.details = details;
        }

        public Integer getId() {
            return id;
        }

        public Integer getVisitID() {
            return visitID;
        }

        public String getFacility() {
            return facility;
        }

        public String getDoctor() {
            return doctor;
        }

        public String getDate() {
            return date;
        }

        public List<HistoryRecipeDetails> getDetails() {
            return details;
        }
    }

    public static class HistoryRecipeDetails {
        private final String name;
        private final String dose;

        public HistoryRecipeDetails(String name, String dose) {
            this.name = name;
            this.dose = dose;
        }

        public String getName() {
            return name;
        }

        public String getDose() {
            return dose;
        }
    }

    public static class HistoryEmergency {
        private final Integer id;
        private final Integer visitID;
        private final String facility;
        private final String doctor;
        private final String date;
        private final String name;
        private final String dose;

        public HistoryEmergency(int id, Integer visitID, String facility, String doctor, String date, String name, String dose) {
            this.id = id;
            this.visitID = visitID;
            this.facility = facility;
            this.doctor = doctor;
            this.date = date;
            this.name = name;
            this.dose = dose;
        }

        public Integer getId() {
            return id;
        }

        public Integer getVisitID() {
            return visitID;
        }

        public String getFacility() {
            return facility;
        }

        public String getDoctor() {
            return doctor;
        }

        public String getDate() {
            return date;
        }

        public String getName() {
            return name;
        }

        public String getDose() {
            return dose;
        }
    }

    public static class HistoryService {
        private final int id;
        private final String name;
        private final String facility;
        private final String doctor;
        private final String date;
        private final PopOverLabel notes;

        public HistoryService(int id, String name, String facility, String doctor, String date, String notes) {
            this.id = id;
            this.name = name;
            this.facility = facility;
            this.doctor = doctor;
            this.date = date;
            this.notes = new PopOverLabel(notes);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getFacility() {
            return facility;
        }

        public String getDoctor() {
            return doctor;
        }

        public String getDate() {
            return date;
        }

        public PopOverLabel getNotes() {
            return notes;
        }
    }

    @Override
    public void initialize(User user) {
        this.user = (ClinicDoctor) user;
        medicine.setItems(FXCollections.observableList(this.user.getMedicines()));
        emergentMedicine.setItems(FXCollections.observableList(this.user.getMedicines()));
    }

    @FXML
    public void initialize() {

        serviceIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        serviceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        updateNotes.disableProperty().bind(Bindings.or(Bindings.isEmpty(servicesTable.getSelectionModel().getSelectedItems()), notes.textProperty().isEmpty()));
        endService.disableProperty().bind(Bindings.isEmpty(servicesTable.getSelectionModel().getSelectedItems()));
        presPatientID.editableProperty().bind(Bindings.isEmpty(medicinesTable.getItems()));
        presVisitID.editableProperty().bind(Bindings.isEmpty(medicinesTable.getItems()));
        printPres.disableProperty().bind(recipeID.textProperty().isEmpty());
        registerPres.disableProperty().bind(Bindings.or(recipeID.textProperty().isNotEmpty(), Bindings.isEmpty(medicinesTable.getItems())));
        addMedicine.disableProperty().bind(Bindings.or(medicine.valueProperty().isNull(), dose.textProperty().isEmpty()));
        deleteMedicine.disableProperty().bind(Bindings.isEmpty(medicinesTable.getSelectionModel().getSelectedItems()));
        medicineNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        doseCol.setCellValueFactory(new PropertyValueFactory<>("dose"));
        registerService.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(servicePatientID.textProperty(), serviceFacility.valueProperty(), serviceDoctor.valueProperty(), service.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return servicePatientID.getText().isEmpty() || serviceFacility.getValue() == null || serviceDoctor.getValue() == null || service.getValue() == null;
            }
        });
        search.disableProperty().bind(Bindings.or(searchMethod.valueProperty().isNull(), patientSearch.textProperty().isEmpty()));
        registerEmergent.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(emergentPatientID.textProperty(), emergentVisitID.textProperty(), emergentMedicine.valueProperty(), emergentDose.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return emergentPatientID.getText().isEmpty() || emergentVisitID.getText().isEmpty() || emergentMedicine.getValue() == null || emergentDose.getText().isEmpty();
            }
        });
        labProvidedServiceIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        labProvidedServiceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        labProvidedServiceDateCol.setCellValueFactory(new PropertyValueFactory<>("registerDate"));
        labProvidedServiceResultDateCol.setCellValueFactory(new PropertyValueFactory<>("resultDate"));
        saveLabResult.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(labServicesTable.getSelectionModel().selectedItemProperty());
            }

            @Override
            protected boolean computeValue() {
                return labServicesTable.getSelectionModel().getSelectedItem() == null || labServicesTable.getSelectionModel().getSelectedItem().getResultDate() == null;
            }
        });
        printLabResult.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(labServicesTable.getSelectionModel().selectedItemProperty());
            }

            @Override
            protected boolean computeValue() {
                return labServicesTable.getSelectionModel().getSelectedItem() == null || labServicesTable.getSelectionModel().getSelectedItem().getResultDate() == null;
            }
        });
        openLabResult.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(labServicesTable.getSelectionModel().selectedItemProperty());
            }

            @Override
            protected boolean computeValue() {
                return labServicesTable.getSelectionModel().getSelectedItem() == null || labServicesTable.getSelectionModel().getSelectedItem().getResultDate() == null;
            }
        });
        showLabServices.disableProperty().bind(labServicesPatientID.textProperty().isEmpty());
        historySearch.disableProperty().bind(Bindings.or(historyPatientSearchKey.textProperty().isEmpty(), historySearchMethod.valueProperty().isNull()));

        historyServiceIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        historyServiceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        historyServiceFacility.setCellValueFactory(new PropertyValueFactory<>("facility"));
        historyServiceDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        historyServiceDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        historyServiceNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));

        historyEmergencyOrderID.setCellValueFactory(new PropertyValueFactory<>("id"));
        historyEmergencyOrderVisitID.setCellValueFactory(new PropertyValueFactory<>("visitID"));
        historyEmergencyOrderFacility.setCellValueFactory(new PropertyValueFactory<>("facility"));
        historyEmergencyOrderDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        historyEmergencyOrderDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        historyEmergencyOrderMedicine.setCellValueFactory(new PropertyValueFactory<>("name"));
        historyEmergencyOrderDose.setCellValueFactory(new PropertyValueFactory<>("dose"));

        historyRecipeID.setCellValueFactory(new PropertyValueFactory<>("id"));
        historyRecipeVisitID.setCellValueFactory(new PropertyValueFactory<>("visitID"));
        historyRecipeFacility.setCellValueFactory(new PropertyValueFactory<>("facility"));
        historyRecipeDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        historyRecipeDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        historyRecipesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                historyRecipeDetailsTable.getItems().clear();
            } else {
                historyRecipeDetailsTable.setItems(FXCollections.observableList(newValue.getDetails()));
            }
        });
        historyRecipeMedicine.setCellValueFactory(new PropertyValueFactory<>("name"));
        historyRecipeDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
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


    private void clearVisitPatientInfo() {
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


    public void clearPres() {
        presVisitID.setText("");
        presPatientID.setText("");
        recipeID.setText("");
        medicine.setValue(null);
        medicinesTable.getItems().clear();
        dose.setText("");
    }

    public void addMedicineHandler() {
        medicinesTable.getItems().add(new Medicine(medicine.getValue().getId(), medicine.getValue().getName(), dose.getText()));
    }

    public void deleteMedicineHandler() {
        medicinesTable.getItems().remove(medicinesTable.getSelectionModel().getSelectedItem());
    }

    public void registerPres() {
        int prescriptionID = user.registerRecipe(Integer.parseInt(presPatientID.getText()), Integer.parseInt(presVisitID.getText()), medicinesTable.getItems());
        if (prescriptionID == -1) {
            showErrorAlert("لم يتم تسجيل الروشتة", "برجاء التأكد من صحة البيانات");
        } else {
            recipeID.setText(Integer.toString(prescriptionID));
            showConfirmationAlert("تم تسجيل الروشتة بنجاح", "رقم الروشتة " + prescriptionID + " يمكن طباعتها الان ");
        }

    }

    public void refreshServiceFacilities() {
        List<IDNamePair> facilities = new ArrayList<>();
        facilities.add(user.getFacility());
        facilities.add(new IDNamePair(6, "المختبر"));
        serviceFacility.setItems(FXCollections.observableList(facilities));
    }

    public void refreshServices() {
        if (serviceFacility.getValue() != null) {
            service.setItems(FXCollections.observableList(user.getServices(serviceFacility.getValue().getId())));
            serviceDoctor.setItems(FXCollections.observableList(user.getFacilityEmployees(serviceFacility.getValue().getId())));
        }
    }

    public void registerServiceHandler() {
        String id = user.registerService(Integer.parseInt(servicePatientID.getText()), serviceDoctor.getValue().getId(), service.getValue().getId(), serviceFacility.getValue().getId());
        if (id == null) {
            showErrorAlert("خطأ في تسجيل الخدمة!", "خطأ في تسجيل الخدمة.\nالرجاء التأكد من صحة البيانات.");
        } else {
            showConfirmationAlert("تم تسجيل الخدمة بنجاح!", "رقم الخدمة: " + id + "\nبرجاء دفع تكلفة الخدمة بالاستقبال");
        }
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
    private void searchPatientServices() {
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
            showErrorAlert("خطأ في البحث عن مريض", "لا يوجد مريض مطابق للبيانات!\nالرجاء التأكد من صحة ");
        } else {
            showPatientInfo(patient);
            servicesTable.setItems(FXCollections.observableList(user.getPaidProvidedServices(Integer.parseInt(patientID.getText()), user.getFacilityID())));
        }


    }

    public void clearServiceHandler() {
        serviceDoctor.setValue(null);
        servicePatientID.setText("");
        service.setValue(null);
        serviceFacility.setValue(null);
        notes.setText("");
    }

    public void clearPaidServices() {
        clearVisitPatientInfo();
        servicesTable.getItems().clear();
        notes.setText("");
    }

    public void printPresHandler() {
        JasperPrint prescription = user.printPrescription(Integer.parseInt(recipeID.getText()));
        if (prescription == null) {
            showErrorAlert("خطأ في الطباعة", "لا يمكن طباعة الوصفة الطبية برجاء التأكد من توصيل الطابعة بشكل صحيح");
        } else {
            Thread thread = new Thread(() -> {
                boolean success;
                try {
                    success = JasperPrintManager.printReport(prescription, true);
                } catch (JRException e) {
                    success = false;
                }
                if (success) {
                    Platform.runLater(() -> showConfirmationAlert("تمت الطباعة بنجاح", "تم ارسال الوصفة الطبية الي الطابعة بنجاح")
                    );
                } else {
                    Platform.runLater(() -> showErrorAlert("خطأ في الطباعة", "لا يمكن طباعة الوصفة الطبية برجاء التأكد من توصيل الطابعة بشكل صحيح"));
                }
            });
            thread.start();
        }


    }

    public void updateNotesHandler() {
        if (user.updateNotes(servicesTable.getSelectionModel().getSelectedItem().getId(), notes.getText())) {
            showConfirmationAlert("تم تحديث الملاحظات بنجاح", "تم تحديث ملاحظات الخدمة بنجاح");
        } else {
            showErrorAlert("خطأ في تحديث الملاحظات", "لم يتم تحديث ملاحظات الخدمة يرجي التأكد من صحة البيانات");
        }
    }

    public void endServiceHandler() {
        if (user.endService(servicesTable.getSelectionModel().getSelectedItem().getId())) {
            servicesTable.getItems().remove(servicesTable.getSelectionModel().getSelectedItem());
            notes.setText("");
        } else {
            showErrorAlert("خطأ في انهاء الخدمة", "لم يتم انهاء الخدمة يرجي التأكد من صحة البيانات");
        }
    }

    public void clearEmergentHandler() {
        emergentPatientID.setText("");
        emergentVisitID.setText("");
        emergentMedicine.setValue(null);
        emergentDose.setText("");
    }

    public void registerEmergentHandler() {
        if (user.registerEmergencyOrder(Integer.parseInt(emergentPatientID.getText()), Integer.parseInt(emergentVisitID.getText()), emergentMedicine.getValue().getId(), emergentDose.getText()) == -1) {
            showErrorAlert("خطأ في وصف دواء طارئ", "لم يتم وصف الدواء برجاء التأكد من صحة البيانات");
        } else {
            showConfirmationAlert("تم وصف الدواء بنجاح", "تم وصف الدواء بنجاح يرجي دفع التكلفة في الاستقبال");
        }
    }

    public void saveLabResultHandler() {
        boolean success;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documents", "*.pdf"));
        File saveFile = fileChooser.showSaveDialog(labServicesTable.getScene().getWindow());
        if (saveFile == null) {
            success = false;
        } else {
            success = user.saveResultToPath(labServicesTable.getSelectionModel().getSelectedItem().getId(), saveFile);
        }
        if (success) {
            showConfirmationAlert("تم حفظ نتيجة التحليل", "تم حفظ نتيجة التحليل علي الجهاز بنجاح!");
        } else {
            showErrorAlert("لا يمكن حفظ نتيجة التحليل", "برجاء التأكد من صحة البيانات!");
        }
    }

    public void printLabResultHandler() {
        byte[] result = user.getResultAsBytes(labServicesTable.getSelectionModel().getSelectedItem().getId());
        if (result == null) {
            showErrorAlert("خطأ في الطباعة", "لم يتم طباعة النتيجة برجاء التأكد من رفعها من الجهة المصدرة لها");
            return;
        }
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPageable(new PDFPageable(PDDocument.load(result)));
            if (printerJob.printDialog()) {
                printerJob.print();
            }
        } catch (IOException | PrinterException e) {
            showErrorAlert("خطأ في الطباعة", "برجاء التأكد من توصيل الطابعة بطريقة صحيحة");
        }

    }

    public void openLabResultHandler() {
        File tmpFile = user.saveTmpResult(labServicesTable.getSelectionModel().getSelectedItem().getId());
        if (tmpFile == null) {
            showErrorAlert("لا يمكن فتح النتيجة", "برجاء التأكد من رفعها من الجهة المصدرة لها!");
            return;
        }
        try {
            Desktop.getDesktop().open(tmpFile);
        } catch (IOException e) {
            showErrorAlert("لا يمكن فتح النتيجة", "برجاء التأكد من رفعها من الجهة المصدرة لها!");
        }
    }

    public void clearLabServicesTable() {
        labServicesTable.getItems().clear();
        labServicesPatientID.setText("");
    }

    public void showLabServicesHandle() {
        labServicesTable.setItems(FXCollections.observableList(user.getPaidLabServices(Integer.parseInt(labServicesPatientID.getText()))));
    }



    private void showHistoryPatientInfo(Map<String, String> patient) {
        historyPatientID.setText(patient.get("id"));
        historyPatientName.setText(patient.get("name"));
        historyPatientNationalID.setText(patient.get("nationalID"));
        historyPatientDateOfBirth.setText(patient.get("dateOfBirth"));
        historyPatientGender.setText(patient.get("gender"));
        historyPatientPhone.setText(patient.get("phone"));
        historyPatientNationality.setText(patient.get("nationality"));
        historyPatientMarital.setText(patient.get("marital"));
        historyPatientAddress.setText(patient.get("address"));
        historyPatientEmail.setText(patient.get("email"));
        historyPatientAllergies.setText(patient.get("allergies"));
        historyPatientBloodType.setText(patient.get("bloodType"));
        historyPatientBirthPlace.setText(patient.get("birthPlace"));
    }

    public void refreshHistorySearchField() {
        switch (historySearchMethod.getValue()) {
            case "بالرقم":
            case "برقم الهوية":
                if (historySearchAuto != null) {
                    historySearchAuto.dispose();
                    historySearchAuto = null;
                }
                break;
            case "بالاسم":
                historySearchAuto = TextFields.bindAutoCompletion(historyPatientSearchKey,
                        request -> user.getPatientNamesWith(request.getUserText()));
                historySearchAuto.setMaxWidth(historyPatientSearchKey.getWidth());
                break;
        }
        historyPatientSearchKey.setText("");
    }

    public void historySearchPatient() {
        String colName;
        switch (historySearchMethod.getValue()) {
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
        Map<String, String> patient = user.getPatientBy(colName, historyPatientSearchKey.getText());
        if (patient.isEmpty()) {
            showErrorAlert("خطأ في البحث عن مريض", "لا يوجد مريض مطابق للبيانات!\nالرجاء التأكد من صحة ");
            return;
        }
        int patientID = Integer.parseInt(patient.get("id"));
        showHistoryPatientInfo(patient);
        historyServicesTable.setItems(FXCollections.observableList(user.getPaidServices(patientID)));
        historyEmergencyTable.setItems(FXCollections.observableList(user.getPaidEmergencyOrders(patientID)));
        historyRecipesTable.setItems(FXCollections.observableList(user.getRecipes(patientID)));
    }

    public void clearHistory() {
        historyPatientSearchKey.setText("");
        historyPatientID.setText("");
        historyPatientName.setText("");
        historyPatientNationalID.setText("");
        historyPatientDateOfBirth.setText("");
        historyPatientGender.setText("");
        historyPatientPhone.setText("");
        historyPatientNationality.setText("");
        historyPatientMarital.setText("");
        historyPatientAddress.setText("");
        historyPatientEmail.setText("");
        historyPatientAllergies.setText("");
        historyPatientBloodType.setText("");
        historyPatientBirthPlace.setText("");
        historyServicesTable.getItems().clear();
        historyEmergencyTable.getItems().clear();
        historyRecipesTable.getItems().clear();
        historyRecipeDetailsTable.getItems().clear();


    }

    public static class Medicine {
        private final int id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty dose;

        public Medicine(int id, String name, String dose) {
            this.id = id;
            this.name = new SimpleStringProperty(name);
            this.dose = new SimpleStringProperty(dose);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public String getDose() {
            return dose.get();
        }

        public SimpleStringProperty doseProperty() {
            return dose;
        }

        public int getId() {
            return id;
        }
    }

    public static class ProvidedService {
        private final int id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty facilityName;
        private final int cost;

        public ProvidedService(int id, String name, String facilityName, int cost) {
            this.id = id;
            this.name = new SimpleStringProperty(name);
            this.facilityName = new SimpleStringProperty(facilityName);
            this.cost = cost;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
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

    public static class Service {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;

        public Service(int id, String name) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
        }

        public int getId() {
            return id.get();
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }
    }

    public static class PrescribedMedicine {
        String name;
        String dose;

        public PrescribedMedicine(String name, String dose) {
            this.name = name;
            this.dose = dose;
        }

        public String getName() {
            return name;
        }

        public String getDose() {
            return dose;
        }
    }
}
