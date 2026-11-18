import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/lead_crm",
            "root",
            "admin"
        );

    }
}