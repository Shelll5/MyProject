package entity;

/**
 * Created by Mila on 09.05.2017.
 */
public class URL extends BaseEntity {

    private String urlName;

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    @Override
    public String toString() {
        return "urlName='" + urlName + '\'' +
                " , " + super.toString();
    }
}
