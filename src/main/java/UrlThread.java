import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UrlAndImageDaoImpl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mila on 04.05.2017.
 */
public class UrlThread extends Thread {
    private final static Logger LOG = LoggerFactory.getLogger(UrlThread.class);
    private final static String PATTERN = "^((https?|ftp)\\:\\/\\/)?([a-z0-9]{1})((\\.[a-z0-9-])|([a-z0-9-]))*([a-z0-9]{1})\\.([a-z]{2,6})(\\/?)$";
    private final static String HTTP = ".+((http).+(jpg))";

    private String url;

    private String folderName;

    public UrlThread(String url){
        this.url = url;
    }

    public UrlThread(String url, String folderName){
        this.url = url;
        this.folderName = folderName;
    }

    @Override
    public void run() {
        if (checkUrl(url)){
            String a = "https://www.tut.by/";
            UrlAndImageDaoImpl dao = new UrlAndImageDaoImpl();
            try {
                List<String> images = findJpgLinks(url);
                dao.addUrlAndUploadImages(url, images);
            } catch (SQLException e) {
                e.printStackTrace();
                LOG.error("При згрузке данных произошла ошибка");
            }
        } else {
            LOG.info("Enter correct url");
        }
    }

    private List<String> findJpgLinks(String url) throws SQLException {
        HttpURLConnection connection = null;
        List<String> jpg = new ArrayList<>();
        try {
            connection = (HttpURLConnection) new java.net.URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(500);
            connection.connect();

            StringBuilder sb = new StringBuilder();
            List<String> allText = new ArrayList<>();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()){
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String html;
                while ((html = br.readLine()) != null){
                    allText.add(html);
                }
                for (String text : allText) {
                    String http;
                    if ((http = cutPathJpg(text)) != null){
                        jpg.add(http);
                    }
                }
            } else{
                LOG.info("not found email");
            }

        }catch (MalformedURLException e){
            LOG.info("Укажите протокол");
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return jpg;
    }

    private static String cutPathJpg(String http){
        Matcher matcher = Pattern.compile(HTTP).matcher(http);
        String res = null;
        while(matcher.find()){
            res = matcher.group(1);
        }
        return res;
    }

    public static boolean checkUrl(String url){
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

}
