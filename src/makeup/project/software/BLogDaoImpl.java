package makeup.project.software;

import makeup.project.software.BookLog;
import makeup.project.software.Dao.BLogDao;
import makeup.project.software.User;

import javax.swing.*;
import java.sql.*;

public class BLogDaoImpl implements BLogDao {
    @Override
    public void addLog(int book_id, Date b_added, Connection connection) {
        String que = "insert into book_log (book_id, date_added) values(?, ?) ";
        try {
            PreparedStatement stat = connection.prepareStatement(que);
            stat.setInt(1, book_id);
            stat.setDate(2, b_added);
            stat.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't add book-log. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void subUser2Upd(int userId, Connection connection) {
        Date curDate = getDate();

        String que = "update book_log set u_id = ?, isSubscribed = true where date_added >= ?";
        try {
            PreparedStatement stat = connection.prepareStatement(que);
            stat.setInt(1, userId);
            stat.setDate(2, curDate);
            stat.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't subscribe user. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void unsub2Upd(int userId, Connection connection) {
        String que = "update book_log set isSubscribed = false where u_id = ?";
        try {
            PreparedStatement stat = connection.prepareStatement(que);
            stat.setInt(1, userId);
            stat.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't unsubscribe user. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public BookLog getLog(User user, Connection connection) {
        BookLog bookLog = new BookLog();
        String que = "select * from book_log where u_id = '" + user.getUserId() + "'";

        try {
            Statement stat = connection.createStatement();
            ResultSet rSet = stat.executeQuery(que);

            while (rSet.next()) {
                bookLog = getBookLogValFromSet(rSet);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't get book-log.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return bookLog;
    }

    @Override
    public ResultSet getLogRS(int userId, Connection connection) {
        String que = "select b_name as 'Book Name', author as 'Authors', b_year as 'Published Year', " +
                "day(date_added) as 'Day', month(date_added) as 'Month', year(date_added) as 'Year' " +
                "from book b, book_log bl where b.book_id = bl.book_id and u_id = '" + String.valueOf(userId) + "'";

        ResultSet rSet = null;

        try {
            Statement stat = connection.createStatement();
            rSet = stat.executeQuery(que);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't get Log Result-set. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return rSet;
    }

    private BookLog getBookLogByResultSet(String que, Connection con) {
        BookLog bl = new BookLog();

        try {
            Statement stat = con.createStatement();
            ResultSet rSet = stat.executeQuery(que);

            while (rSet.next()) {
                bl.setBl_id(rSet.getInt("bl_id"));
                bl.setU_id(rSet.getInt("u_id"));
                bl.setBook_id(rSet.getInt("book_id"));
                bl.setDate_added(rSet.getDate("date_added"));
                bl.setSubscribed(rSet.getBoolean("isSubscribed"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error." +
                            "\nCan't get book-log by result set. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return bl;
    }

    private Date getDate() {
        java.util.Date dObject = new java.util.Date();
        return new Date(dObject.getTime());
    }

    private BookLog getBookLogValFromSet(ResultSet set) {
        BookLog bLog = new BookLog();

        try {
            int bl_id = set.getInt("bl_id");
            int u_id = set.getInt("u_id");
            int book_id = set.getInt("book_id");
            Date date_added = set.getDate("date_added");
            boolean isSubscribed = set.getBoolean("isSubscribed");

            bLog.setBl_id(bl_id);
            bLog.setU_id(u_id);
            bLog.setBook_id(book_id);
            bLog.setDate_added(date_added);
            bLog.setSubscribed(isSubscribed);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't get book object.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return bLog;
    }
}
