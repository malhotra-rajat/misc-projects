class ABC
{
	synchronized void disp(String s)
	{
		System.out.print("("+s);
		try
		{
			Thread.sleep(2000);
		}
		catch(InterruptedException e)
		{
			
		}
		System.out.print(") ");
	}
}
class threadnew implements Runnable
{
	ABC ob;
	String s;
	Thread t;
	threadnew(ABC a,String m)
	{
		s=m;
		ob=a;
		t=new Thread(this);
		t.start();
	}
	public void run()
	{
		ob.disp(s);
	}
}
public class multithread 
{
   public static void main(String args[])
   {
	   ABC a=new ABC();
	   threadnew t1=new threadnew(a,"JAVA");
	   threadnew t2=new threadnew(a,"PROGRAMMING");
	   try
	   {
		   t1.t.join();
		   t2.t.join();
	   }
	   catch(InterruptedException e)
	   {
		   
	   }
	   System.out.println("Main Now Ends");
   }
}

/* OUTPUT
 * (JAVA) (PROGRAMMING) Main Now Ends */
