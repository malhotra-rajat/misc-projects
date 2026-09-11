import java.applet.Applet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** Runs an unchanged historical applet in a standalone desktop window on JDK 23. */
@SuppressWarnings("removal")
public class ArchiveAppletHost {
    public static void main(String[] args) throws Exception {
        String name = args.length == 0 ? "movingball" : args[0];
        if (!name.equals("movingball") && !name.equals("manageuserapplet"))
            throw new IllegalArgumentException("Choose movingball or manageuserapplet");
        Applet applet = (Applet)Class.forName(name).getDeclaredConstructor().newInstance();
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new JFrame("Archived Java applet: " + name);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(applet);
            frame.setSize(640, 360);
            frame.setVisible(true);
            applet.init();
            // Older browsers relaid out the applet after visibility changes.
            for (java.awt.Component component : applet.getComponents())
                if (component instanceof java.awt.Button button)
                    button.addActionListener(event -> {
                        applet.invalidate();applet.validate();frame.validate();applet.repaint();
                    });
            applet.validate();
            applet.start();
        });
    }
}
