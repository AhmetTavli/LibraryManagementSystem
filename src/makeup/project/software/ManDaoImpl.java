package makeup.project.software;

import makeup.project.software.Dao.ManDao;
import makeup.project.software.Manager;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ManDaoImpl implements ManDao {
    public ArrayList<Manager> getAllManagers(Connection connection) {
        String query = "select * from manager_accept";

        return getMultipleManagersByResultSet(query, connection);
    }

    public Manager getManagerByEmail(String email, Connection connection) {
        String query = "select * from manager_accept m where m.manager_email = '" + email + "'";

        return getSingleManagerByEmail(query, connection);
    }

    private Manager getSingleManagerByEmail(String que, Connection con) {
        Manager man = new Manager();

        try {
            Statement stat = con.createStatement();
            ResultSet rSet = stat.executeQuery(que);

            while (rSet.next()) {
                int manId = rSet.getInt("manager_accept_id");
                String email = rSet.getString("manager_email");

                man.setId(manId);
                man.setEmail(email);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error." +
                            "\nCan't get manager by result set. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }

        return man;
    }

    private ArrayList<Manager> getMultipleManagersByResultSet(String query, Connection connection) {
        Manager manager;
        ArrayList<Manager> managerList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int managerId = resultSet.getInt("manager_accept_id");
                String email = resultSet.getString("manager_email");

                manager = new Manager();
                manager.setId(managerId);
                manager.setEmail(email);

                managerList.add(manager);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error." +
                            "\nCan't get managers by result set. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }

        return managerList;
    }
}
