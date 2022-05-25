package HMS.Model;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserFactory {

    private static int authenticate(String userName, String userPassword) {
        String query = "select id,userPass from employees where userName = ?;";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int employeeID = -1;
        try {
            connection = DBOperator.getOperator().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, userName);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString(2).equals(userPassword)) {
                    employeeID = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(connection, statement, resultSet);
        return employeeID;
    }

    private static Map<String, Object> getUserDetails(int employeeID) {
        String query = "select employees.employeeTypeID,facilities.id,facilities.name from facilities,employees where employees.id = ? and facilities.id = employees.facilityID;";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Map<String, Object> details = new HashMap<>();
        try {
            connection = DBOperator.getOperator().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, employeeID);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                details.put("employeeTypeID", resultSet.getInt(1));
                details.put("facilityID", resultSet.getInt(2));
                details.put("facilityName", resultSet.getString(3));
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(connection, statement, resultSet);
        return details;
    }

    public static User getUser(String userName, String userPassword) {
        int employeeID = authenticate(userName, userPassword);
        if (employeeID == -1) {
            return null;
        }
        Map<String, Object> userDetails = getUserDetails(employeeID);
        if (userDetails.isEmpty()) {
            return null;
        }
        int employeeTypeID = (int) userDetails.get("employeeTypeID");
        IDNamePair facility = new IDNamePair((int) userDetails.get("facilityID"), (String) userDetails.get("facilityName"));
        switch (employeeTypeID) {
            case 1:
                return new ClinicDoctor(employeeID, facility);
            case 2:
                return new EmergencyDoctor(employeeID, facility);
            case 3:
                return new LabDoctor(employeeID, facility);
            case 4:
                return new Receptionist(employeeID, facility);
            case 5:
                return new Admin(employeeID, facility);
            default:
                return null;
        }

    }


}
