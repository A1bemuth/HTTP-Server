import javax.net.ssl.SSLSocketFactory;
import org.postgresql.*;
import java.sql.*;

public class Database {
    private String url;
    private Connection db;
    private SSLSocketFactory socketFactory;
    public Database() throws Throwable {
        super();
        Database.class.forName("org.postgresql.Driver");
        this.url = "jdbc:postgresql:postgres";
        this.db = DriverManager.getConnection(url, "postgres", "1234567890");
        //socketFactory = new org.postgresql.ssl.NonValidatingFactory("");

    }

}
