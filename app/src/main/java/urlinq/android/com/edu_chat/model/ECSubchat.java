package urlinq.android.com.edu_chat.model;

import android.os.Parcel;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kai Mou on 1/12/2016.
 */
public class ECSubchat extends ECObject {
	private final String name;
	private final int privacy;
	private final int origin_id;
	private final int subchannel_id;
	private final String origin_type;
	private final String description;

	public ECSubchat(JSONObject subchat) throws JSONException {
		super(subchat.getInt("id"), null, null);
		this.name = subchat.getString("name");
		this.privacy = subchat.getInt("privacy");
		this.subchannel_id = subchat.getInt("id");
		this.origin_id = subchat.getInt("origin_id");
		this.origin_type = subchat.getString("origin_type");
		this.description = subchat.getString("description");
		Log.d("Subchat Creation", origin_type + origin_id);

	}

	public String getDescription() {
		return this.description;
	}

	public int getPrivacy() {
		return this.privacy;
	}

	public String getName() {
		return this.name;
	}

	public int getOrigin_id() {
		return this.origin_id;
	}

	public String getOrigin_type() {
		return this.origin_type;
	}

	public int getSubchannel_id() {
		return this.subchannel_id;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(this.privacy);
		dest.writeInt(this.origin_id);
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeInt(this.subchannel_id);
		dest.writeString(this.origin_type);
	}

	private ECSubchat(Parcel in) {
		super(in);
		this.privacy = in.readInt();
		this.origin_id = in.readInt();
		this.name = in.readString();
		this.subchannel_id = in.readInt();
		this.description = in.readString();
		this.origin_type = in.readString();
	}

	public static final Creator<ECSubchat> CREATOR = new Creator<ECSubchat>() {
		public ECSubchat createFromParcel(Parcel source) {
			return new ECSubchat(source);
		}

		public ECSubchat[] newArray(int size) {
			return new ECSubchat[size];
		}
	};

	@Override
	public String toString() {
		return "ECSubchat{" +
				"description='" + description + '\'' +
				", name='" + name + '\'' +
				", privacy=" + privacy +
				", origin_id=" + origin_id +
				", subchannel_id=" + subchannel_id +
				", origin_type='" + origin_type + '\'' +
				'}' + super.toString();
	}
}
