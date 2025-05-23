package Staff;

public class Staff {
    
    private String staffID;
    private String name;
    private String role;
    private String password;
    
    public Staff(String staffID, String name, String role, String password) {
        this.setStaffID(staffID);
        this.setName(name);
        this.setRole(role);
        this.setPassword(password);
    }

    public String getStaffID() {
        return staffID;
    }

    public String getName() {
        return name;
    }
    
    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }
    
    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isSuperAdmin() {
        return this.role.equals("SuperAdmin");
    }
    
    public String toDataString() {
        return String.join(",",staffID,name,role,password);
    }
}
