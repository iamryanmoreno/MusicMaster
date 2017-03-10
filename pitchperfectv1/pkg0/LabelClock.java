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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class LabelClock extends JLabel implements Runnable
{

	private FontRenderContext frc;

	private FontMetrics fontMetrics;

	private Font font;

	private String text = "00:00:00";

	private Color outlineColor = Color.green;

	private Color fillColor = Color.black;

	public LabelClock()
	{
		font = new Font("Atomic Clock Radio", 0, 20);
		new Thread(this).start();
	}

	public void run()
	{
		while (true)
		{
			repaint();
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public Dimension getPreferredSize()
	{
		return new Dimension(120, 30);
	}

	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}

	public LabelClock(Font font)
	{
		this.font = font;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	private Graphics2D setAntiAliasing(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		// Activate anti aliasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return g2;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D graph = setAntiAliasing(g);

		g.setFont(font);
		fontMetrics = graph.getFontMetrics();
		frc = graph.getFontRenderContext();

		TextLayout textlay = new TextLayout(text, font, frc);

		int msgWidth = fontMetrics.stringWidth(text);
		int msgHeight = fontMetrics.getHeight();

		float sw = (float) ((getWidth() * 0.5) - (msgWidth * 0.5));
		float sh = (float) ((getHeight() * 0.5) + (msgHeight * 0.5));

		Shape sha = textlay.getOutline(AffineTransform.getTranslateInstance(sw, sh));

		graph.setColor(outlineColor);
		graph.draw(sha);
		graph.setColor(fillColor);
		graph.fill(sha);

	}
	public java.awt.Font getFont()
	{
		return font;
	}

	public void setFont(java.awt.Font font)
	{
		this.font = font;
		repaint();
	}

	public java.awt.Color getOutlineColor()
	{
		return outlineColor;
	}

	public void setOutlineColor(java.awt.Color outlineColor)
	{
		this.outlineColor = outlineColor;
	}

	public java.awt.Color getFillColor()
	{
		return fillColor;
	}

	public void setFillColor(java.awt.Color fillColor)
	{
		this.fillColor = fillColor;
	}
}
