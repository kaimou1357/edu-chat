package urlinq.android.com.edu_chat.model.enums;

import urlinq.android.com.edu_chat.model.ECCategory;

/**
 * Created by Jacob on 10/29/15.
 */
public enum ECCategoryType {
	ECDepartmentCategoryType("Department"),
	ECClassCategoryType("Class"),
	ECGroupCategoryType("Group");

    private final String userType;


    ECCategoryType(final String userType){this.userType = userType;}

    public String getUserType(){return userType;}

}
