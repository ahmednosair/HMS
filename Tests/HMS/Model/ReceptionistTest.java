package HMS.Model;

import HMS.Control.ClinicDoctorController;
import HMS.Control.ReceptionistController;
import javafx.util.converter.LocalDateStringConverter;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReceptionistTest {
    private Receptionist recep;
    ReceptionistTest(){
        recep = (Receptionist) UserFactory.getUser("recep","recep");
    }
    @Test
    void addPatient() {
        ReceptionistController.PatientInfo patientInfo = new ReceptionistController.PatientInfo();
        patientInfo.setAllergies("حساسية");
        patientInfo.setBloodType("A");
        patientInfo.setName("اسم");
        patientInfo.setNationalID("0000");
        patientInfo.setGender("انثي");
        patientInfo.setDateOfBirth(LocalDate.parse("2020-10-10"));
        patientInfo.setAddress("تجريبي");
        patientInfo.setEmail("تجريبي");
        patientInfo.setPhone("11111");
        patientInfo.setNationality("تجريبي");
        patientInfo.setMarital("عزباء");
        patientInfo.setBirthPlace("تجريبي");

        int id = recep.addPatient(patientInfo);
        Map<String, String> patient = new HashMap<>();
        Connection c = null;
        ResultSet resultSet = null;
        ResultSetMetaData metaData;
        Statement statement = null;
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.createStatement();
            resultSet = statement.executeQuery("select * from patients where id ="+id+";");
            metaData = resultSet.getMetaData();
            int n = metaData.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= n; i++) {
                    patient.put(metaData.getColumnName(i), resultSet.getString(i));
                }
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        assertEquals(Integer.toString(id),patient.get("id"));
        assertEquals("اسم",patient.get("name"));
        assertEquals("0000",patient.get("nationalID"));
        assertEquals("انثي",patient.get("gender"));
        assertEquals("تجريبي",patient.get("nationality"));
        assertEquals("عزباء",patient.get("marital"));
        assertEquals("تجريبي",patient.get("address"));
        assertEquals("تجريبي",patient.get("email"));
        assertEquals("حساسية",patient.get("allergies"));
        assertEquals("A",patient.get("bloodType"));
        assertEquals("تجريبي",patient.get("birthPlace"));
    }

    @Test
    void registerService() {
        String id = recep.registerService(1,1,1,6);
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<ClinicDoctorController.ProvidedService> result = new ArrayList<>();
        String query = "select providedServices.id,services.name,facilities.name,services.cost from providedServices,services,facilities where providedServices.id = ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(id));
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(new ClinicDoctorController.ProvidedService(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4)));
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        assertNotEquals(0,result.size());
    }


}