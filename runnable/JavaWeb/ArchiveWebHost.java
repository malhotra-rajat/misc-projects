import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

/** A loopback-only host for the original servlet and JSP coursework. */
public class ArchiveWebHost {
    public static void main(String[] args) throws Exception {
        Tomcat server = new Tomcat();
        server.setBaseDir(args[0]);
        server.setPort(Integer.parseInt(args[2]));
        server.getConnector().setProperty("address", "127.0.0.1");
        Context context = server.addWebapp("", args[1]);
        context.setParentClassLoader(ArchiveWebHost.class.getClassLoader());
        Tomcat.addServlet(context, "archived-login", new LoginServlet());
        context.addServletMappingDecoded("/login", "archived-login");
        server.start();
        System.out.println("Archive Java web labs: http://127.0.0.1:"+args[2]);
        server.getServer().await();
    }
}
