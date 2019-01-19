package makeup.project.software;

import makeup.project.software.Dao.UMailDao;
import makeup.project.software.UserMail;

import javax.swing.*;
import java.sql.*;

public class UMailDaoImpl implements UMailDao {
    @Override
    public void sendEmail(UserMail u_mail, Connection con) {
        String que = "insert into user_mail (mail_from, body, u_id) values(?, ?, ?)";
        try {
            PreparedStatement stat = con.prepareStatement(que);
            stat.setString(1, u_mail.getMail_from());
            stat.setString(2, u_mail.getBody());
            stat.setInt(3, u_mail.getUser_id());
            stat.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't send email to user. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void delEmail(UserMail u_mail, Connection con) {
        String que = "delete from user_mail where u_id = ?";
        try {
            PreparedStatement s = con.prepareStatement(que);
            s.setInt(1, u_mail.getUser_id());
            s.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't remove book. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public ResultSet getEmail(int userId, Connection con) {
        String q = "select mail_from as 'From', body as 'Message' from user_mail where u_id = " + userId;

        ResultSet rSet = null;

        try {
            Statement stat = con.createStatement();
            rSet = stat.executeQuery(q);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rSet;
    }


}
