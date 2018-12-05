package Utils.DB;

import Config.Configure;

import java.sql.*;

public class SingleConnection {
    private static SingleConnection instance = new SingleConnection();
    private Connection connection;

    private SingleConnection() {
        connection = null;

        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://").append(Configure.URL).append(":").append(Configure.PORT).append('/').append(Configure.DB_NAME).append("?serverTimezone=GMT");

        try {
            connection = DriverManager
                    .getConnection(sb.toString(),Configure.USERNAME, Configure.PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
    }

    public static SingleConnection getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
