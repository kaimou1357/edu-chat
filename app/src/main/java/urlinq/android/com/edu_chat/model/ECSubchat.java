package urlinq.android.com.edu_chat.model;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Kai Mou on 1/12/2016.
 */
public class ECSubchat extends ECObject {
    private final String name;
    private final int privacy;
    private final String description;

    public ECSubchat(JSONObject subchat) throws JSONException{
        super(subchat.getInt("id"), null, null);
        this.name = subchat.getString("name");
        this.privacy = subchat.getInt("privacy");
        this.description = subchat.getString("description");
        Log.d("Subchat Creation", name + privacy + description);

    }

    public String getDescription(){
        return this.description;
    }
    public int getPrivacy(){
        return this.privacy;
    }
    public String getName(){
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.privacy);
        dest.writeString(this.name);
        dest.writeString(this.description);
    }

    protected ECSubchat(Parcel in) {
        super(in);
        this.privacy = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
    }
    public static final Creator<ECSubchat> CREATOR = new Creator<ECSubchat>() {
        public ECSubchat createFromParcel(Parcel source) {
            return new ECSubchat(source);
        }

        public ECSubchat[] newArray(int size) {
            return new ECSubchat[size];
        }
    };

    public String toString(){

        return "ECSubChat{" +
                " name='" + name + '\'' +
                " privacysetting='" + privacy + '\'' +
                " subchat description='" + description + '\'' +
                '}' + super.toString();
    }
}
