
import java.applet.*;
import java.awt.*;
public class movingball extends Applet implements Runnable
{
	
	int x_pos = 10;		
	int y_pos = 100;	
	int radius = 30;	

	public void init()
	{
		setBackground (Color.blue);
	}

	public void start ()
	{
		
		Thread th = new Thread (this);
		th.start ();
	}

	public void stop()
	{

	}

	public void destroy()
	{

	}

	public void run ()
	{
		
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		
		while (true)
		{
			
			if(x_pos<this.getWidth())
				x_pos ++;
				else
				x_pos=10;


			
			repaint();

			try
			{
				
				Thread.sleep (20);
			}
			catch (InterruptedException ex)
			{
				
			}

			
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}


	public void paint (Graphics g)
	{
		g.setColor  (Color.red);

		g.fillOval (x_pos- radius, y_pos- radius,2* radius,2* radius);
	}

}
