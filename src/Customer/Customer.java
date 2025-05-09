package Customer;

import java.io.Serializable;

public class Customer implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String paymentMethod; // ✅ 新增字段

    // ✅ 构造函数（5个参数）
    public Customer(String id, String name, String phone, String email, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.paymentMethod = ""; // 默认空字符串
    }

    // ✅ 构造函数（6个参数，支持读取含付款方式）
    public Customer(String id, String name, String phone, String email, String password, String paymentMethod) {
        this(id, name, phone, email, password);
        this.paymentMethod = paymentMethod;
    }

    // ✅ Getter
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPaymentMethod() { return paymentMethod; }

    // ✅ Setter
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    // ✅ 写入文本用
    public String toDataString() {
        return id + "," + name + "," + phone + "," + email + "," + password + "," + paymentMethod;
    }
}
