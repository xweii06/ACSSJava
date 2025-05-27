package Customer;

import Main.User;
import java.io.Serializable;

public class Customer extends User implements Serializable {
    private String phone;
    private String email;
    private String paymentMethod;
    
    public Customer(String id, String name, String phone, String email, String password) {
        super(id, name, password);
        this.phone = phone;
        this.email = email;
        this.paymentMethod = "";
    }

    public Customer(String id, String name, String phone, String email, String password, String paymentMethod) {
        this(id, name, phone, email, password);
        this.paymentMethod = paymentMethod;
    }

    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getPaymentMethod() { return paymentMethod; }
    
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    @Override
    public String toDataString() {
        return id + "," + name + "," + phone + "," + email + "," + password + "," + paymentMethod;
    }
}