package makeup.project.software.Dao;

import makeup.project.software.Manager;

import java.sql.Connection;
import java.util.ArrayList;

public interface ManDao {
    ArrayList<Manager> getAllManagers(Connection connection);
    Manager getManagerByEmail(String email, Connection connection);
}
