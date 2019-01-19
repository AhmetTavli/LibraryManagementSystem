package makeup.project.software.Dao;

import makeup.project.software.User;

import java.sql.Connection;

public interface UserDao {
    User getUserById(int u_id, Connection connection);

    User getSingleUserByEmail(String email, Connection connection);

    User getSingleUserByEmail(String email, String password, Connection connection);

    void addUser(User user, Connection connection);

    void updateUserPwd(User user, Connection connection);
}
