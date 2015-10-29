package urlinq.android.com.edu_chat.model;

import org.json.JSONObject;

/**
 * Created by Jacob on 10/27/15.
 */
public class ECCategory extends ECObject {

	private String name;
	private String professorFirstName;
	private String professorLastName;
	private String professorID;
	private String departmentTag;

	private ECMessage mostRecentMessage;

	public ECCategory() {
		// TODO: Replace null with Object identifier
		super(null, fileURL);
	}

	public ECCategory buildWithJSON(JSONObject response, ECCategoryType groupType) {
		// TODO: Make this... It'll be pretty long. Set everything from the supers too
		switch (groupType) {
			case ECDepartmentCategoryType:
				break;
			case ECClassCategoryType:
				break;
			case ECGroupCategoryType:
				break;
		}
		return null;
	}
}
