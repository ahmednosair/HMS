package HMS.Model;

import HMS.Control.EmergencyDoctorController;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmergencyDoctorTest {

    private EmergencyDoctor doctor;

    EmergencyDoctorTest() {
        doctor = (EmergencyDoctor) UserFactory.getUser("emerg", "emerg");
    }

    @Test
    void endEmergencyOrder() {
        int id = -1;
        Connection c = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        String query = "insert into emergencyOrders (patientID, doctorID, facilityID, visitID, date, medicineID, dose) VALUES (?,?,?,?,?,?,?);";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, 1);
            statement.setInt(2, doctor.employeeID);
            statement.setInt(3, doctor.getFacilityID());
            statement.setInt(4, 1);
            statement.setDate(5, Date.valueOf(java.time.LocalDate.now()));
            statement.setInt(6, 1);
            statement.setString(7, "امبول");
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        doctor.endEmergencyOrder(id);
        c = null;
        statement = null;
        resultSet = null;
        query = "select isDone  from emergencyOrders where emergencyOrders.id = ?;";
        int isDone = 0;
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                isDone = resultSet.getInt(1);
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        assertEquals(1, isDone);
    }
}