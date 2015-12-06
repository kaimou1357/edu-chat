package urlinq.android.com.edu_chat.model.enums;

/**
 * Created by Jacob on 10/29/15.
 */
public enum ECCategoryType {
	ECDepartmentCategoryType("department"),
	ECClassCategoryType("class"),
	ECGroupCategoryType("group");

    private final String userType;


    ECCategoryType(final String userType){this.userType = userType;}

    public String getCategoryString(){return userType;}

}
