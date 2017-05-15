package util;

import org.h2.jdbc.JdbcSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Mila on 09.05.2017.
 */
public class Util {
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
            System.out.println("Wrong connection");
            exception.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }
}
