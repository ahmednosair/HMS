package HMS.Model;

import HMS.Control.LabController;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class LabDoctor extends User {
    public LabDoctor(int employeeID, IDNamePair facility) {
        super(employeeID, facility);
    }

    public List<IDNamePair> getFacilityEmployees(int facilityID) {
        Connection c = null;
        ResultSet resultSet = null;
        Statement statement = null;
        List<IDNamePair> names = new ArrayList<>();
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.createStatement();
            resultSet = statement.executeQuery("select id,name from employees where facilityID = '" + facilityID + "'");
            while (resultSet.next()) {
                names.add(new IDNamePair(resultSet.getInt(1), resultSet.getString(2)));
            }

        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return names;
    }

    public List<IDNamePair> getServices(int facilityID) {
        String query = "Select id,name from services where facilityID='" + facilityID + "';";
        Connection c = null;
        ResultSet resultSet = null;
        Statement statement = null;
        List<IDNamePair> names = new ArrayList<>();
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                names.add(new IDNamePair(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return names;
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

    public String registerService(int patientID, int doctorID, int serviceID, int facilityID) {
        String providedServiceID = null;
        Connection c = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        String query = "insert into providedServices (serviceID,patientID,employeeID, doctorID,facilityID,registerDate) VALUES (?,?,?,?,?,?);";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, serviceID);
            statement.setInt(2, patientID);
            statement.setInt(3, employeeID);
            statement.setInt(4, doctorID);
            statement.setInt(5, facilityID);
            statement.setDate(6, Date.valueOf(java.time.LocalDate.now()));
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                providedServiceID = resultSet.getString(1);
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return providedServiceID;
    }

    public List<LabController.LabService> getPaidLabServices(int patientID, int facilityID) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<LabController.LabService> paidServices = new ArrayList<>();
        String query = "select providedServices.id,services.name,providedServices.registerDate,providedServices.doneDate from providedServices,services where providedServices.patientID = ? and providedServices.facilityID = ? and providedServices.billID is not null and providedServices.serviceID=services.id;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, patientID);
            statement.setInt(2, facilityID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                paidServices.add(new LabController.LabService(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return paidServices;
    }

    public boolean registerServiceResult(int providedServiceID, byte[] resultAsBytes) {
        //Edit
        return false;
    }

    public boolean endService(int providedServiceID) {
        boolean success = true;
        Connection c = null;
        PreparedStatement statement = null;
        String query = "update providedServices set doneDate = ? where id = ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setDate(1, Date.valueOf(java.time.LocalDate.now()));
            statement.setInt(2, providedServiceID);
            if (statement.executeUpdate() == 0) {
                success = false;
            }
        } catch (SQLException e) {
            success = false;
        }
        DBOperator.closeResources(c, statement, null);
        return success;
    }

    public boolean saveResultToPath(int providedServiceID, File savePlace) {
       //Edit
        return false;
    }

    public byte[] getResultAsBytes(int providedServiceID) {
        //Edit
        return null;
    }

    public File saveTmpResult(int providedServiceID) {
        File file = null;
        try {
            file = File.createTempFile("service", ".pdf");
            file.deleteOnExit();
            if (!saveResultToPath(providedServiceID, file)) {
                file = null;
            }
        } catch (IOException e) {
        }
        return file;
    }

}
