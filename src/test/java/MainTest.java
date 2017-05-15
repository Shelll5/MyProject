import org.junit.Before;
import org.junit.Test;
import service.UrlAndImageDaoImpl;
import util.Util;
import java.sql.Connection;
import static org.junit.Assert.*;

/**
 * Created by Mila on 22.04.2017.
 */
//@RunWith(value=org.junit.runners.Parameterized.class)
public class MainTest {
    private Util util = new Util();
    private UrlAndImageDaoImpl urlAndImageDao = new UrlAndImageDaoImpl();

    @Test
    public void testConnection() throws Exception{
        Connection connection = util.getConnetion();
        assertFalse(connection.isClosed());
    }
    @Test
    public void testCheckUrl() throws Exception {
        String url = "mail.ru";
        String errUrl = "sdfsfd sdf sadf sd";
        boolean correctUrl = UrlThread.checkUrl(url);
        boolean incorrectUrl = UrlThread.checkUrl(errUrl);
        assertTrue(correctUrl);
        assertFalse(incorrectUrl);
    }
}