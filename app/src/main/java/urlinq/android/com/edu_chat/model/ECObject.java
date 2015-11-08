package urlinq.android.com.edu_chat.model;

import android.util.Log;

/**
 * Created by Jacob on 10/27/15.
 */
public class ECObject {

    private final String objectIdentifier;
    private final String fileURL;
    private String color;

    public ECObject(String objectIdentifier, String fileURL) {
        if (objectIdentifier == null || fileURL == null) {
            Log.w(String.format("EDU.CHAT %s", getClass().getSimpleName()), "ECObject create with null property");
        }
        this.objectIdentifier = objectIdentifier;
        this.fileURL = fileURL;
        Log.v(String.format("EDU.CHAT %s", getClass().getSimpleName()), this.toString());
    }

    public String getFileURL() {
        return fileURL;
    }

    public String getObjectIdentifier() {
        return objectIdentifier;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ECObject{" +
                "color='" + color + '\'' +
                ", objectIdentifier='" + objectIdentifier + '\'' +
                ", fileURL='" + fileURL + '\'' +
                '}';
    }
}
