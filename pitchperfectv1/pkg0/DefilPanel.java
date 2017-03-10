/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;

import javax.swing.JPanel;

public class DefilPanel extends JPanel{
    
    String msg = "";
    int x_coord = 0;
    int y_coord = 0;
    Image img = null;
    Graphics gi = null;
    int len = 0;
    
    public DefilPanel(String s){
        msg = s;
        setFont(new Font("Atomic Clock Radio"/* "TimesRoman" */, Font.BOLD, 24));
        
        try {
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    void init() throws Exception {
        this.setLayout(new BorderLayout());
        this.setBackground(SystemColor.controlText);
        this.setSize(new Dimension(100, 30));
    }

    
    public void setMsgPosX(int new_x) {
        x_coord = new_x;
    }
    
    public void setMsgPosY(int new_y) {
        y_coord = new_y;
    }
    
    public int getMsgPosX() {
        return x_coord;
    }
    
    public int getMsgPosY() {
        return y_coord;
    }
    
    public int getMsgLength() {
        return len;
    }
    
    public void setMsgLength(int v) {
        len = v;
    }
    
    public void setString(String s) {
        msg = s;
        len = 0;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (len == 0) {
            //calculate the measure of text
            FontMetrics fm = g.getFontMetrics();
            len = fm.stringWidth(msg);
            x_coord = getSize().width;
            y_coord = (getSize().height - fm.getHeight()) / 2 + fm.getAscent();
        }
        
        g.setColor(Color.white);
        // System.out.println("x = " + x_coord + " y = " + y_coord);
        g.drawString(msg, x_coord, y_coord);
    }
}
