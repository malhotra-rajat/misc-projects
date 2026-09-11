import java.applet.*;
import java.awt.*;
import java.awt.event.*;
/*
 * <applet code="manageuserapplet" width=600 height=150>
 * </applet>
 */
public class manageuserapplet extends Applet implements ActionListener 
{
  TextField one,two,three;
  Label lb1,lb2,lb3;
  Button bt1,bt2;
  String msg="";
  public void init()
  {
	  lb1=new Label("Enter first number: ",Label.RIGHT);
	  lb2=new Label("Enter second number: ",Label.RIGHT);
	  lb3=new Label("Enter third number: ",Label.RIGHT);
	  one=new TextField(4);
	  two=new TextField(4);
	  three=new TextField(4);
	  bt1=new Button("Start");
	  bt2=new Button("Show result");
	  one.setVisible(false);
	  two.setVisible(false);
	  three.setVisible(false);
	  lb1.setVisible(false);
	  lb2.setVisible(false);
	  lb3.setVisible(false);
	  bt2.setVisible(false);
	  add(lb1);
	  add(one);
	  add(lb2);
	  add(two);
	  add(lb3);
	  add(three);
	  add(bt1);
	  add(bt2);
	  bt1.addActionListener(this);
	  bt2.addActionListener(this);
	  one.addActionListener(this);
	  two.addActionListener(this);
	  three.addActionListener(this);
  }
  public void actionPerformed(ActionEvent ae)
  {
	String str=ae.getActionCommand();
	if(str.equals("Start"))
	{
		bt2.setVisible(true);
		lb1.setVisible(true);
		lb2.setVisible(true);
		lb3.setVisible(true);
		bt1.setVisible(false);
		one.setVisible(true);
		two.setVisible(true);
		three.setVisible(true);
	}
	if(str.equals("Show result"))
	{
		
		String char1=one.getText();
		String char2=two.getText();
		String char3=three.getText();
			try
			{
				int x=Integer.parseInt(char1);
				int y=Integer.parseInt(char2);
				int z=Integer.parseInt(char3);
				if(x>=y && x>=z)
				{
					msg+=char1+" is greatest";
					repaint();
				}
				if(y>x && y>z)
				{
					msg+=char2+" is greatest";
					repaint();
				}
				else 
				{
					msg+=char3 +" is greatest";
					repaint();
				}
			}
			catch(Exception e)
			{
				
			}
		
	}
  }
  public void paint(Graphics g)
  {
	  g.drawString(msg,5,100);
  }
}
