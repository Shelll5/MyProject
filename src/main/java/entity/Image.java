package entity;

/**
 * Created by Mila on 09.05.2017.
 */
public class Image extends BaseEntity {

    private String urlGuid;

    private String imagePath;

    public String getUrlGuid() {
        return urlGuid;
    }

    public void setUrlGuid(String urlGuid) {
        this.urlGuid = urlGuid;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Image: " +
                "urlGuid='" + urlGuid + '\'' +
                ", imagePath='" + imagePath + '\'' + " , " +
                 super.toString();
    }
}
