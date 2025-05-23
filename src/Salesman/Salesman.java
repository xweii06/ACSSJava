package Salesman;

import utils.DataIO;

public class Salesman {
    
    private static final String SALESMAN_FILE = "salesman.txt";
    private String id;
    private String name;
    private String phone;
    private String email;
    private String password;
    
    public Salesman(String id, String name, String phone, String email, String password) {
        this.setSalesmanID(id);
        this.setName(name);
        this.setPhone(phone);
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getsalesmanID() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
    
    public void setSalesmanID(String id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String toDataString() {
        return id + "," + name + "," + phone + "," + email + "," + password;
    }
}
