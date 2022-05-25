package HMS.Model;

import HMS.Control.*;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Receptionist extends User {

    static class BillDetail {
        private final String id;
        private final String name;
        private final String facility;
        private final String cost;

        public BillDetail(String id, String name, String facility, String cost) {
            this.id = id;
            this.name = name;
            this.facility = facility;
            this.cost = cost;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getFacility() {
            return facility;
        }

        public String getCost() {
            return cost;
        }
    }


    public Receptionist(int employeeID, IDNamePair facility) {
        super(employeeID, facility);
    }

    public int addPatient(ReceptionistController.PatientInfo info) {
        int patientID = -1;
        Connection c = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        String query = "insert into patients (name, nationalID, gender, dateOfBirth, address, email, phone, nationality, marital, allergies, bloodType, birthPlace) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, info.getName());
            statement.setString(2, info.getNationalID());
            statement.setString(3, info.getGender());
            statement.setDate(4, Date.valueOf(info.getDateOfBirth()));
            statement.setString(5, info.getAddress());
            statement.setString(6, info.getEmail());
            statement.setString(7, info.getPhone());
            statement.setString(8, info.getNationality());
            statement.setString(9, info.getMarital());
            statement.setString(10, info.getAllergies());
            statement.setString(11, info.getBloodType());
            statement.setString(12, info.getBirthPlace());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                patientID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOperator.closeResources(c, statement, resultSet);
        return patientID;
    }

    public boolean updatePatient(ReceptionistController.PatientInfo info) {
        boolean success = true;
        String updatePatientQuery = "UPDATE patients SET name = ?, nationalID = ?, gender = ?, dateOfBirth = ?, address = ?, email = ?, phone = ?, nationality = ?, marital = ?, allergies = ?, bloodType = ?, birthPlace = ? where id = ?;";
        Connection c = null;
        PreparedStatement statement = null;
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(updatePatientQuery);
            statement.setString(1, info.getName());
            statement.setString(2, info.getNationalID());
            statement.setString(3, info.getGender());
            statement.setDate(4, Date.valueOf(info.getDateOfBirth()));
            statement.setString(5, info.getAddress());
            statement.setString(6, info.getEmail());
            statement.setString(7, info.getPhone());
            statement.setString(8, info.getNationality());
            statement.setString(9, info.getMarital());
            statement.setString(10, info.getAllergies());
            statement.setString(11, info.getBloodType());
            statement.setString(12, info.getBirthPlace());
            statement.setInt(13, info.getId());
            statement.executeUpdate();
        } catch (RuntimeException | SQLException e) {
            e.printStackTrace();
            success = false;
        }
        DBOperator.closeResources(c, statement, null);
        return success;
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
        String query = "select id, name, nationalID, gender, dateOfBirth, address, email, phone, nationality, marital, allergies, bloodType, birthPlace from patients where id = ?;";
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
                info.setAddress(resultSet.getString(6));
                info.setEmail(resultSet.getString(7));
                info.setPhone(resultSet.getString(8));
                info.setNationality(resultSet.getString(9));
                info.setMarital(resultSet.getString(10));
                info.setAllergies(resultSet.getString(11));
                info.setBloodType(resultSet.getString(12));
                info.setBirthPlace(resultSet.getString(13));
            }
        } catch (SQLException  e) {
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

    public List<ClinicDoctorController.ProvidedService> getPendingProvidedServices(int patientID) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<ClinicDoctorController.ProvidedService> result = new ArrayList<>();
        String query = "select providedServices.id,services.name,facilities.name,services.cost from providedServices,services,facilities where providedServices.patientID = ? and providedServices.billID is null and providedServices.serviceID=services.id and providedServices.facilityID=facilities.id;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, patientID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(new ClinicDoctorController.ProvidedService(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4)));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return result;
    }

    public List<EmergencyDoctorController.EmergencyOrder> getPendingEmergencyOrders(int patientID) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EmergencyDoctorController.EmergencyOrder> result = new ArrayList<>();
        String query = "select emergencyOrders.id,medicines.name,facilities.name,medicines.price from emergencyorders,medicines,facilities where emergencyOrders.patientID = ? and emergencyOrders.billID is null and emergencyOrders.medicineID =medicines.id and emergencyOrders.facilityID=facilities.id;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, patientID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(new EmergencyDoctorController.EmergencyOrder(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4)));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return result;
    }

    private int calculateBillAmount( List<ClinicDoctorController.ProvidedService> pendingServices, List<EmergencyDoctorController.EmergencyOrder> pendingEmergencyOrders ) {
        double amount = 0;

        for (ClinicDoctorController.ProvidedService service : pendingServices) {
                amount += service.getCost();
        }
        for (EmergencyDoctorController.EmergencyOrder order : pendingEmergencyOrders) {
                amount += order.getCost();
        }
        return (int) Math.ceil(amount);
    }

    private boolean attachServicesToBill(int billID, List<ClinicDoctorController.ProvidedService> services) {
        if (services == null || services.isEmpty()) {
            return true;
        }
        Connection c = null;
        PreparedStatement statement = null;
        boolean success = true;
        String query = "update providedServices set billID = ? where id = ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            for (ClinicDoctorController.ProvidedService service : services) {
                statement.setInt(1, billID);
                statement.setInt(2, service.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            success = false;
        }
        DBOperator.closeResources(c, statement, null);
        return success;
    }

    private boolean attachEmergencyOrdersToBill(int billID, List<EmergencyDoctorController.EmergencyOrder> pendingOrders) {
        if (pendingOrders == null || pendingOrders.isEmpty()) {
            return true;
        }
        Connection c = null;
        PreparedStatement statement = null;
        boolean success = true;
        String query = "update emergencyOrders set billID = ? where id = ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            for (EmergencyDoctorController.EmergencyOrder order : pendingOrders) {
                statement.setInt(1, billID);
                statement.setInt(2, order.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            success = false;
        }
        DBOperator.closeResources(c, statement, null);
        return success;
    }

    public int issueBill(int patientID, List<ClinicDoctorController.ProvidedService> pendingServices, List<EmergencyDoctorController.EmergencyOrder> pendingEmergencyOrders) {
        int billID = -1;
        Connection c = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        String query = "insert into bills (patientID,receptionistID,date,amount) values (?,?,?,?);";
        int amount = calculateBillAmount( pendingServices, pendingEmergencyOrders);
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, patientID);
            statement.setInt(2, employeeID);
            statement.setDate(3, Date.valueOf(java.time.LocalDate.now()));
            statement.setInt(4, amount);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                billID = resultSet.getInt(1);
            }

        } catch (SQLException e) {
        }
        if (billID != -1) {
            if (!attachServicesToBill(billID, pendingServices) || !attachEmergencyOrdersToBill(billID, pendingEmergencyOrders)) {
                billID = -1;
            }
        }
        DBOperator.closeResources(c, statement, resultSet);
        return billID;
    }

    public List<ReceptionistController.Bill> getPatientBills(int patientID) {
        List<ReceptionistController.Bill> bills = new ArrayList<>();
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select bills.id , bills.amount,bills.paid from bills where patientID = ?";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, patientID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bills.add(new ReceptionistController.Bill(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(2) - resultSet.getInt(3)));
            }
        } catch (SQLException e) {
        }
        DBOperator.closeResources(c, statement, resultSet);
        return bills;
    }

    public String calculateDebit(List<ReceptionistController.Bill> bills) {
        int debit = 0;
        for (ReceptionistController.Bill bill : bills) {
            debit += bill.getRemAmount();
        }
        return Integer.toString(debit);
    }

    public boolean updateAmount(int billID, int paid) {
        boolean result = true;
        Connection c = null;
        PreparedStatement statement = null;
        String query = "update bills set paid =  paid + ? where id = ?;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, paid);
            statement.setInt(2, billID);
            statement.executeUpdate();
        } catch (RuntimeException | SQLException e) {
            result = false;
        }
        DBOperator.closeResources(c, statement, null);
        return result;
    }

    private Map<String, String> getBillInfo(int id) {
        Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Map<String, String> details = new HashMap<>();
        String query = "select bills.patientID,patients.name, bills.date,contracts.name,bills.paid from bills,patients,contracts where bills.id = ? and patients.id = bills.patientID and contracts.id = bills.contractID;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                details.put("id", Integer.toString(id));
                details.put("patientID", resultSet.getString(1));
                details.put("patientName", resultSet.getString(2));
                details.put("date", resultSet.getString(3));
                details.put("contract", resultSet.getString(4));
                details.put("amount", resultSet.getString(5));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return details;
    }

    private List<BillDetail> getBillDetails(int id) {
       /* Connection c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Map<String, String> details = new HashMap<>();
        String query = "select bills.patientID,patients.name, bills.date,contracts.name,bills.paid from bills,patients,contracts where bills.id = ? and patients.id = bills.patientID and contracts.id = bills.contractID;";
        try {
            c = DBOperator.getOperator().getConnection();
            statement = c.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                details.put("id", Integer.toString(id));
                details.put("patientID", resultSet.getString(1));
                details.put("patientName", resultSet.getString(2));
                details.put("date", resultSet.getString(3));
                details.put("contract", resultSet.getString(4));
                details.put("amount", resultSet.getString(5));
            }
        } catch (SQLException e) {

        }
        DBOperator.closeResources(c, statement, resultSet);
        return details;*/
        return null;
    }

    public JasperPrint printBill(int id) {
        JasperPrint bill = null;
        Map<String, String> billInfo = getBillInfo(id);
        if (billInfo.isEmpty()) {
            return null;
        }
        List<BillDetail> details = getBillDetails(id);
        if (details.isEmpty()) {
            return null;
        }
        Map<String, Object> paras = new HashMap<>(billInfo);
        paras.put("tableItems", new JRBeanCollectionDataSource(details));
        try {
            bill = JasperFillManager.fillReport(getClass().getResourceAsStream("bill.jasper"), paras, new JREmptyDataSource());
        } catch (JRException e) {
        }
        return bill;
    }

}
