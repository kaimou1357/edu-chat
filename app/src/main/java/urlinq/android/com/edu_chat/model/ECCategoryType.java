package urlinq.android.com.edu_chat.model;

/**
 * Created by Jacob on 10/29/15.
 */
public enum ECCategoryType {
	ECDepartmentCategoryType(0),
	ECClassCategoryType(1),
	ECGroupCategoryType(2);

    private final int value;

    ECCategoryType(final int newValue){
        value = newValue;
    }

    public int getValue(){return value;}

}
