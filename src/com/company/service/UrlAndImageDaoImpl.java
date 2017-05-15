package service;

import dao.UrlAndImageDao;
import entity.Image;
import entity.URL;
import org.h2.engine.SysProperties;
import org.h2.jdbc.JdbcSQLException;
import util.Util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mila on 09.05.2017.
 */
public class UrlAndImageDaoImpl implements UrlAndImageDao {
    private Util util = new Util();

    public String generatedGuid(){
        return UUID.randomUUID().toString();
    }

    public Long currentTime(){
        return System.currentTimeMillis();
    }

    @Override
    public void addUrlAndUploadImages(String urlName, List<String> images) throws SQLException {
        URLConnection urlConnection;
        Connection connection = util.getConnetion();
        PreparedStatement urlState = null;
        PreparedStatement imageState = null;
        PreparedStatement uploadState = null;
        String sql = "insert into URLS (guid , url_name, create_date, last_editing_date) values (?, ?, ?, ?)";
        String sqlImage = "insert into IMAGES (guid , image_path, url_name, create_date, last_editing_date) values (?, ?, ?, ?, ?)";
        String sqlUpload = "insert into FILES (guid, file_path, file, create_date, last_editing_date) values (?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);

            urlState = connection.prepareStatement(sql);
            imageState = connection.prepareStatement(sqlImage);
            uploadState = connection.prepareStatement(sqlUpload);

            urlState.setString(1 , generatedGuid());
            urlState.setString(2, urlName);
            urlState.setDate(3, new java.sql.Date(currentTime()));
            urlState.setDate(4, new java.sql.Date(currentTime()));
            urlState.executeUpdate();

            for (String imagePath : images) {
                imageState.setString(1, generatedGuid());
                imageState.setString(2, imagePath);
                imageState.setString(3,urlName);
                imageState.setDate(4, new Date(currentTime()));
                imageState.setDate(5, new Date(currentTime()));
                imageState.executeUpdate();

                urlConnection = new java.net.URL(imagePath).openConnection();

                uploadState.setString(1, generatedGuid());
                uploadState.setString(2, imagePath);
                uploadState.setBinaryStream(3, urlConnection.getInputStream());
                uploadState.setDate(4, new Date(currentTime()));
                uploadState.setDate(5, new Date(currentTime()));
                uploadState.executeUpdate();
            }
            connection.commit();
        } catch (FileNotFoundException e){
            System.err.println("File not found");
            connection.rollback();
        }
        catch (JdbcSQLException e){
            e.printStackTrace();
            System.err.println("Database not found");
            connection.rollback();
        }
        catch (SQLException e){
            System.err.println("Transaction is being rolled back");
            connection.rollback();
        } catch (IOException e) {
            System.err.println("No file");
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
        List<entity.URL> urlses = new ArrayList<>();
        entity.URL urls = new URL();
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
        String sql = "select * from IMAGES WHERE URL_NAME LIKE ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Image image = new Image();
                image.setGuid(rs.getString("guid"));
                image.setUrlName(rs.getString("url_name"));
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
                image.setUrlName(rs.getString("url_name"));
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
    public void downloadImage(String path, String folderPath) {
        Connection connection = util.getConnetion();
        String sql = "select * from FILES WHERE FILE_PATH = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, path);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                String name = res.getString("file_path");
                Blob file = res.getBlob("file");
                BufferedInputStream bis = new BufferedInputStream(file.getBinaryStream());
                File folder = new File("pic\\" + folderPath + "\\");
                if (!folder.exists()){
                    folder.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(folder.toString(), "pic.jpg"));
                int i;
                if ((i = bis.read()) != -1){
                    fos.write(i);
                }
                bis.close();
                fos.flush();
                fos.close();
            }
        } catch (SQLException | IOException e) {
            System.out.println("Exception :" + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void createTables() throws SQLException {
        Connection connection = util.getConnetion();
        PreparedStatement urlTable = null;
        PreparedStatement imageTable = null;
        PreparedStatement fileTable = null;
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
                "    URL_NAME VARCHAR(50)," +
                "    CONSTRAINT PK_IMAGE PRIMARY KEY (GUID));";
        String sqlFile = "CREATE TABLE IF NOT EXISTS FILES" +
                "    (GUID VARCHAR(36)  NOT NULL," +
                "    FILE_PATH VARCHAR(150) NOT NULL," +
                "    FILE BLOB NOT NULL," +
                "    CREATE_DATE DATE NOT NULL," +
                "    LAST_EDITING_DATE DATE NOT NULL," +
                "    CONSTRAINT PK_FILE PRIMARY KEY (GUID));";
        try {
            urlTable = connection.prepareStatement(sqlUrl);
            urlTable.executeUpdate();
            imageTable = connection.prepareStatement(sqlImage);
            imageTable.executeUpdate();
            fileTable = connection.prepareStatement(sqlFile);
            fileTable.executeUpdate();
        } catch (SQLException e){
            if (connection != null){
                connection.rollback();
                System.err.println("Ошибка при создани db");
                e.printStackTrace();
            }
        }
    }
}
