import java.sql.*;

public class SQLConnection {
    public static Connection connection;
    static String connectionUrl =
            "jdbc:sqlserver://w66050.database.windows.net:1433;" +
            "database=ProjektPO;" +
            "user=w66050@w66050;" +
            "password=zaq1@WSX;encrypt=true;" +
            "trustServerCertificate=false;" +
            "hostNameInCertificate=*.database.windows.net;" +
            "loginTimeout=30;";
    static {
        try {
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DBConnection() throws SQLException {
    }
}
