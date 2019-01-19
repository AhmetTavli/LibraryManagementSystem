package makeup.project.software.Dao;

import makeup.project.software.BookLog;
import makeup.project.software.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;

public interface BLogDao {
    void addLog(int book_id, Date b_added, Connection connection);

    void subUser2Upd(int userId, Connection connection);

    void unsub2Upd(int userId, Connection connection);

    BookLog getLog(User user, Connection connection);

    ResultSet getLogRS(int userId, Connection connection);
}
