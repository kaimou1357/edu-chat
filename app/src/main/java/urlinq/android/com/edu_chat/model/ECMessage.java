package urlinq.android.com.edu_chat.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by Kai on 10/23/2015.
 */
public class ECMessage extends ECObject {

	private String messageTitle;
	private String author;
	private Date messageDate;
	private ECMessageType messageType;
	private String oIdentifier;
	private String fileURL;

	public ECMessage(JSONObject recentMessage) throws JSONException {
		super(recentMessage.getJSONObject("message_data").getString("id"), recentMessage.getJSONObject("most_recent_message_creator_info").getJSONObject("picture_file").getString("file_url"));

        author = recentMessage.getJSONObject("most_recent_message_creator_info").getString("firstname") + recentMessage.getJSONObject("most_recent_message_creator_info").getString("lastname");
        String date = recentMessage.getJSONObject("message_data").getString("sent_at");
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        try{
            messageDate = format.parse(date);
        }catch(ParseException t){
            t.printStackTrace();
        }
        messageTitle = recentMessage.getJSONObject("message_data").getString("text");

        String messageT = recentMessage.getJSONObject("message_data").getString("type");
        //Just adding these two types for now.
        if(messageT.equals("file")){
            messageType = ECMessageType.ALMessageFileType;
        }
        if(messageT.equals("text")){
            messageType = ECMessageType.ALMessageTextType;
        }



	}


	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
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
