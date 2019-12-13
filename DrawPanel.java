


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class DrawPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int height = 760;
	private int width = 760;
	public DataBase result;
	

	public DrawPanel(DataBase db) {
		this.result = db;

	}

	@Override
	public void paintComponent(Graphics g) {
		/*Graphics2D g2d = (Graphics2D) g;
		int w2 = getWidth() / 2;
        int h2 = getHeight() / 2;
		g2d.rotate(Math.PI/2);
		super.paintComponent(g);*/
		//int distanceX = (width / x);
		//int distanceY = (height / y);
		int distanceX = 21;
		int distanceY = 21;
		//System.out.println(distanceX + "," + distanceY);

		//Grid Lines
		int j;
		for (j = 0; j <= width; j += distanceX)
			g.drawLine(j, 0, j, height);
		for (j = 0; j <= height; j += distanceY)
			g.drawLine(0, j, width, j);
		
			
		for (DataPoint location : result.database) {
			//System.out.println(location.x + " " + location.y);
			drawProduct(g, location.x, location.y,distanceX, distanceY);
		}
		
	}

	private void drawProduct(Graphics g, float x, float y, int distanceX,
			int distanceY) {
		int widthStip = (int) (1200 / distanceX) / 14;//point size
		x = distanceX * x;
		y = distanceY * y;
		double middleX = ((distanceX / 2) - widthStip / 2 + x);
		double middleY = ((distanceY / 2) - widthStip / 2 + y);

		int middleXInt = (int) middleX;
		int middleYInt = (int) middleY;
		g.setColor(Color.black);
		g.fillOval(middleXInt, middleYInt, widthStip, widthStip);
	}

	
	public void setResult(DataBase db) {
		this.result = db;
	}
	
}