package util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Mila on 09.05.2017.
 */
public class Util {
    private final static Logger LOG = LoggerFactory.getLogger(Util.class);
    private final static String USER = "admin";
    private final static String PASSWORD = "admin";
    public Connection getConnetion(){
        Connection connection = null;
        String driver = "jdbc:h2:";
        String path = System.getProperty("user.dir");
        String URL = driver + path + "/db";
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            //System.out.println("successful connection");
        }  catch ( SQLException exception){
            LOG.error("Wrong connection ! Database may be already in use");
        } catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }
}
