package makeup.project.software;

import java.sql.Connection;

class Database {
    private static Database database = new Database();

    private Database() {
    }

    static Database getInstance() {
        return database;
    }

    void checkDriverAvailability() {
        Communication.checkDriverClass();
    }

    Connection connect() {
        return Communication.databaseConnection(this);
    }
}
