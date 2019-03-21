package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class DBUtils {
    public static Connection getConnection(ConnectionType connectionType){
        Connection connection = null;
        switch (connectionType){
            case MYSQL :
                String connectionUrl = "jdbc:mysql://localhost:3306/JDBC_DAO";
                String userName = "root";
                String password = "rootpassword";

                Properties properties = new Properties();
                properties.setProperty("user", userName);
                properties.setProperty("password", password);
                properties.setProperty("useSSL", "false");
                try {
                    connection = DriverManager.getConnection(connectionUrl, properties);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                break;
        }
        return connection;
    }
}
