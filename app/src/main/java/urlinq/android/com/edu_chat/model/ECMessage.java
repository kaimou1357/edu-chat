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

    /**
     * Leave the ints as they are in order to use it with RecyclerView Adapter.
     */
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

    public ECMessage(String id, String fileURL,String messageTitle, String author, Date messageDate, ECMessageType messageType ){
        super(id, fileURL, null);
        this.messageTitle = messageTitle;
        this.author = author;
        this.messageDate = messageDate;
        this.messageType = messageType;
        Log.v(String.format("EDU.CHAT %s", getClass().getSimpleName()), this.toString());



    }

    public static ECMessage ECMessageBuilder(JSONObject recentMessage) throws JSONException, ParseException{
        String username = recentMessage.getJSONObject("most_recent_message_creator_info").getString("firstname") + recentMessage.getJSONObject("most_recent_message_creator_info").getString("lastname");

        String message = recentMessage.getJSONObject("message_data").getString("text");
        String id = recentMessage.getJSONObject("message_data").getString("id");
        String fileURL = recentMessage.getJSONObject("most_recent_message_creator_info").getJSONObject("picture_file").getString("file_url");

        String messageT = recentMessage.getJSONObject("message_data").getString("type");
        ECMessageType messageType = null;//Just adding these two types for now.
        if (messageT.equals("file")) {
            messageType = ECMessageType.ECMessageFileType;
        } else if (messageT.equals("text")) {
            messageType = ECMessageType.ECMessageTextType;
        } else {
            messageType = ECMessageType.ECNotSupportedType;
        }
        Date messageDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        messageDate = format.parse(recentMessage.getJSONObject("message_data").getString("sent_at").replace("T", " "));
        return new ECMessage(id, fileURL, messageT, username, messageDate, messageType);

    }


    public String getMessageTitle() {
        return messageTitle;
    }

    public String getAuthor() {
        return author;
    }

//    public Date getMessageDate() {
//        return messageDate;
//    }

    public ECMessageType getMessageType() {
        return messageType;
    }
}
