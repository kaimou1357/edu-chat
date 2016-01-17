package urlinq.android.com.edu_chat.model;

import android.os.Parcel;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.model.enums.ECMessageType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by Kai on 10/23/2015.
 */
public class ECMessage extends ECObject {

	private final String messageTitle;
	private final Date messageDate;
	private final ECUser author;
	private final ECMessageType messageType;
	private final String subchannelName = null;

	public ECMessage(JSONObject recentMessage) throws JSONException, ParseException {
		super(recentMessage.getInt("id"), null, null);
		this.author = new ECUser(recentMessage.getJSONObject("user"));
		this.messageTitle = recentMessage.getString("text");
		this.messageType = ECMessageType.ECMessageTextType;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		this.messageDate = format.parse(recentMessage.getString("sent_at").replace("T", " "));
		Log.v(String.format("EDU.CHAT %s", getClass().getSimpleName()), this.toString());

	}
	public String getMessageTitle() {
		return messageTitle;
	}

	public ECUser getAuthor() {
		return author;
	}

	public Date getMessageDate() {
		return messageDate;
	}

	public ECMessageType getMessageType() {
		return messageType;
	}

	/**
	 * Handle Parcelable Stuff under here.
	 * @return
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.messageTitle);
		dest.writeLong(messageDate != null ? messageDate.getTime() : -1);
		dest.writeParcelable(this.author, 0);
		dest.writeInt(this.messageType == null ? -1 : this.messageType.ordinal());
	}

	ECMessage(Parcel in) {
		super(in);
		this.messageTitle = in.readString();
		long tmpMessageDate = in.readLong();
		this.messageDate = tmpMessageDate == -1 ? null : new Date(tmpMessageDate);
		this.author = in.readParcelable(ECUser.class.getClassLoader());
		int tmpMessageType = in.readInt();
		this.messageType = tmpMessageType == -1 ? null : ECMessageType.values()[tmpMessageType];
	}

	public static final Creator<ECMessage> CREATOR = new Creator<ECMessage>() {
		public ECMessage createFromParcel(Parcel source) {
			return new ECMessage(source);
		}

		public ECMessage[] newArray(int size) {
			return new ECMessage[size];
		}
	};

	@Override
	public String toString() {
		int subchannelID = 0;
		return "ECMessage{" +
				"author='" + author + '\'' +
				", messageTitle='" + messageTitle + '\'' +
				", messageDate=" + messageDate +
				", messageType=" + messageType +
				", subchannelID=" + subchannelID +
				", subchannelName='" + subchannelName + '\'' +
				'}' + super.toString();
	}
}
