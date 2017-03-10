
package pitchperfectv1.pkg0;
import java.sql.*;
public class DatabaseConn {
    Connection conn = null; 
    public void connectDB(){
        try{
            conn =  DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll","root","");
            if(!conn.isClosed()){
                System.out.println("Database is successfully connected.");
            }
        }
        catch(SQLException e){
            ErrorHandler.display(e.getMessage());
        }
    }
}
