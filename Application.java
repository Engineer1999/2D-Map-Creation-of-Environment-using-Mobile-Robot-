import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.fazecast.jSerialComm.*;


import javax.swing.JFrame;
import javax.swing.JPanel;


class DataPoint {
	
	float relX;		//relative origin abscissa, position of car
	float relY;		//relative origin ordinate, position of car
	float r;			//distance of obstacle
	int theta;      //direction of obstacle
	float x;			//absolute pixel abscissa
	float y;			//absolute pixel ordinate
	
	DataPoint(float relX, float relY, float r, int theta) {
		//assigning received values
		this.relX = relX;
		this.relY = relY;
		this.r = r;
		this.theta = theta;
		this.x = (float) (r*Math.cos(Math.toRadians(theta))); //x = r*cosine(theta in radians) 
		this.y = (float) (r*Math.sin(Math.toRadians(theta))); //y = r*sine(theta in radians);
		//shifting origin back to (0,0)
		this.x = (this.x + relX +150 )*0.12F;								
		this.y = (-this.y - relY+ 150 )*0.12F;	
		System.out.println("----------------------------------"+this.y);
	}
	DataPoint(float x, float y) {
		this.x=(x+150)*0.12F;
		this.y=(-y+150)*0.12F;
	}
	
	void display() {
		System.out.println(relX);
		System.out.println(relY);
		System.out.println(r);
		System.out.println(theta);
	}
	
}

class DataBase {
	
	
	ArrayList<DataPoint> database = new ArrayList<DataPoint>();
	
	int[] getMaxXY() {
		int[] max = {Integer.MIN_VALUE,Integer.MIN_VALUE};
		int i, size;
		size = database.size();
		for(i = 0; i < size; i++) {
			DataPoint temp = database.get(i);
			if(temp.x > max[0])
				max[0] = (int) temp.x;
			if(temp.y > max[1])
				max[1] = (int) temp.y;	
		}
		return max;
	}
	
	
}
final class MessageListener implements SerialPortMessageListener
{
   @Override
   public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }

   @Override
   public byte[] getMessageDelimiter() { return new byte[] { (byte)0x0a }; }

   @Override
   public boolean delimiterIndicatesEndOfMessage() { return true; }

   @Override
   public void serialEvent(SerialPortEvent event)
   {
      byte[] delimitedMessage = event.getReceivedData();
      String msg = new String(delimitedMessage);
      System.out.println("Received the following delimited message: " + msg);
      String[] arr = msg.split("\\s",0);
      DataPoint dp = new DataPoint(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]),Float.parseFloat(arr[2]),Integer.parseInt(arr[3]));
      dp.display();
      Utilities util = new Utilities();
      util.logToDatabase(dp, Application.db);
      
   }
}

class Utilities {
	
	void changeDir(DataPoint dp) {  //tell car to change direction 
		
	}
	
	void logToDatabase(DataPoint dp, DataBase db) { //update DataBase
		
		int i, size;
		size = db.database.size();
		for(i = 0; i < size; i++) {
			if(db.database.get(i).x == dp.x && db.database.get(i).y == dp.y)
			{
				changeDir(dp);
				return;
			}
		}
		db.database.add(dp);
		Application.window.refreshGraph(db);
	}
	
}

class Application {

	
	static DataBase db =  new DataBase(); 
	static View window;
	static int quit=0;
	public static void main(String[] args) throws InterruptedException {
		
		//listPorts();
		window = new View(db);
		window.frame.setVisible(true);
		/*int x=0, y=0;
		Utilities util = new Utilities();
		util.logToDatabase(new DataPoint(50,20), db);
		Thread.sleep(200);
		for(x=0; x<=30 ;x++)
		{
			util.logToDatabase(new DataPoint(x,10), db);
			//util.logToDatabase(new DataPoint(x,30), db);
			Thread.sleep(200);

		}
		for(x=0; x<=30 ;x++)
		{
			//util.logToDatabase(new DataPoint(x,10), db);
			util.logToDatabase(new DataPoint(x,30), db);
			Thread.sleep(200);

		}*//*
		for(x=30; x<=40 ;x++)
		{
			util.logToDatabase(new DataPoint(x,10), db);
			util.logToDatabase(new DataPoint(x,90), db);
			Thread.sleep(200);
		}
		//
		util.logToDatabase(new DataPoint(40,90), db);
		Thread.sleep(200);
		for(y=20; y<=30 ;y++)
		{
			util.logToDatabase(new DataPoint(50,y), db);
			Thread.sleep(200);
		}
		for(y=30; y<=70 ;y++)
		{
			util.logToDatabase(new DataPoint(30,y), db);
			util.logToDatabase(new DataPoint(50,y), db);
			Thread.sleep(200);
		}
		for(y=70; y<=80 ;y++)
		{
			util.logToDatabase(new DataPoint(30,y), db);
			util.logToDatabase(new DataPoint(120,y), db);
			Thread.sleep(200);
		}
		//
		util.logToDatabase(new DataPoint(120,80), db);
		Thread.sleep(200);
		for(x=40; x<=50 ;x++)
		{
			util.logToDatabase(new DataPoint(x,90), db);
			Thread.sleep(200);
		}
		for(x=50; x<=100 ;x++)
		{
			util.logToDatabase(new DataPoint(x,70), db);
			util.logToDatabase(new DataPoint(x,90), db);
			Thread.sleep(200);
		}
		for(x=100; x<=110 ;x++)
		{
			util.logToDatabase(new DataPoint(x,10), db);
			util.logToDatabase(new DataPoint(x,90), db);
			Thread.sleep(200);
		}
		//
		util.logToDatabase(new DataPoint(110,10), db);
		Thread.sleep(200);
		for(y=80; y>=70 ;y--)
		{
			util.logToDatabase(new DataPoint(120,y), db);
			Thread.sleep(200);
		}
		for(y=70; y>=30 ;y--)
		{
			util.logToDatabase(new DataPoint(100,y), db);
			util.logToDatabase(new DataPoint(120,y), db);
			Thread.sleep(200);
		}
		for(y=30; y>=20 ;y--)
		{
			util.logToDatabase(new DataPoint(100,y), db);
			Thread.sleep(200);
		}
		//
		for(x=110; x<=120 ;x++)
		{
			util.logToDatabase(new DataPoint(x,10), db);
			util.logToDatabase(new DataPoint(x,90), db);
			Thread.sleep(200);
		}
		for(x=120; x<=150 ;x++)
		{
			util.logToDatabase(new DataPoint(x,10), db);
			util.logToDatabase(new DataPoint(x,30), db);
			Thread.sleep(200);
		}
	*/

		SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.setBaudRate(115200);
		comPort.openPort();
		MessageListener listener = new MessageListener(); 
		comPort.addDataListener(listener);
		while(true) {
		}		
	}
	
	public static void listPorts() {
		SerialPort[] listOfPorts= SerialPort.getCommPorts();			//create an array of type SerialPorts called listOfPorts, and fill using library getCommPorts method
		if (listOfPorts.length == 0) {								//if no ports found
			System.out.println("No ports found");					//print "No ports found"
		}
		else {
			for (int i = 0; i < listOfPorts.length; i++) {			//if i < size of listOfPorts array
		        
		         System.out.println("Port " + i + ": " + listOfPorts[i].getDescriptivePortName()); //print name of port in array index i
		     }
		}
	
	}
}

class View {

	JFrame frame;
	JPanel panel;
	
	
	public View(DataBase db) {	
		//db = new DataBase();
		initialize(db);
	}

	
	private void initialize(DataBase db) {
		
		frame = new JFrame();
		frame.setBounds(0, 0, 760, 760);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Map");
		
		//graphs
		panel = new DrawPanel(db);
		panel.setBounds(0, 0, 760, 760);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		panel.setVisible(true);

		
	}
	 void refreshGraph(DataBase db) {
		panel = new DrawPanel(db);
		frame.repaint();
	 }
}
