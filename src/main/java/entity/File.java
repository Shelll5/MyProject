package entity;

/**
 * Created by Mila on 11.05.2017.
 */
public class File extends BaseEntity {

    private String imageGuid;

    public String getImageGuid() {
        return imageGuid;
    }

    public void setImageGuid(String imageGuid) {
        this.imageGuid = imageGuid;
    }

    @Override
    public String toString() {
        return "File{" +
                "imageGuid='" + imageGuid + '\'' +
                "} " + super.toString();
    }
}
