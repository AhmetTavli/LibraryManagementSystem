package makeup.project.software;

import makeup.project.design.Home;
import makeup.project.design.ManagerForm;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        Database mySql = Database.getInstance();

        mySql.checkDriverAvailability();

        Connection connection = mySql.connect();

        Home home = new Home(connection);
        home.setVisible(true);
    }
}
