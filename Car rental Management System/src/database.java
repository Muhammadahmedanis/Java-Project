import java.sql.Connection;
import java.sql.DriverManager;

public class database {
    public static Connection connectDb(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection("Jdbc:mysql://localhost/rentcar", "root", ""); // root this is the default userName while "" or empty is for the password
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
