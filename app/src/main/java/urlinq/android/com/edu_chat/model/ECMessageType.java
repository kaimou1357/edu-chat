package urlinq.android.com.edu_chat.model;

/**
 * Created by kaimou on 11/3/15.
 */
public enum ECMessageType {
    ALMessageTextType(0),
    ALMessageFileType(1),
    ALMessageEventType(2),
    AlTypingType(3),
    ALCommentType(4),
    ALNotSupportedType(5);

    private final int value;
    ECMessageType(final int newValue){
        value = newValue;
    }
    public int getValue(){return value;}



}
