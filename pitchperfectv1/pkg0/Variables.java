/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;

import java.sql.Time;
import java.util.Date;


public class Variables {
    private String title;
    private String date;
    private String time;
    
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    
    public String getDate(){
        return date;
    }
    
    public void setDate(String date){
        this.date = date;
    }
    
    public String getTime(){
        return time;
    }
    
    public void setTime(String time){
        this.time = time;
    }
    
    public String Eliminate(){
        Module module = new Module();
        String var = "delete from current_history where title = '" + title + "';";
        return module.Execute(var);
        
    }
    public String Insert(){
        Module module = new Module();
        String var = "insert into current_history values('" + this.getTitle() + 
                "',' " + this.getDate() + "', '" + this.getTime() + "';";
        System.out.println("Successful");
        return module.Execute(var);
    }
    
    
}
