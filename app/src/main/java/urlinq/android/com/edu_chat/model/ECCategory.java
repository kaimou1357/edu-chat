package urlinq.android.com.edu_chat.model;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.model.enums.ECCategoryType;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 10/27/15.
 */
public class ECCategory extends ECObject {

	private final String name;
	private final String professorFirstName;
	private final String professorLastName;
	private final String professorID;
	private final String departmentTag;
	private final ECMessage mostRecentMessage;

	@Override
	public String toString() {
		return "ECCategory{" +
				"departmentTag='" + departmentTag + '\'' +
				", name='" + name + '\'' +
				", professorFirstName='" + professorFirstName + '\'' +
				", professorLastName='" + professorLastName + '\'' +
				", professorID='" + professorID + '\'' +
				", mostRecentMessage=" + mostRecentMessage +
				'}' + super.toString();
	}

	public ECCategory(String id, String fileURL, String name, String professorFirstName, String professorLastName, String professorID, String departmentTag, ECMessage mostRecentMessage) {
		// TODO: Pass in a color
		super(id, fileURL, null);
		this.name = name;
		this.professorFirstName = professorFirstName;
		this.professorLastName = professorLastName;
		this.professorID = professorID;
		this.departmentTag = departmentTag;
		this.mostRecentMessage = mostRecentMessage;
		if (getColor() == null) {
			Log.e(getClass().getSimpleName(),
					String.format("CREATED A CATEGORY THAT HAS NO COLOR!!! Category = %s",
							toString()));
		}
	}


	public static List<ECObject> buildManyWithJSON(JSONArray response, ECCategoryType groupType) {
		ArrayList<ECObject> objects = new ArrayList<>();
		switch (groupType) {
			case ECDepartmentCategoryType: {
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject obj = response.getJSONObject(i);
						objects.add(buildOneECCategory(obj, ECCategoryType.ECDepartmentCategoryType));
					}
				} catch (JSONException e) {
					Log.e(ECCategory.class.getSimpleName(), e.getClass().getSimpleName());
				}
				return objects;
			}
			case ECClassCategoryType: {
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject obj = response.getJSONObject(i);
						objects.add(buildOneECCategory(obj, ECCategoryType.ECClassCategoryType));
					}
				} catch (JSONException e) {
					Log.e(ECCategory.class.getSimpleName(), e.getClass().getSimpleName());
				}
				return objects;
			}
			case ECGroupCategoryType: {
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject obj = response.getJSONObject(i);
						objects.add(buildOneECCategory(obj, ECCategoryType.ECGroupCategoryType));
					}
				} catch (JSONException e) {
					Log.e(ECCategory.class.getSimpleName(), e.getClass().getSimpleName());
				}
				return objects;
			}

		}
		return null;
	}

	// TODO: Build one is stupid. Build one is a constructor !! Fix this.

	/**
	 * Helper function to help build one ECCategory in order to use with the ArrayList building later.
	 *
	 * @param obj
	 * @param groupType
	 * @return
	 * @throws JSONException
	 */
	public static ECCategory buildOneECCategory(JSONObject obj, ECCategoryType groupType) throws JSONException {

		switch (groupType) {
			case ECDepartmentCategoryType: {
				String identifier = obj.getString("id");
				String fileURL = obj.getJSONObject("picture_file").getString("file_url");

				String name = obj.getString("department_name");
				//There are no specific professors in a department.
				String professorFirstName = null;
				String professorLastName = null;
				String professorID = null;
				String departmentTag = obj.getString("department_tag");
				ECMessage recentMessage = null;
				try {
					recentMessage = ECMessage.ECMessageBuilder(obj.getJSONObject("most_recent_message_info"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return new ECCategory(identifier, fileURL, name, professorFirstName, professorLastName, professorID, departmentTag, recentMessage);
			}
			case ECClassCategoryType: {

				String identifier = obj.getString("id");
				String fileURL = obj.getJSONObject("picture_file").getString("file_url");
				String name = obj.getString("class_name");
				//There are no specific professors in a department.
				String professorFirstName = obj.getString("class_professor_firstname");
				String professorLastName = obj.getString("class_professor_lastname");
				//The professor is the creator in the class type.
				String professorID = obj.getString("creator_id");
				String departmentTag = obj.getString("parent_department_name");
				ECMessage recentMessage = null;
				try {
					recentMessage = ECMessage.ECMessageBuilder(obj.getJSONObject("most_recent_message_info"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return new ECCategory(identifier, fileURL, name, professorFirstName, professorLastName, professorID, departmentTag, recentMessage);
			}
			case ECGroupCategoryType: {
				String identifier = obj.getString("id");
				String fileURL = obj.getJSONObject("picture_file").getString("file_url");
				String name = obj.getString("group_name");
				//There are no specific professors in a department.
				String professorFirstName = null;
				String professorLastName = null;
				String professorID = null;
				String departmentTag = null;
				ECMessage recentMessage = null;
				try {
					recentMessage = ECMessage.ECMessageBuilder(obj.getJSONObject("most_recent_message_info"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return new ECCategory(identifier, fileURL, name, professorFirstName, professorLastName, professorID, departmentTag, recentMessage);

			}

		}
		return null;


	}


	public String getName() {
		return name;
	}

	public String getProfessorFirstName() {
		return professorFirstName;
	}

	public String getProfessorLastName() {
		return professorLastName;
	}

	public String getProfessorID() {
		return professorID;
	}

	public String getDepartmentTag() {
		return departmentTag;
	}

	public ECMessage getMostRecentMessage() {
		return mostRecentMessage;
	}
}
