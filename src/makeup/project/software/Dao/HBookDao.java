package makeup.project.software.Dao;

import makeup.project.software.HiredBook;

import java.sql.Connection;
import java.sql.ResultSet;

public interface HBookDao {
    ResultSet getLast5DayBooksRS(Connection connection);

    ResultSet getUserCheckRS(int user_id, Connection connection);

    ResultSet getPenaltyRS(Connection connection);

    ResultSet getUnConfirmBooksRS(Connection connection);

    void checkBook(HiredBook hBook, Connection connection);

    void remCheck(int user_id, int book_id, Connection connection);

    void confirmBook(HiredBook hiredBook, Connection connection);

    HiredBook getCheckinDetail(int user_id, int book_id, Connection connection);

    HiredBook getCheckedBookUser(int book_id, Connection connection);
}
