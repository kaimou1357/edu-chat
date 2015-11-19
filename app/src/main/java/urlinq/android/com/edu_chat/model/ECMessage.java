package urlinq.android.com.edu_chat.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import urlinq.android.com.edu_chat.model.enums.ECMessageType;


/**
 * Created by Kai on 10/23/2015.
 */
public class ECMessage extends ECObject {

	private final String messageTitle;
	private final Date messageDate;
	private final String author;
	private final ECMessageType messageType;
	private final int subchannelID = 0;
	private final String subchannelName = null;

	@Override
	public String toString () {
		return "ECMessage{" +
				"author='" + author + '\'' +
				", messageTitle='" + messageTitle + '\'' +
				", messageDate=" + messageDate +
				", messageType=" + messageType +
				'}' + super.toString();
	}

	public ECMessage (JSONObject recentMessage) throws JSONException, ParseException {
		super(recentMessage.getString("object_id"), null, null);
		this.author = recentMessage.getJSONObject("user").getString("name");
		this.messageTitle = recentMessage.getString("text");
		this.messageType = ECMessageType.ECMessageTextType;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.messageDate = format.parse(recentMessage.getString("sent_at").replace("T", " "));
		Log.v(String.format("EDU.CHAT %s", getClass().getSimpleName()), this.toString());

	}


	public String getMessageTitle () {
		return messageTitle;
	}

	public String getAuthor () {
		return author;
	}

	public Date getMessageDate () {
		return messageDate;
	}

	public ECMessageType getMessageType () {
		return messageType;
	}
}
