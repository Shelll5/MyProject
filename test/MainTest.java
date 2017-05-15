import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Mila on 22.04.2017.
 */
//@RunWith(value=org.junit.runners.Parameterized.class)
public class MainTest {
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