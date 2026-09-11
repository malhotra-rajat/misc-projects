
import java.awt.*;
import java.awt.event.*;

public class frameandpanel
{
  public static void main(String[] args){
    Panel panel = new Panel();
    panel.add(new Button("Button 1"));
    panel.add(new Button("Button 2"));
    panel.add(new Button("Button 3"));
    Frame frame = new Frame("Container Frame");
    TextArea txtArea = new TextArea();
    frame.add(txtArea, BorderLayout.CENTER);
    frame.add(panel, BorderLayout.SOUTH);
    frame.setSize(400,400);
    frame.setVisible(true);
    frame.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent we)
      {
        System.exit(0);
      }
    });
  }
}
