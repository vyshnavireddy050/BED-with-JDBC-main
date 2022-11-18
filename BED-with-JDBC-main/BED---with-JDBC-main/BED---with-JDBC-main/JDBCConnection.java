package JDBC;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
public static Connection getConnection() {
		
		Connection conn = null;
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database1", "root", "Poojitha");
		} catch (ClassNotFoundException e) {
			System.out.println("Error - " + e);
		} catch (SQLException e) {
			System.out.println("Error - " + e);
		}
		return conn;
	}
	
}


