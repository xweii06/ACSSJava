package Salesman;

import Main.User;

public class Salesman extends User {
    
    private String phone;
    private String email;
    
    public Salesman(String id, String name, String phone, String email, String password) {
        super(id, name, password);
        this.setPhone(phone);
        this.setEmail(email);
    }
    
    public String getPhone() {return phone;}
    public String getEmail() {return email;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setEmail(String email) {this.email = email;}
    
    @Override
    public String toDataString() {
        return id + "," + name + "," + phone + "," + email + "," + password;
    }
}
