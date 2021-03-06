import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

import freemarker.template.*;


public class HttpServer {
    public static Configuration cfg = new Configuration(new Version(2,3,21));
    public static ArrayList<Album> albums = new ArrayList<Album>();

    public static void main(String[] args) throws Throwable{
        ServerSocket ss = new ServerSocket(8080);
        configureFreeMarker();
        while (true) {
            Socket s = ss.accept();
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }
    }

    private static void configureFreeMarker() throws IOException {
        cfg.setDirectoryForTemplateLoading(new File(HttpServer.class.getResource("com/company/webFiles/templates").getPath()));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setEncoding(new Locale("ru"), "UTF-8");
        cfg.setOutputEncoding("windows-1251");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
    }
}
