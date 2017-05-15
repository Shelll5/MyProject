package entity;

import java.sql.Blob;

/**
 * Created by Mila on 11.05.2017.
 */
public class File extends BaseEntity{

    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "File{" +
                "filePath='" + filePath + '\'' +
                "} " + super.toString();
    }
}
