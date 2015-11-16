package urlinq.android.com.edu_chat.model.enums;

/**
 * Created by Jacob on 10/29/15.
 */
public enum ECUserType {
	ECUserTypeStudent("User"),
	ECUserTypeProfessor("User");

	private final String userType;


	ECUserType(final String userType){this.userType = userType;}

	public String getUserType(){return userType;}
}
