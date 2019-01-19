package makeup.project.software;

import makeup.project.software.Dao.HBookDao;
import makeup.project.software.HiredBook;

import javax.swing.*;
import java.sql.*;

public class HBookDaoImpl implements HBookDao {
    @Override
    public ResultSet getLast5DayBooksRS(Connection connection) {
        String que = "select b_name as 'Book Name', \n" +
                "            author as 'Author', \n" +
                "            b_year as 'Published Year',\n" +
                "            isbn as 'ISBN',\n" +
                "            h_date as 'Hired Date',\n" +
                "            h_return as 'Return Date',\n" +
                "            time_left as 'Day Left',\n" +
                "            case when action_confirmed <> 'N'" +
                "               THEN 'Manager Confirmed' else 'Not Confirmed' end as 'Confirm Situation'\n" +
                "       from hire_book hb, book b\n" +
                "      where h_return - h_date <= 5 and hb.b_id = b.book_id";

        ResultSet rSet = null;
        try {
            Statement stat = connection.createStatement();
            rSet = stat.executeQuery(que);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error." +
                            "\nCan't get last books set.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }

        return rSet;
    }

    @Override
    public void checkBook(HiredBook hBook, Connection connection) {
        String que = "insert hire_book (u_id, b_id, h_date, h_return, time_left, penalty, action_confirmed) " +
                "values(?, ?, ?, ?, ?, ?, ?) ";
        try {
            PreparedStatement stat = connection.prepareStatement(que);
            stat.setInt(1, hBook.getU_id());
            stat.setInt(2, hBook.getB_id());
            stat.setDate(3, hBook.getH_date());
            stat.setDate(4, hBook.getH_return());
            stat.setInt(5, hBook.getTimeLeft());
            stat.setString(6, hBook.getPenalty());
            stat.setString(7, hBook.getAction_confirm());
            stat.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't check book. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void remCheck(int user_id, int book_id, Connection connection) {
        String que = "delete from hire_book where u_id = ? and b_id = ?";
        try {
            PreparedStatement stat = connection.prepareStatement(que);
            stat.setInt(1, user_id);
            stat.setInt(2, book_id);
            stat.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't remove check-book.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void confirmBook(HiredBook hiredBook, Connection connection) {
        String que = "update hire_book set action_confirmed = ? where u_id = ? and b_id = ?";
        try {
            PreparedStatement stat = connection.prepareStatement(que);
            stat.setString(1, hiredBook.getAction_confirm());
            stat.setInt(2, hiredBook.getU_id());
            stat.setInt(3, hiredBook.getB_id());
            stat.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't confirm book.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public HiredBook getCheckinDetail(int user_id, int book_id, Connection connection) {
        String que = "select * from hire_book where u_id = " + user_id + " and b_id = " + book_id;
        HiredBook h = new HiredBook();
        try {
            Statement stat = connection.createStatement();
            ResultSet rSet = stat.executeQuery(que);

            while (rSet.next()) {
                h = getHBookValFromSet(rSet);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't get check-in detail.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return h;
    }

    @Override
    public HiredBook getCheckedBookUser(int book_id, Connection connection) {
        HiredBook hb = new HiredBook();

        String que = "select * from hire_book where b_id = " + book_id;

        try {
            Statement stat = connection.createStatement();
            ResultSet rSet = stat.executeQuery(que);

            while (rSet.next()) {
                hb = getHBookValFromSet(rSet);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't get books user.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return hb;
    }


    @Override
    public ResultSet getUserCheckRS(int user_id, Connection connection) {
        String que = "select b_name as 'Book Name', \n" +
                "            author as 'Author', \n" +
                "            b_year as 'Published Year',\n" +
                "            isbn as 'ISBN',\n" +
                "            date_format(h_date,'%d/%m/%Y') as 'Hired Date',\n" +
                "            date_format(h_return,'%d/%m/%Y') as 'Return Date',\n" +
                "            penalty as 'Payment',\n" +
                "            time_left as 'Day Left',\n" +
                "            case when action_confirmed <> 'N' " +
                "                   THEN 'Manager Confirmed' else 'Not Confirmed' end as 'Confirm Situation' " +
                "     from   hire_book hb, book b\n" +
                "     where  hb.b_id = b.book_id and u_id = " + user_id + "";

        ResultSet rSet = null;
        try {
            Statement stat = connection.createStatement();
            rSet = stat.executeQuery(que);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't get checked books.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return rSet;
    }

    @Override
    public ResultSet getPenaltyRS(Connection connection) {
        String que = "select first_name as 'Name',\n" +
                "            surname as 'Surname',\n" +
                "            email,\n" +
                "            b_name as 'Book Name',\n" +
                "            b_year as 'Published Year',\n" +
                "            isbn as 'ISBN',\n" +
                "            h_return as 'Return Date',\n" +
                "            time_left as 'Day Left',\n" +
                "            penalty as 'Payment',\n" +
                "            case when action_confirmed <> 'N' THEN 'Manager Confirmed' else 'Not Confirmed' end as 'Confirm Situation'\n" +
                "       from hire_book hb, book b, library_user l\n" +
                "       where hb.b_id = b.book_id and l.user_id = hb.u_id and penalty <> 0;";

        ResultSet rSet = null;
        try {
            Statement stat = connection.createStatement();
            rSet = stat.executeQuery(que);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error." +
                            "\nCan't get penalty set.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return rSet;
    }

    @Override
    public ResultSet getUnConfirmBooksRS(Connection connection) {
        String que = "SELECT \n" +
                "    first_name AS 'Name',\n" +
                "    surname AS 'Surname',\n" +
                "    email,\n" +
                "    b_name AS 'Book Name',\n" +
                "    b_year AS 'Published Year',\n" +
                "    isbn AS 'ISBN',\n" +
                "    h_date AS 'Hired Date',\n" +
                "    time_left AS 'Day Left',\n" +
                "    penalty AS 'Payment',\n" +
                "    CASE\n" +
                "        WHEN action_confirmed <> 'N' THEN 'Manager Confirmed'\n" +
                "        ELSE 'Not Confirmed'\n" +
                "    END AS 'Confirm Situation'\n" +
                "FROM\n" +
                "    hire_book hb,\n" +
                "    book b,\n" +
                "    library_user l\n" +
                "WHERE\n" +
                "    hb.b_id = b.book_id\n" +
                "        AND l.user_id = hb.u_id\n" +
                "        AND action_confirmed = 'N'";

        ResultSet rSet = null;

        try {
            Statement s = connection.createStatement();
            rSet = s.executeQuery(que);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error." +
                            "\nCan't get unconfirmed book-set.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return rSet;
    }

    private HiredBook getHBookValFromSet(ResultSet rSet) {
        HiredBook hb = new HiredBook();

        try {
            int hb_id = rSet.getInt("hb_id");
            int u_id = rSet.getInt("u_id");
            int b_id = rSet.getInt("b_id");
            Date h_date = rSet.getDate("h_date");
            Date h_return = rSet.getDate("h_return");
            int time_left = rSet.getInt("time_left");
            String penalty = rSet.getString("penalty");
            String action_confirmed = rSet.getString("action_confirmed");

            hb = new HiredBook();
            hb.setHb_id(hb_id);
            hb.setU_id(u_id);
            hb.setB_id(b_id);
            hb.setH_date(h_date);
            hb.setH_return(h_return);
            hb.setTimeLeft(time_left);
            hb.setPenalty(penalty);
            hb.setAction_confirm(action_confirmed);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't get hire book object.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return hb;
    }
}
