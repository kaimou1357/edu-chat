package urlinq.android.com.edu_chat.model;

import org.json.JSONArray;
import org.json.JSONException;
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
	private String groupName;
	private ECMessage mostRecentMessage;

	public void setName(String name) {
		this.name = name;
	}

	public void setProfessorFirstName(String professorFirstName) {
		this.professorFirstName = professorFirstName;
	}

	public void setProfessorLastName(String professorLastName) {
		this.professorLastName = professorLastName;
	}

	public void setProfessorID(String professorID) {
		this.professorID = professorID;
	}

	public void setDepartmentTag(String departmentTag) {
		this.departmentTag = departmentTag;
	}

	public void setMostRecentMessage(ECMessage mostRecentMessage) {
		this.mostRecentMessage = mostRecentMessage;
	}

	private static String identifier;
	private static String fileURL;

	public ECCategory() {
		// TODO: Replace null with Object identifier
		super(identifier, fileURL);
	}

	public static ECCategory buildWithJSON(JSONArray response, ECCategoryType groupType) {
		// TODO: Make this... It'll be pretty long. Set everything from the supers too
		switch (groupType) {
			case ECDepartmentCategoryType:
			{
			}
				break;
			case ECClassCategoryType:
				break;
			case ECGroupCategoryType:
			{
				ECCategory group = new ECCategory();
				try{
					identifier = response.getJSONObject(13).toString();
					fileURL = response.getJSONObject(12).getString("file_url");
					group.setName(response.getJSONObject(0).toString());

				}catch(JSONException e){}

				return group;

			}

		}
		return null;
	}
}
