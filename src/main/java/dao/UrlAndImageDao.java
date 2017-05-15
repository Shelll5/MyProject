package dao;

import entity.Image;
import entity.URL;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mila on 09.05.2017.
 */
public interface UrlAndImageDao {
    /**
     * create url     *
     * upload images
     */
    void addUrlAndUploadImages(String urlName, List<String> images) throws SQLException;
    /**
     * read url
     */
    public List<URL> getAllUrls() throws SQLException;
    /**
     * read url by name
     */
    public List<URL> getUrlsByName(String name) throws SQLException;
    /**
     * read imege by name
     */
    public List<Image> getImageByUrlName(String name) throws SQLException;
    /**
     * read image
     */
    public List<Image> getAllImages() throws SQLException;
    /**
     * download image
     */
    public void downloadImage(String path, String folderPath);
    /**
     * create table database if not exist
     */
    public void createTables() throws SQLException;

}
