package makeup.project.software;

import makeup.project.software.Dao.ReqDao;
import makeup.project.software.Request;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReqDaoImpl implements ReqDao {
    @Override
    public void sendReq(Request req, Connection con) {
        String que = "insert into request (u_id, i_name, author, i_type, u_comment) values(?, ?, ?, ?, ?)";
        try {
            PreparedStatement stat = con.prepareStatement(que);
            stat.setInt(1, req.getU_id());
            stat.setString(2, req.getI_name());
            stat.setString(3, req.getAuthor());
            stat.setString(4, req.getI_type());
            stat.setString(5, req.getU_comment());
            stat.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't send request.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ResultSet displayReq(Connection con) {
        String que = "select  l.first_name as 'Name',\n" +
                "\t\t   l.surname as 'Surname',\n" +
                "           l.email,\n" +
                "           r.i_type as 'Request Type',\n" +
                "           r.i_name as 'Request Name',\n" +
                "           r.author as 'Author'\n" +
                "from    request r, \n" +
                "           library_user l\n" +
                "where r.u_id = l.user_id";

        ResultSet rSet = null;
        try {
            Statement stat = con.createStatement();
            rSet = stat.executeQuery(que);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't display user request.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return rSet;
    }
}
