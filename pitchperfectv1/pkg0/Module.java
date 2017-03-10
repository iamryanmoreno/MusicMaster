/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import java.sql.*;

public class Module {
    public ResultSet List (String var){
        try{
            Connection conn =  DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root","");
            PreparedStatement da = conn.prepareStatement(var);
            ResultSet tbl = da.executeQuery();
            if(!conn.isClosed()){
                System.out.println("Connected");
            }
            return tbl;
        } catch(Exception e){
        javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
        e.printStackTrace();
        return null;
    }
    }
    
    public String Execute (String var){
        try{
            Connection conn =  DriverManager.getConnection ("jdbc:mysql://localhost:3306/project","root","");
            PreparedStatement da = conn.prepareStatement(var);
            int r = da.executeUpdate();
            
            if(!conn.isClosed()){
                System.out.println("Connected");
            }
            
            return "Select " + r + " files ";
            
            
        } catch(Exception e){
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
