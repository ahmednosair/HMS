package HMS.Model;

import HMS.Control.ReceptionistController;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Admin extends User {
    public Admin(int employeeID, IDNamePair facility) {
        super(employeeID, facility);
    }

    public int addPatient(Map<String,String> info) {
        int patientID = -1;
        return patientID;
    }

    public boolean updatePatient(Map<String, String> info) {
        boolean success = true;

        return success;
    }

    public int deletePatient(String id) {
        int patientID = -1;
        return patientID;
    }

    public Collection<String> getPatientNamesWith(String partOfName) {
        Collection<String> result = new ArrayList<>();
        Connection c = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        String query = "select name from  patients where name like ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setString(1, partOfName + "%");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return result;
    }

    public ReceptionistController.PatientInfo getPatientByID(int id) {
        ReceptionistController.PatientInfo info = null;
        Connection c = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        String query = "select id, name, nationalID, gender, dateOfBirth, IDFileName,IDScan, address, email, phone, nationality, marital, allergies, bloodType, birthPlace from patients where id = ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                info = new ReceptionistController.PatientInfo();
                info.setId(resultSet.getInt(1));
                info.setName(resultSet.getString(2));
                info.setNationalID(resultSet.getString(3));
                info.setGender(resultSet.getString(4));
                info.setDateOfBirth(resultSet.getDate(5).toLocalDate());
                File tmpFile = File.createTempFile("tmp", "." + resultSet.getString(6));
                tmpFile.deleteOnExit();
                FileOutputStream out = new FileOutputStream(tmpFile);
                out.write(resultSet.getBlob(7).getBytes(1, (int) resultSet.getBlob(7).length()));
                out.close();
                info.setIDScan(tmpFile);
                info.setAddress(resultSet.getString(8));
                info.setEmail(resultSet.getString(9));
                info.setPhone(resultSet.getString(10));
                info.setNationality(resultSet.getString(11));
                info.setMarital(resultSet.getString(12));
                info.setAllergies(resultSet.getString(13));
                info.setBloodType(resultSet.getString(14));
                info.setBirthPlace(resultSet.getString(15));
            }
        } catch (SQLException | IOException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return info;
    }

    public ReceptionistController.PatientInfo getPatientByName(String name) {
        ReceptionistController.PatientInfo info = null;
        Connection c = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        String query = "select id, name, nationalID, gender, dateOfBirth, IDFileName,IDScan, address, email, phone, nationality, marital, allergies, bloodType, birthPlace from patients where name = ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                info = new ReceptionistController.PatientInfo();
                info.setId(resultSet.getInt(1));
                info.setName(resultSet.getString(2));
                info.setNationalID(resultSet.getString(3));
                info.setGender(resultSet.getString(4));
                info.setDateOfBirth(resultSet.getDate(5).toLocalDate());
                File tmpFile = File.createTempFile("tmp", "." + resultSet.getString(6));
                tmpFile.deleteOnExit();
                FileOutputStream out = new FileOutputStream(tmpFile);
                out.write(resultSet.getBlob(7).getBytes(1, (int) resultSet.getBlob(7).length()));
                out.close();
                info.setIDScan(tmpFile);
                info.setAddress(resultSet.getString(8));
                info.setEmail(resultSet.getString(9));
                info.setPhone(resultSet.getString(10));
                info.setNationality(resultSet.getString(11));
                info.setMarital(resultSet.getString(12));
                info.setAllergies(resultSet.getString(13));
                info.setBloodType(resultSet.getString(14));
                info.setBirthPlace(resultSet.getString(15));
            }
        } catch (SQLException | IOException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return info;
    }


    public ReceptionistController.PatientInfo getPatientByNationalID(String nationalID) {
        ReceptionistController.PatientInfo info = null;
        Connection c = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        String query = "select id, name, nationalID, gender, dateOfBirth, IDFileName,IDScan, address, email, phone, nationality, marital, allergies, bloodType, birthPlace from patients where nationalID = ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setString(1, nationalID);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                info = new ReceptionistController.PatientInfo();
                info.setId(resultSet.getInt(1));
                info.setName(resultSet.getString(2));
                info.setNationalID(resultSet.getString(3));
                info.setGender(resultSet.getString(4));
                info.setDateOfBirth(resultSet.getDate(5).toLocalDate());
                File tmpFile = File.createTempFile("tmp", "." + resultSet.getString(6));
                tmpFile.deleteOnExit();
                FileOutputStream out = new FileOutputStream(tmpFile);
                out.write(resultSet.getBlob(7).getBytes(1, (int) resultSet.getBlob(7).length()));
                out.close();
                info.setIDScan(tmpFile);
                info.setAddress(resultSet.getString(8));
                info.setEmail(resultSet.getString(9));
                info.setPhone(resultSet.getString(10));
                info.setNationality(resultSet.getString(11));
                info.setMarital(resultSet.getString(12));
                info.setAllergies(resultSet.getString(13));
                info.setBloodType(resultSet.getString(14));
                info.setBirthPlace(resultSet.getString(15));
            }
        } catch (SQLException | IOException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return info;
    }


    public int addEmployee(Map<String, String> info) {
        int patientID = -1;

        return patientID;
    }

    public int updateEmployee(Map<String, String> info) {
        int patientID = -1;

        return patientID;
    }

    public int deleteEmployee(String id) {
        int patientID = -1;

        return patientID;
    }

    public List<IDNamePair> getAllServiceFacilities() {
        Connection c = null;
        ResultSet resultSet = null;
        Statement statement = null;
        List<IDNamePair> names = new ArrayList<>();
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.createStatement();
            resultSet = statement.executeQuery("select id,name from facilities where isSP = TRUE;");
            while (resultSet.next()) {
                names.add(new IDNamePair(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return names;
    }

    public boolean addFacility(String name) {
        return false;
    }

    public boolean removeFacility(String id) {
        return false;
    }

    public boolean updateFacility(String id, Map<String, String> details) {
        return false;
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

    public boolean addNewService(int facilityID) {
        return false;
    }

    public boolean deleteService(int id) {
        return false;
    }

    public boolean deleteBill(int id) {
        return false;
    }

    public boolean updateService(int id, Map<String, String> details) {
        return false;
    }

    public boolean updateBill(int id, Map<String, String> details) {
        return false;
    }

    public List<ReceptionistController.Bill> getPatientBills(int patientID) {
        List<ReceptionistController.Bill> bills = new ArrayList<>();
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select bills.id , bills.amount,bills.paid from bills,contracts where patientID = ? and bills.contractID=contracts.id;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, patientID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bills.add(new ReceptionistController.Bill(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(3) - resultSet.getInt(4)));
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return bills;
    }

    public JasperPrint printPatientBills(int id) {
        return null;
    }

    public JasperPrint report(Map<String,String> criteria) {
        return null;
    }
}
