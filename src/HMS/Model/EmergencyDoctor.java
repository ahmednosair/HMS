package HMS.Model;

import HMS.Control.EmergencyDoctorController;

import java.sql.*;
import java.util.*;

public class EmergencyDoctor extends User {
    public EmergencyDoctor(int employeeID, IDNamePair facility) {
        super(employeeID, facility);
    }


    public List<EmergencyDoctorController.PendingOrder> getPaidPendingOrdersOf(int patientID) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EmergencyDoctorController.PendingOrder> paidPendingOrders = new ArrayList<>();
        String query = "select emergencyOrders.id,medicines.name,emergencyOrders.dose,facilities.name,employees.name  from emergencyOrders,medicines,facilities,employees where emergencyOrders.patientID = ? and emergencyOrders.billID is not null and emergencyOrders.isDone = FALSE and emergencyOrders.medicineID =medicines.id and emergencyOrders.facilityID = facilities.id and emergencyOrders.doctorID = employees.id;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, patientID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                paidPendingOrders.add(new EmergencyDoctorController.PendingOrder(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
            }
        } catch (SQLException e) {
            paidPendingOrders = null;
        }
        DBOperator.closeResources(c, statement, resultSet);
        return paidPendingOrders;
    }

    public List<EmergencyDoctorController.Order> getPaidOrdersOf(int patientID) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EmergencyDoctorController.Order> orders = new ArrayList<>();
        String query = "select emergencyOrders.id,medicines.name,emergencyOrders.dose,facilities.name,employees.name  from emergencyOrders,medicines,facilities,employees where emergencyOrders.patientID = ? and emergencyOrders.billID is not null and emergencyOrders.isDone = TRUE and emergencyOrders.medicineID =medicines.id and emergencyOrders.facilityID = facilities.id and emergencyOrders.doctorID = employees.id;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, patientID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(new EmergencyDoctorController.Order(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
            }
        } catch (SQLException e) {
            orders = null;
        }
        DBOperator.closeResources(c, statement, resultSet);
        return orders;
    }



    public boolean endEmergencyOrder(int orderID) {
        boolean success = true;
        Connection c = null;
        PreparedStatement statement = null;
        String query = "update emergencyOrders set isDone = TRUE where id = ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, orderID);
            if (statement.executeUpdate() == 0) {
                success = false;
            }
        } catch (SQLException e) {
            success = false;
        }
        DBOperator.closeResources(c, statement, null);
        return success;
    }


    public Collection<String> getPatientNamesWith(String partOfName) {
        Collection<String> result = new ArrayList<>();
        Connection c = null;
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.createStatement();
            resultSet =
                    statement.executeQuery("select name from  patients where name like'%" + partOfName + "%'");
            while (resultSet.next()) {
                result.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return result;
    }

    public Map<String, String> getPatientBy(String col, String val) {
        Map<String, String> patient = new HashMap<>();
        Connection c = null;
        ResultSet resultSet = null;
        ResultSetMetaData metaData;
        Statement statement = null;
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.createStatement();
            resultSet = statement.executeQuery("select * from patients where " + col + " ='" + val + "';");
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
        return patient;
    }

}
