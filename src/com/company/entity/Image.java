package entity;

/**
 * Created by Mila on 09.05.2017.
 */
public class Image extends BaseEntity {

    private String urlName;

    private String imagePath;

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
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
                "urlName='" + urlName + '\'' +
                ", imagePath='" + imagePath + '\'' + " , " +
                 super.toString();
    }
}
