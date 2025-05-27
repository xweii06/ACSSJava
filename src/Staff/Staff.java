package Staff;

import Main.User;

public class Staff extends User {
    
    private String role;
    private String password;
    
    public Staff(String id, String name, String role, String password) {
        super(id,name,password);
        this.setRole(role);
    }
    
    public String getRole() {
        return role;
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
    
    @Override
    public String toDataString() {
        return String.join(",",id,name,role,password);
    }
}
