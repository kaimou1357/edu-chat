package urlinq.android.com.edu_chat.model;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.model.enums.ECMessageType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kai on 10/23/2015.
 */
public class ECMessage extends ECObject {

	private final String messageTitle;
	private final String author;
	private final Date messageDate;
	private final ECMessageType messageType;

	@Override
	public String toString() {
		return "ECMessage{" +
				"author='" + author + '\'' +
				", messageTitle='" + messageTitle + '\'' +
				", messageDate=" + messageDate +
				", messageType=" + messageType +
				'}' + super.toString();
	}

	public ECMessage (JSONObject recentMessage) throws JSONException, ParseException {
		super(recentMessage.getJSONObject("message_data").getString("id"), null, null);
		this.author = recentMessage.getJSONObject("most_recent_message_creator_info").getString("firstname") + " " + recentMessage.getJSONObject("most_recent_message_creator_info").getString("lastname");
		this.messageTitle= recentMessage.getJSONObject("message_data").getString("text");
		this.messageType = ECMessageType.ECMessageTextType;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.messageDate = format.parse(recentMessage.getJSONObject("message_data").getString("sent_at").replace("T", " "));
		Log.v(String.format("EDU.CHAT %s", getClass().getSimpleName()), this.toString());

	}


	public String getMessageTitle() {
		return messageTitle;
	}

	public String getAuthor() {
		return author;
	}

	public Date getMessageDate() {
		return messageDate;
	}

	public ECMessageType getMessageType() {
		return messageType;
	}
}
