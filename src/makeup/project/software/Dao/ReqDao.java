package makeup.project.software.Dao;

import makeup.project.software.Request;

import java.sql.Connection;
import java.sql.ResultSet;

public interface ReqDao {
    void sendReq(Request req, Connection con);

    ResultSet displayReq(Connection con);
}
