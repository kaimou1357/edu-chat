package urlinq.android.com.edu_chat.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kai on 10/23/2015.
 */
public class ECMessage extends ECObject {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;
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

    public ECMessage(JSONObject recentMessage) throws JSONException, ParseException {
        super(recentMessage.getJSONObject("message_data").getString("id"), recentMessage.getJSONObject("most_recent_message_creator_info").getJSONObject("picture_file").getString("file_url"));


        author = recentMessage.getJSONObject("most_recent_message_creator_info").getString("firstname") + recentMessage.getJSONObject("most_recent_message_creator_info").getString("lastname");
        String date = recentMessage.getJSONObject("message_data").getString("sent_at");
        SimpleDateFormat format = new SimpleDateFormat("MM dd, yyyy", Locale.ENGLISH);
        messageDate = format.parse(date);
        messageTitle = recentMessage.getJSONObject("message_data").getString("text");

        String messageT = recentMessage.getJSONObject("message_data").getString("type");
        //Just adding these two types for now.
        if (messageT.equals("file")) {
            messageType = ECMessageType.ALMessageFileType;
        } else if (messageT.equals("text")) {
            messageType = ECMessageType.ALMessageTextType;
        } else {
            messageType = ECMessageType.ALNotSupportedType;
        }
        Log.v(String.format("EDU.CHAT %s", getClass().getSimpleName()), this.toString());
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
