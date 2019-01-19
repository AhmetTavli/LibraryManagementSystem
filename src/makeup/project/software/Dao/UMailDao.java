package makeup.project.software.Dao;

import makeup.project.software.UserMail;

import java.sql.Connection;
import java.sql.ResultSet;

public interface UMailDao {
    void sendEmail(UserMail u_mail, Connection con);

    void delEmail(UserMail u_mail, Connection con);

    ResultSet getEmail(int userId, Connection con);
}
