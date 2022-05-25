package HMS.Model;

import HMS.Control.*;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ClinicDoctor extends User {


    public ClinicDoctor(int employeeID, IDNamePair facilityID) {
        super(employeeID, facilityID);
    }


    private void cleanRecipe(int presID) {
        if (presID == -1) {
            return;
        }
        Connection c = null;
        Statement statement = null;
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.createStatement();
            statement.executeUpdate("delete from recipes where id = '" + presID + "';");
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, null);
    }

    public List<IDNamePair> getMedicines() {
        Connection c = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<IDNamePair> medicines = new ArrayList<>();
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.createStatement();
            resultSet = statement.executeQuery("select id,name from medicines;");
            while (resultSet.next()) {
                medicines.add(new IDNamePair(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return medicines;
    }

    public boolean insertRecipeDetails(int recipeID, List<ClinicDoctorController.Medicine> details) {
        Connection c = null;
        PreparedStatement statement = null;
        boolean success = true;
        String query = "insert into recipedetails (medicineID, dose, recipeID) values (?,?,?);";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            for (ClinicDoctorController.Medicine m : details) {
                statement.setInt(1, m.getId());
                statement.setString(2, m.getDose());
                statement.setInt(3, recipeID);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            success = false;
        }
        DBOperator.closeResources(c, statement, null);
        return success;
    }

    public int insertRecipe(int patientID, int visitID) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int presID = -1;
        String query = "insert into recipes (patientID,doctorID, facilityID, visitID, date) values(?,?,?,?,?);";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, patientID);
            statement.setInt(2, getEmployeeID());
            statement.setInt(3, getFacilityID());
            statement.setInt(4, visitID);
            statement.setDate(5, Date.valueOf(java.time.LocalDate.now()));
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                presID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return presID;
    }

    public int registerRecipe(int patientID, int visitID, List<ClinicDoctorController.Medicine> details) {
        int recipeID = insertRecipe(patientID, visitID);
        if (recipeID != -1) {
            if (!insertRecipeDetails(recipeID, details)) {
                cleanRecipe(recipeID);
                recipeID = -1;
            }
        }
        return recipeID;
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

    public Collection<String> getPatientNamesWith(String partOfName) {
        Collection<String> result = new ArrayList<>();
        Connection c = null;
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.createStatement();
            resultSet =
                    statement.executeQuery("select name from  patients where name like'" + partOfName + "%'");
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

    public List<ClinicDoctorController.Service> getPaidProvidedServices(int patientID, int facilityID) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<ClinicDoctorController.Service> paidServices = new ArrayList<>();
        String query = "select providedServices.id,services.name from providedServices,services where providedServices.patientID = ? and providedServices.facilityID = ? and providedServices.billID is not null and providedServices.doneDate is null and providedServices.serviceID=services.id;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, patientID);
            statement.setInt(2, facilityID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                paidServices.add(new ClinicDoctorController.Service(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return paidServices;
    }

    public Map<String, String> getPresDetails(int id) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Map<String, String> details = new HashMap<>();
        String query = "select recipes.patientID,recipes.visitID,recipes.doctorID,recipes.date,employees.name,patients.name,facilities.name from recipes,employees,patients,facilities WHERE recipes.id = ? and employees.id = recipes.doctorID and patients.id =recipes.patientID and facilities.id=recipes.facilityID;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                details.put("presID", Integer.toString(id));
                details.put("patientID", resultSet.getString(1));
                details.put("visitID", resultSet.getString(2));
                details.put("doctorID", resultSet.getString(3));
                details.put("date", resultSet.getString(4));
                details.put("doctorName", resultSet.getString(5));
                details.put("patientName", resultSet.getString(6));
                details.put("facilityName", resultSet.getString(7));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return details;
    }

    public List<ClinicDoctorController.PrescribedMedicine> getPresMedicines(int recipeID) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<ClinicDoctorController.PrescribedMedicine> medicines = new ArrayList<>();
        String query = "select medicines.name, dose from recipeDetails,medicines WHERE recipeDetails.recipeID = ? and recipeDetails.medicineID = medicines.id;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, recipeID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                medicines.add(new ClinicDoctorController.PrescribedMedicine(resultSet.getString(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return medicines;
    }

    public JasperPrint printPrescription(int recipeID) {
        JasperPrint prescription = null;
        Map<String, String> presDetails = getPresDetails(recipeID);
        if (presDetails.isEmpty()) {
            return null;
        }
        List<ClinicDoctorController.PrescribedMedicine> items = getPresMedicines(recipeID);
        if (items.isEmpty()) {
            return null;
        }
        Map<String, Object> paras = new HashMap<>(presDetails);
        paras.put("tableItems", new JRBeanCollectionDataSource(items));
        try {
            prescription = JasperFillManager.fillReport(getClass().getResourceAsStream("medicalPres.jasper"), paras, new JREmptyDataSource());
        } catch (JRException e) {
        }
        return prescription;
    }

    public boolean updateNotes(int providedServiceID, String notes) {
        boolean success = true;
        Connection c = null;
        PreparedStatement statement = null;
        String query = "update providedServices set notes = ? where id = ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setString(1, notes);
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

    public int registerEmergencyOrder(int patientID, int visitID, int medicineID, String dose) {
        int providedServiceID = -1;
        Connection c = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        String query = "insert into emergencyOrders (patientID, doctorID, facilityID, visitID, date, medicineID, dose) VALUES (?,?,?,?,?,?,?);";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, patientID);
            statement.setInt(2, getEmployeeID());
            statement.setInt(3, getFacilityID());
            statement.setInt(4, visitID);
            statement.setDate(5, Date.valueOf(java.time.LocalDate.now()));
            statement.setInt(6, medicineID);
            statement.setString(7, dose);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                providedServiceID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return providedServiceID;
    }


    public boolean saveResultToPath(int providedServiceID, File savePlace) {
       //Edit
        return false;
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

    public byte[] getResultAsBytes(int providedServiceID) {
        //Edit
        return null;
    }

    public List<LabController.LabService> getPaidLabServices(int patientID) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<LabController.LabService> paidServices = new ArrayList<>();
        final int labFacilityID = 6;
        String query = "select providedServices.id,services.name,providedServices.registerDate,providedServices.doneDate from providedServices,services where providedServices.patientID = ? and providedServices.facilityID = ? and providedServices.billID is not null and providedServices.serviceID=services.id;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, patientID);
            statement.setInt(2, labFacilityID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                paidServices.add(new LabController.LabService(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return paidServices;
    }



    public List<ClinicDoctorController.HistoryService> getPaidServices(int patientID) {
        Connection c = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select providedServices.id,services.name,facilities.name,employees.name,providedServices.registerDate,providedServices.notes from providedServices,services,facilities,employees where providedServices.patientID = ? and billID is not null and providedServices.serviceID = services.id and providedServices.facilityID = facilities.id and providedServices.doctorID = employees.id;";
        List<ClinicDoctorController.HistoryService> result = new ArrayList<>();
        try {
            c = DBOperator.getOperator().getConnection();
            preparedStatement = c.prepareStatement(query);
            preparedStatement.setInt(1, patientID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new ClinicDoctorController.HistoryService(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getDate(5).toString(), resultSet.getString(6)));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, preparedStatement, resultSet);
        return result;
    }

    public List<ClinicDoctorController.HistoryEmergency> getPaidEmergencyOrders(int patientID) {
        Connection c = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select emergencyOrders.id,emergencyOrders.visitID,facilities.name,employees.name,emergencyOrders.date,medicines.name,emergencyOrders.dose from emergencyOrders,facilities,employees,medicines where emergencyOrders.patientID = ? and emergencyOrders.billID is not null and emergencyOrders.facilityID = facilities.id and emergencyOrders.doctorID = employees.id and emergencyOrders.medicineID = medicines.id;";
        List<ClinicDoctorController.HistoryEmergency> result = new ArrayList<>();
        try {
            c = DBOperator.getOperator().getConnection();
            preparedStatement = c.prepareStatement(query);
            preparedStatement.setInt(1, patientID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new ClinicDoctorController.HistoryEmergency(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getDate(5).toString(), resultSet.getString(6), resultSet.getString(7)));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, preparedStatement, resultSet);
        return result;
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

    public List<ClinicDoctorController.HistoryRecipe> getRecipes(int patientID) {
        Connection c = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select recipes.id,recipes.visitID,facilities.name,employees.name,recipes.date from recipes,facilities,employees where recipes.patientID = ? and recipes.facilityID = facilities.id and recipes.doctorID = employees.id;";
        List<ClinicDoctorController.HistoryRecipe> result = new ArrayList<>();
        try {
            c = DBOperator.getOperator().getConnection();
            preparedStatement = c.prepareStatement(query);
            preparedStatement.setInt(1, patientID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new ClinicDoctorController.HistoryRecipe(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getDate(5).toString(), getRecipeDetails(resultSet.getInt(1))));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, preparedStatement, resultSet);
        return result;
    }


}
