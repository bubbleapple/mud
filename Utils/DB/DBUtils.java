package Utils.DB;

import Config.Configure;

import java.sql.*;

public class DBUtils {
    public static Connection SQL_CON = null;
    /**
     * get connection from db, this method is used in multi thread.
     * TODO: not sure whether should we do this
     * @return sql conenction
     */
    public static Connection getNewConnection() {
        Connection connection = null;

        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://").append(Configure.URL).append(":").append(Configure.PORT).append("/mud?serverTimezone=GMT");

        try {
            connection = DriverManager
                    .getConnection(sb.toString(),Configure.USERNAME, Configure.PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }
        return connection;
    }
}
