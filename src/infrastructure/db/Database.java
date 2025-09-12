package infrastructure.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Database {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Properties props = loadProperties();
            String url = props.getProperty("dburl");
            return DriverManager.getConnection(url, props);
        } catch (SQLException | ClassNotFoundException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("resources/application.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }
}

class DbException extends RuntimeException {
    public DbException(String message) {
        super(message);
    }
}
