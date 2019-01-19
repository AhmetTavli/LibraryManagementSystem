package makeup.project.software;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class Communication {
    private static Connection connection;

    static void checkDriverClass() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException exception) {
            JOptionPane.showMessageDialog(new JFrame(), "MySQL connector not found!", "Dialog",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }
    }

    static Connection databaseConnection(Database database) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "ahmettavli",
                    "@hMeT!89");
            connection.setCatalog("library");
        }catch (SQLException sql) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Database Connection Problem!" +
                            "\n\nOne possible solution:" +
                            "\nOpen Terminal and type:" +
                            "\n\nbrew services start mysql" +
                            "\n\nThen, " +
                            "\n\nmysql -u <username> -p" +
                            "\n\nThe program is quitting now",
                    "Dialog",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }

        return connection;
    }
}
