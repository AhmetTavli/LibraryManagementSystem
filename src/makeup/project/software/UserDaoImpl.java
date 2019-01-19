package makeup.project.software;

import makeup.project.software.Dao.UserDao;

import javax.swing.*;
import java.sql.*;

public class UserDaoImpl implements UserDao {
    @Override
    public User getUserById(int u_id, Connection connection) {
        String que = "select * from library_user where user_id = '" + u_id + "'";

        return getSingleUserByResultSet(que, connection);
    }

    public User getSingleUserByEmail(String email, Connection connection) {
        String query = "select * from library_user where email = '" + email + "'";

        return getSingleUserByResultSet(query, connection);
    }

    @Override
    public User getSingleUserByEmail(String email, String pwd, Connection connection) {
        String query = "select * from library_user where email = '" + email + "' and user_password = '" + pwd + "' ";

        return getSingleUserByResultSet(query, connection);
    }

    public void addUser(User user, Connection connection) {
        String query = "insert into " +
                "library_user (first_name, surname, email, user_password, gender, birthday, user_role) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUserPassword());
            statement.setString(5, user.getGender());
            statement.setString(6, user.getBirthday());
            statement.setString(7, user.getRole());

            statement.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't add user. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateUserPwd(User user, Connection connection) {
        String que = "update library_user set user_password = ? where email = ?";
        try {
            PreparedStatement stat = connection.prepareStatement(que);
            stat.setString(1, user.getUserPassword().toString());
            stat.setString(2, user.getEmail());
            stat.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error." +
                            "\nCan't update user password. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    private User getSingleUserByResultSet(String que, Connection con) {
        User u = new User();

        try {
            Statement stat = con.createStatement();
            ResultSet rSet = stat.executeQuery(que);

            while (rSet.next()) {
                u.setUserId(rSet.getInt("user_id"));
                u.setFirstName(rSet.getString("first_name"));
                u.setSurname(rSet.getString("surname"));
                u.setEmail(rSet.getString("email"));
                u.setUserPassword(rSet.getString("user_password"));
                u.setGender(rSet.getString("gender"));
                u.setBirthday(rSet.getString("birthday"));
                u.setRole(rSet.getString("user_role"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error." +
                            "\nCan't get user by result set. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return u;
    }
}
