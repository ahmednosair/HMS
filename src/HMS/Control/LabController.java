package HMS.Control;

import HMS.Model.IDNamePair;
import HMS.Model.LabDoctor;
import HMS.Model.User;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LabController implements UserController {
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
    public ComboBox<String> searchMethod;
    public TextField patientSearch;
    public Button search;
    public TextField servicePatientID;
    public SearchableComboBox<IDNamePair> service;
    public Button registerService;
    public ComboBox<IDNamePair> serviceDoctor;
    public Button showServices;
    public TextField servicesPatientID;
    public TableView<LabService> servicesTable;
    public TableColumn<LabService, String> providedServiceIDCol;
    public TableColumn<LabService, String> providedServiceNameCol;
    public TableColumn<LabService, String> providedServiceDateCol;
    public TableColumn<LabService, String> providedServiceResultDateCol;
    public Button saveResult;
    public Button printResult;
    public Button chooseFile;
    public Button registerResult;
    public TextField resultPath;
    public Button openResult;
    public TextField patientBirthPlace;
    LabDoctor user;
    private AutoCompletionBinding<String> searchAuto;


    @Override
    public void initialize(User user) {
        this.user = (LabDoctor) user;
        refreshServices();
    }

    public void initialize() {
        search.disableProperty().bind(Bindings.or(searchMethod.valueProperty().isNull(),
                patientSearch.textProperty().isEmpty()));
        registerService.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(servicePatientID.textProperty(), serviceDoctor.valueProperty(), service.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return servicePatientID.getText().isEmpty() || serviceDoctor.getValue() == null || service.getValue() == null;
            }
        });
        servicesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        providedServiceIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        providedServiceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        providedServiceDateCol.setCellValueFactory(new PropertyValueFactory<>("registerDate"));
        providedServiceResultDateCol.setCellValueFactory(new PropertyValueFactory<>("resultDate"));
        chooseFile.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(servicesTable.getSelectionModel().selectedItemProperty());
            }

            @Override
            protected boolean computeValue() {
                return servicesTable.getSelectionModel().getSelectedItem() == null || servicesTable.getSelectionModel().getSelectedItem().getResultDate() != null;
            }
        });
        registerResult.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(servicesTable.getSelectionModel().selectedItemProperty(), resultPath.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return servicesTable.getSelectionModel().getSelectedItem() == null || servicesTable.getSelectionModel().getSelectedItem().getResultDate() != null || resultPath.getText().isEmpty();
            }
        });
        saveResult.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(servicesTable.getSelectionModel().selectedItemProperty());
            }

            @Override
            protected boolean computeValue() {
                return servicesTable.getSelectionModel().getSelectedItem() == null || servicesTable.getSelectionModel().getSelectedItem().getResultDate() == null;
            }
        });
        printResult.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(servicesTable.getSelectionModel().selectedItemProperty());
            }

            @Override
            protected boolean computeValue() {
                return servicesTable.getSelectionModel().getSelectedItem() == null || servicesTable.getSelectionModel().getSelectedItem().getResultDate() == null;
            }
        });
        openResult.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(servicesTable.getSelectionModel().selectedItemProperty());
            }

            @Override
            protected boolean computeValue() {
                return servicesTable.getSelectionModel().getSelectedItem() == null || servicesTable.getSelectionModel().getSelectedItem().getResultDate() == null;
            }
        });
        showServices.disableProperty().bind(servicesPatientID.textProperty().isEmpty());
    }

    public void refreshServices() {
        service.setItems(FXCollections.observableList(user.getServices(user.getFacilityID())));
        serviceDoctor.setItems(FXCollections.observableList(user.getFacilityEmployees(user.getFacilityID())));
    }


    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content, new ButtonType("??????????"));
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.setTitle("??????");
        alert.setHeaderText(header);
        alert.show();
    }

    private void showConfirmationAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, new ButtonType("??????????"));
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.setTitle("??????????");
        alert.setHeaderText(header);
        alert.show();
    }

    @FXML
    private void refreshSearchField() {
        switch (searchMethod.getValue()) {
            case "????????????":
            case "???????? ????????????":
                if (searchAuto != null) {
                    searchAuto.dispose();
                    searchAuto = null;
                }
                break;
            case "????????????":
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
            case "????????????":
                colName = "id";
                break;
            case "???????? ????????????":
                colName = "nationalID";
                break;
            case "????????????":
                colName = "name";
                break;
            default:
                colName = "";
        }
        Map<String, String> patient = user.getPatientBy(colName, patientSearch.getText());
        if (patient.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "???? ???????? ???????? ?????????? ????????????????!\n???????????? ???????????? ???? ?????? " +
                    "????????????????.", new ButtonType("??????????"));
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            alert.setTitle("??????");
            alert.setHeaderText("?????? ???? ?????????? ???? ????????");
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


    public void registerServiceHandle() {
        String id = user.registerService(Integer.parseInt(servicePatientID.getText()), serviceDoctor.getValue().getId(), service.getValue().getId(), user.getFacilityID());
        if (id == null) {
            showErrorAlert("?????? ???? ?????????? ????????????!", "?????? ???? ?????????? ????????????.\n???????????? ???????????? ???? ?????? ????????????????.");
        } else {
            showConfirmationAlert("???? ?????????? ???????????? ??????????!", "?????? ????????????: " + id + "\n?????????? ?????? ?????????? ???????????? ????????????????????");
        }
    }

    public void clearServiceInfo() {
        servicePatientID.setText("");
        service.setValue(null);
        serviceDoctor.setValue(null);
    }

    public void clearServicesTable() {
        servicesTable.getItems().clear();
        servicesPatientID.setText("");
        resultPath.setText("");
    }

    public void showServicesHandle() {
        servicesTable.setItems(FXCollections.observableList(user.getPaidLabServices(Integer.parseInt(servicesPatientID.getText()), user.getFacilityID())));
    }

    public void chooseFileHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documents", "*.pdf"));
        File file = fileChooser.showOpenDialog(resultPath.getScene().getWindow());
        if (file != null) {
            resultPath.setText(file.getAbsolutePath());
        }
    }

    public void registerResultHandler() {
        boolean success;
        try {
            success = user.registerServiceResult(servicesTable.getSelectionModel().getSelectedItem().getId(), FileUtils.readFileToByteArray(new File(resultPath.getText())));
        } catch (IOException e) {
            success = false;
        }
        success = success && user.endService(servicesTable.getSelectionModel().getSelectedItem().getId());

        if (success) {
            showConfirmationAlert("???? ?????????? ?????????? ??????????????", "???? ?????????? ?????????? ?????????????? ?????? ???????????? ??????????!");
            showServicesHandle();
        } else {
            showErrorAlert("???? ???????? ?????????? ?????????? ??????????????", "?????????? ???????????? ???? ???????? ?????? ?????????????? ???? ???????????? ????????????!");
        }
    }

    public void saveResultHandler() {
        boolean success;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documents", "*.pdf"));
        File saveFile = fileChooser.showSaveDialog(resultPath.getScene().getWindow());
        if (saveFile == null) {
            success = false;
        } else {
            success = user.saveResultToPath(servicesTable.getSelectionModel().getSelectedItem().getId(), saveFile);
        }
        if (success) {
            showConfirmationAlert("???? ?????? ?????????? ??????????????", "???? ?????? ?????????? ?????????????? ?????? ???????????? ??????????!");
        } else {
            showErrorAlert("???? ???????? ?????? ?????????? ??????????????", "?????????? ???????????? ???? ?????? ????????????????!");
        }
    }

    public void printResultHandler() {
        byte[] result = user.getResultAsBytes(servicesTable.getSelectionModel().getSelectedItem().getId());
        if (result == null) {
            showErrorAlert("?????? ???? ??????????????", "???? ?????? ?????????? ?????????????? ?????????? ???????????? ???? ??????????");
            return;
        }
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPageable(new PDFPageable(PDDocument.load(result)));
            if (printerJob.printDialog()) {
                printerJob.print();
            }
        } catch (IOException | PrinterException e) {
            showErrorAlert("?????? ???? ??????????????", "?????????? ???????????? ???? ?????????? ?????????????? ???????????? ??????????");
        }

    }

    public void openResultHandler() {
        boolean success = true;
        File tmpFile = user.saveTmpResult(servicesTable.getSelectionModel().getSelectedItem().getId());
        if (tmpFile == null) {
            success = false;
        } else {
            try {
                Desktop.getDesktop().open(tmpFile);
            } catch (IOException e) {
                success = false;
            }
        }
        if (!success) {
            showErrorAlert("???? ???????? ?????? ?????????? ??????????????", "?????????? ???????????? ???? ?????? ????????????????!");
        }
    }

    public static class LabService {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty registerDate;
        private final SimpleStringProperty resultDate;

        public LabService(int id, String name, String registerDate, String resultDate) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.registerDate = new SimpleStringProperty(registerDate);
            this.resultDate = new SimpleStringProperty(resultDate);
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

        public String getRegisterDate() {
            return registerDate.get();
        }

        public SimpleStringProperty registerDateProperty() {
            return registerDate;
        }

        public String getResultDate() {
            return resultDate.get();
        }

        public SimpleStringProperty resultDateProperty() {
            return resultDate;
        }
    }
}
