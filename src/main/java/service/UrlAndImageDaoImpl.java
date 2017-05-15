package service;

import dao.UrlAndImageDao;
import entity.*;
import org.h2.jdbc.JdbcSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import java.io.*;
import java.io.File;
import java.net.URLConnection;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Mila on 09.05.2017.
 */
public class UrlAndImageDaoImpl implements UrlAndImageDao {

    private final static Logger LOG = LoggerFactory.getLogger(UrlAndImageDaoImpl.class);
    private Util util = new Util();

    public String generatedGuid(){
        return UUID.randomUUID().toString();
    }

    @Override
    public void addUrlAndUploadImages(String urlName, List<String> images) throws SQLException {
        URLConnection urlConnection;
        Connection connection = util.getConnetion();
        PreparedStatement urlState = null;
        PreparedStatement imageState = null;
        PreparedStatement uploadState = null;
        URL url = createUrl(urlName);
        String sql = "insert into URLS (guid , url_name, create_date, last_editing_date) values (?, ?, ?, ?)";
        String sqlImage = "insert into IMAGES (guid , image_path, URL_GUID, create_date, last_editing_date) values (?, ?, ?, ?, ?)";
        String sqlUpload = "insert into FILES (guid, IMAGE_GUID , file, create_date, last_editing_date) values (?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);

            urlState = connection.prepareStatement(sql);
            imageState = connection.prepareStatement(sqlImage);
            uploadState = connection.prepareStatement(sqlUpload);

            urlState.setString(1 , url.getGuid());
            urlState.setString(2, url.getUrlName());
            urlState.setDate(3, new java.sql.Date(url.getCreateDate().getTime()));
            urlState.setDate(4, new java.sql.Date(url.getLastEditingDate().getTime()));
            urlState.executeUpdate();

            for (String imagePath : images) {
                Image image = createImage(imagePath, url.getGuid());
                imageState.setString(1, image.getGuid());
                imageState.setString(2, imagePath);
                imageState.setString(3, image.getUrlGuid());
                imageState.setDate(4, new Date(image.getCreateDate().getTime()));
                imageState.setDate(5, new Date(image.getLastEditingDate().getTime()));
                imageState.executeUpdate();

                urlConnection = new java.net.URL(imagePath).openConnection();

                entity.File file = createFile(image.getGuid());
                uploadState.setString(1, file.getGuid());
                uploadState.setString(2, file.getImageGuid());
                uploadState.setBinaryStream(3, urlConnection.getInputStream());
                uploadState.setDate(4, new Date(file.getCreateDate().getTime()));
                uploadState.setDate(5, new Date(file.getLastEditingDate().getTime()));
                uploadState.executeUpdate();
            }
            connection.commit();
        } catch (FileNotFoundException e){
            LOG.error("File not found");
            e.printStackTrace();
            connection.rollback();
        }
        catch (JdbcSQLException e){
            e.printStackTrace();
            LOG.error("Database not found");
            connection.rollback();
        }
        catch (SQLException e){
            LOG.error("Transaction is being rolled back");
            connection.rollback();
        } catch (IOException e) {
            LOG.error("No file");
            connection.rollback();
            e.printStackTrace();
        } finally {
            if (urlState != null){
                urlState.close();
            }
            if (imageState != null){
                imageState.close();
            }
            if (uploadState != null){
                uploadState.close();
            }
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    private URL createUrl(String urlname){
        URL url = new URL();
        url.setGuid(generatedGuid());
        url.setUrlName(urlname);
        url.setCreateDate(new java.util.Date());
        url.setLastEditingDate(new java.util.Date());
        return url;
    }
    private Image createImage(String imagePath, String urlGuid){
        Image image = new Image();
        image.setGuid(generatedGuid());
        image.setUrlGuid(urlGuid);
        image.setImagePath(imagePath);
        image.setLastEditingDate(new java.util.Date());
        image.setCreateDate(new java.util.Date());
        return image;
    }
    private entity.File createFile(String imageGuid){
        entity.File file = new entity.File();
        file.setGuid(generatedGuid());
        file.setLastEditingDate(new java.util.Date());
        file.setCreateDate(new java.util.Date());
        file.setImageGuid(imageGuid);
        return file;
    }

    @Override
    public List<URL> getAllUrls() throws SQLException {
        List<URL> urlses = new ArrayList<>();
        Connection connection = util.getConnetion();
        Statement ps = null;
        String sql = "select * from URLS ORDER BY LAST_EDITING_DATE DESC";
        try {
            ps = connection.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()){
               URL urls = new URL();
                urls.setGuid(rs.getString("guid"));
                urls.setUrlName(rs.getString("url_name"));
                urls.setCreateDate(rs.getDate("create_date"));
                urls.setLastEditingDate(rs.getDate("last_editing_date"));
                urlses.add(urls);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (ps != null){
                ps.close();
            }
            if (connection != null){
                connection.close();
            }
        }
        return urlses;
    }

    @Override
    public List<URL> getUrlsByName(String urlName) throws SQLException {
        Connection connection = util.getConnetion();
        PreparedStatement ps = null;
        List<URL> urlses = new ArrayList<>();
        URL urls = new URL();
        String sql = "select * from URLS where URL_NAME like ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + urlName + "%");
            ResultSet resultSet =  ps.executeQuery();
            if (resultSet.next()){
                urls.setGuid(resultSet.getString("guid"));
                urls.setUrlName(resultSet.getString("url_name"));
                urls.setCreateDate(resultSet.getDate("create_date"));
                urls.setLastEditingDate(resultSet.getDate("last_editing_date"));
                urlses.add(urls);
            }
        }  catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (ps != null){
                ps.close();
            }
            if (connection != null){
                connection.close();
            }
        }
        return urlses;
    }

    @Override
    public List<Image> getImageByUrlName(String name) throws SQLException {
        Connection connection = util.getConnetion();
        PreparedStatement ps = null;
        List<Image> images = new ArrayList<>();
        String sql = "select * from IMAGES im " +
                "INNER JOIN URLS ur WHERE im.URL_GUID = ur.GUID AND ur.URL_NAME LIKE ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Image image = new Image();
                image.setGuid(rs.getString("guid"));
                image.setUrlGuid(rs.getString("url_guid"));
                image.setImagePath(rs.getString("image_path"));
                image.setCreateDate(rs.getDate("create_date"));
                image.setLastEditingDate(rs.getDate("last_editing_date"));
                images.add(image);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (ps != null){
                ps.close();
            }
            if (connection != null){
                connection.close();
            }
        }
        return images;
    }

    @Override
    public List<Image> getAllImages() throws SQLException {
        Connection connection = util.getConnetion();
        PreparedStatement ps = null;
        List<Image> images = new ArrayList<>();
        String sql = "select * from IMAGES ORDER BY LAST_EDITING_DATE DESC";
        try {
            ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Image image = new Image();
                image.setGuid(rs.getString("guid"));
                image.setUrlGuid(rs.getString("url_guid"));
                image.setImagePath(rs.getString("image_path"));
                image.setCreateDate(rs.getDate("create_date"));
                image.setLastEditingDate(rs.getDate("last_editing_date"));
                images.add(image);
            }
        } catch (SQLException e){
            LOG.error("Error review image");
            e.printStackTrace();
        } finally {
            if (ps != null){
                ps.close();
            }
            if (connection != null){
                connection.close();
            }
        }
        return images;
    }

    @Override
    public void downloadImage(String path, String folderPath) {
        Connection connection = util.getConnetion();
        String sql = "select * from FILES f INNER join IMAGES im WHERE" +
                " f.IMAGE_GUID = im.GUID AND im.IMAGE_PATH = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, path);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                Blob file = res.getBlob("file");
                BufferedInputStream bis = new BufferedInputStream(file.getBinaryStream());
                InputStream a = file.getBinaryStream();
                File folder = new File("pic\\" + folderPath + "\\");
                if (!folder.exists()){
                    folder.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(folder.toString(), "pic" + System.currentTimeMillis() + ".jpg"));
                int i;
                while((i = bis.read()) != -1){
                    fos.write(i);
                }
                bis.close();
                fos.flush();
                fos.close();
            }
        } catch (SQLException | IOException e) {
            System.out.println("Exception :" + e);
            LOG.error("File error");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("Download file error");
            }
        }

    }

    @Override
    public void createTables() throws SQLException {
        Connection connection = util.getConnetion();
        PreparedStatement urlTable = null;
        PreparedStatement imageTable = null;
        PreparedStatement fileTable = null;
        try {
        String sqlUrl = "CREATE TABLE IF NOT EXISTS URLS" +
                "   (GUID VARCHAR(36) NOT NULL," +
                "    URL_NAME VARCHAR(50) NOT NULL," +
                "    CREATE_DATE DATE NOT NULL," +
                "    LAST_EDITING_DATE DATE NOT NULL," +
                "    CONSTRAINT PK_URL PRIMARY KEY (GUID));";
        String sqlImage = "CREATE TABLE IF NOT EXISTS IMAGES" +
                "    (GUID VARCHAR(36) NOT NULL," +
                "    IMAGE_PATH VARCHAR(150) NOT NULL," +
                "    CREATE_DATE DATE NOT NULL," +
                "    LAST_EDITING_DATE DATE NOT NULL," +
                "    URL_GUID VARCHAR(36)," +
                "    CONSTRAINT PK_IMAGE PRIMARY KEY (GUID)," +
                "    CONSTRAINT  FK_URL_GUID FOREIGN KEY (URL_GUID) REFERENCES URLS(GUID));";
        String sqlFile = "CREATE TABLE IF NOT EXISTS FILES" +
                "    (GUID VARCHAR(36)  NOT NULL," +
                "    IMAGE_GUID VARCHAR(36) NOT NULL," +
                "    FILE BLOB NOT NULL," +
                "    CREATE_DATE DATE NOT NULL," +
                "    LAST_EDITING_DATE DATE NOT NULL," +
                "    CONSTRAINT PK_FILE PRIMARY KEY (GUID)," +
                "    CONSTRAINT FK_IMAGE_GUID FOREIGN KEY (IMAGE_GUID) REFERENCES IMAGES(GUID));";

            urlTable = connection.prepareStatement(sqlUrl);
            urlTable.executeUpdate();
            imageTable = connection.prepareStatement(sqlImage);
            imageTable.executeUpdate();
            fileTable = connection.prepareStatement(sqlFile);
            fileTable.executeUpdate();
        } catch (SQLException e){
            if (connection != null){
                connection.rollback();
                LOG.error("Ошибка при создани db");
                e.printStackTrace();
            }
        }
    }
}
