package HMS.Model;

import HMS.Control.ClinicDoctorController;
import HMS.Control.ReceptionistController;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClinicDoctorTest {

    private ClinicDoctor doctor;
    ClinicDoctorTest(){
        doctor = (ClinicDoctor) UserFactory.getUser("doc","doc");
    }

    private List<ClinicDoctorController.HistoryRecipeDetails> getRecipeDetails(int recipeID) {
        Connection c = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select medicines.name,recipeDetails.dose from recipeDetails,medicines where recipeID = ? and recipeDetails.medicineID = medicines.id;";
        List<ClinicDoctorController.HistoryRecipeDetails> result = new ArrayList<>();
        try {
            c = DBOperator.getOperator().getConnection();
            preparedStatement = c.prepareStatement(query);
            preparedStatement.setInt(1, recipeID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new ClinicDoctorController.HistoryRecipeDetails(resultSet.getString(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, preparedStatement, resultSet);
        return result;
    }

    @Test
    void registerRecipe() {
        List<ClinicDoctorController.Medicine> meds = new ArrayList<>();
        meds.add(new ClinicDoctorController.Medicine(1,"بنادول","مرتين يوميا"));
        meds.add(new ClinicDoctorController.Medicine(2,"كتافلام","مرتين يوميا"));
        doctor.registerRecipe(1,1,meds);
        Connection c = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select recipes.id,recipes.visitID,facilities.name,employees.name,recipes.date from recipes,facilities,employees where recipes.patientID = ? and recipes.visitID = ? and recipes.doctorID = ?;";
        List<ClinicDoctorController.HistoryRecipe> result = new ArrayList<>();
        try {
            c = DBOperator.getOperator().getConnection();
            preparedStatement = c.prepareStatement(query);
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, 1);
            preparedStatement.setInt(3, 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new ClinicDoctorController.HistoryRecipe(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getDate(5).toString(), getRecipeDetails(resultSet.getInt(1))));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        DBOperator.closeResources(c, preparedStatement, resultSet);
        assertNotEquals(0,result.size());
        int j = result.size()-1;
        for(int i=0;i<meds.size();i++){
            assertEquals(meds.get(i).getName(),result.get(j).getDetails().get(i).getName());
            assertEquals(meds.get(i).getDose(),result.get(j).getDetails().get(i).getDose());
        }

    }

    @Test
    void getPatientBy() {
        Map<String,String> patient = doctor.getPatientBy("id","1");
        assertEquals("1",patient.get("id"));
        assertEquals("مريض تجريبي",patient.get("name"));
        assertEquals("21212",patient.get("nationalID"));
        assertEquals("ذكر",patient.get("gender"));
        assertEquals("مصري",patient.get("nationality"));
        assertEquals("متزوج",patient.get("marital"));
        assertEquals("تجريبي",patient.get("address"));
        assertEquals("تجريبي",patient.get("email"));
        assertEquals("بنسلين",patient.get("allergies"));
        assertEquals("B",patient.get("bloodType"));
        assertEquals("مصر",patient.get("birthPlace"));

    }

}