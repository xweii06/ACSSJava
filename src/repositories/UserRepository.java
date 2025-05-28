package repositories;

import Main.User;
import java.util.List;
import java.io.IOException;

public interface UserRepository<T extends User> {
    List<T> getAll() throws IOException;
    T getByID(String id) throws IOException;
    void add(T user) throws IOException;
    void update(T user) throws IOException;
    void delete(String id) throws IOException;
}