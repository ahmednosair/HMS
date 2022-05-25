package HMS.Model;

import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class LabDoctorTest {
    private LabDoctor doctor;

    LabDoctorTest() {
        doctor = (LabDoctor) UserFactory.getUser("lab", "lab");
    }
    @Test
    void endService() {
        int id = -1;
        Connection c = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        String query = "insert into providedservices (serviceID, patientID, doctorID, facilityID, cost) values (3, 1, ?, ?, 100);";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,doctor.getEmployeeID());
            statement.setInt(2,doctor.getFacilityID());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        doctor.endService(id);
        c = null;
        statement = null;
        resultSet = null;
        query = "select doneDate from providedservices where providedservices.id = ?;";
        Date doneDate = null;
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                doneDate = resultSet.getDate(1);
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        assertNotNull(doneDate);
    }
}