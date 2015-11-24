package urlinq.android.com.edu_chat.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import urlinq.android.com.edu_chat.model.enums.ECCategoryType;


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
	private final ECCategoryType typeOfCategory;
	private final String[] subchannels = null;

	@Override
	public String toString () {
		return "ECCategory{" +
				"departmentTag='" + departmentTag + '\'' +
				", name='" + name + '\'' +
				", professorFirstName='" + professorFirstName + '\'' +
				", professorLastName='" + professorLastName + '\'' +
				", professorID='" + professorID + '\'' +
				", mostRecentMessage=" + mostRecentMessage +
				", typeOfCategory=" + typeOfCategory +
				", subchannels=" + Arrays.toString(subchannels) +
				'}' + super.toString();
	}

	public ECCategory (JSONObject obj, ECCategoryType groupType) throws JSONException, ParseException {
		super(obj.getInt("id"), obj.getJSONObject("picture_file").getString("file_url"), obj.getString("color"));
		switch (groupType) {
			case ECDepartmentCategoryType: {
				this.typeOfCategory = ECCategoryType.ECDepartmentCategoryType;
				this.name = obj.getString("department_name");
				//There are no specific professors in a department.
				this.professorFirstName = null;
				this.professorLastName = null;
				this.professorID = null;
				this.departmentTag = obj.getString("department_tag");

				this.mostRecentMessage = new ECMessage(obj.getJSONObject("most_recent_message_info").getJSONObject("message_data"));
				if (getColor() == null) {
					Log.e(getClass().getSimpleName(),
							String.format("CREATED A CATEGORY THAT HAS NO COLOR!!! Category = %s",
									toString()));
				}

			}
			break;
			case ECClassCategoryType: {
				this.typeOfCategory = ECCategoryType.ECClassCategoryType;
				this.name = obj.getString("class_name");
				//There are no specific professors in a department.
				this.professorFirstName = obj.getString("class_professor_firstname");
				this.professorLastName = obj.getString("class_professor_lastname");
				//The professor is the creator in the class type.
				this.professorID = obj.getString("creator_id");
				this.departmentTag = obj.getString("parent_department_name");

				this.mostRecentMessage = new ECMessage(obj.getJSONObject("most_recent_message_info").getJSONObject("message_data"));
				if (getColor() == null) {
					Log.e(getClass().getSimpleName(),
							String.format("CREATED A CATEGORY THAT HAS NO COLOR!!! Category = %s",
									toString()));
				}

			}
			break;
			case ECGroupCategoryType: {
				this.typeOfCategory = ECCategoryType.ECGroupCategoryType;
				this.name = obj.getString("group_name");
				//There are no specific professors in a department.
				this.professorFirstName = null;
				this.professorLastName = null;
				this.professorID = null;
				this.departmentTag = null;

				mostRecentMessage = new ECMessage(obj.getJSONObject("most_recent_message_info").getJSONObject("message_data"));
				if (getColor() == null) {
					Log.e(getClass().getSimpleName(),
							String.format("CREATED A CATEGORY THAT HAS NO COLOR!!! Category = %s",
									toString()));
				}
			}
			break;
			//Should never reach the default case.
			default: {
				this.typeOfCategory = null;
				this.name = null;
				//There are no specific professors in a department.
				this.professorFirstName = null;
				this.professorLastName = null;
				this.professorID = null;
				this.departmentTag = null;
				this.mostRecentMessage = null;
			}

		}
		//It should never reach this point....

	}

	public static List<ECObject> buildManyWithJSON (JSONArray response, ECCategoryType groupType) {
		ArrayList<ECObject> objects = new ArrayList<>();
		switch (groupType) {
			case ECDepartmentCategoryType: {
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject obj = response.getJSONObject(i);
						objects.add(new ECCategory(obj, ECCategoryType.ECDepartmentCategoryType));
					}
				} catch (JSONException e) {
					Log.e(ECCategory.class.getSimpleName(), e.getClass().getSimpleName());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return objects;
			}
			case ECClassCategoryType: {
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject obj = response.getJSONObject(i);
						objects.add(new ECCategory(obj, ECCategoryType.ECClassCategoryType));
					}
				} catch (JSONException e) {
					Log.e(ECCategory.class.getSimpleName(), e.getClass().getSimpleName());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return objects;
			}
			case ECGroupCategoryType: {
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject obj = response.getJSONObject(i);
						objects.add(new ECCategory(obj, ECCategoryType.ECGroupCategoryType));
					}
				} catch (JSONException e) {
					Log.e(ECCategory.class.getSimpleName(), e.getClass().getSimpleName());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return objects;
			}

		}
		return null;
	}

	public String getName () {
		return name;
	}

	public String getProfessorFirstName () {
		return professorFirstName;
	}

	public String getProfessorLastName () {
		return professorLastName;
	}

	public String getProfessorID () {
		return professorID;
	}

	public String getDepartmentTag () {
		return departmentTag;
	}

	public ECCategoryType getTypeOfCategory () {
		return typeOfCategory;
	}

	public ECMessage getMostRecentMessage () {
		return mostRecentMessage;
	}
}
