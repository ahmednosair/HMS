package HMS.Model;

public abstract class User {
    final int employeeID;
    final IDNamePair facility;

    public User(int employeeID, IDNamePair facility) {
        this.employeeID = employeeID;
        this.facility = facility;
    }

    public IDNamePair getFacility() {
        return facility;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public int getFacilityID() {
        return facility.getId();
    }

    public String getFacilityName() {
        return facility.getName();
    }


}

