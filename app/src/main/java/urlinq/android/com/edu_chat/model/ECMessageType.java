package urlinq.android.com.edu_chat.model;

/**
 * Created by kaimou on 11/3/15.
 */
public enum ECMessageType {
    ECMessageTextType(1),
    ECMessageFileType(2),
    ECMessageEventType(3),
    ECTypingType(4),
    ECCommentType(5),
    ECNotSupportedType(6),
    ECSubChannelType(7);

    private final int value;
    ECMessageType(final int newValue){
        value = newValue;
    }
    public int getValue(){return value;}



}
