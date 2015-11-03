package urlinq.android.com.edu_chat.model;

import java.util.Date;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by Kai on 10/23/2015.
 */
public class ECMessage extends ECObject {

	private String messageTitle;
	private ECUser author;
	private Date messageDate;
	private ECMessageType messageType;


	public ECMessage(JSONObject messageJSON) {

		// TODO: don't pass in null IMPLEMENT THE CONSTRUCTOR with JSON argument.
		super(null, null);
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public ECUser getAuthor() {
		return author;
	}

	public void setAuthor(ECUser author) {
		this.author = author;
	}

	public Date getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	public ECMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(ECMessageType messageType) {
		this.messageType = messageType;
	}
}
