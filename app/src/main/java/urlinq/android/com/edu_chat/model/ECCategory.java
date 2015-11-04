package urlinq.android.com.edu_chat.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

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

	public ECCategory(String id, String fileURL) {
		// TODO: Replace null with Object identifier
		super(identifier, fileURL);
	}

	public static ArrayList<ECCategory> buildManyWithJSON(JSONArray response, ECCategoryType groupType) {
		// TODO: Make this... It'll be pretty long. Set everything from the supers too
		switch (groupType) {
			case ECDepartmentCategoryType:
			{
				ArrayList<ECCategory> departments = new ArrayList<ECCategory>();
				try{
					for(int i = 0; i<response.length(); i++){
						JSONObject obj = response.getJSONObject(i);

                        String identifier = obj.getString("id");
                        String fileURL = obj.getJSONObject("picture_file").getString("file_url");
                        ECCategory department = new ECCategory(identifier, fileURL);

                        ECMessage recentMessage = new ECMessage(obj.getJSONObject("most_recent_message_info"));

                        department.setMostRecentMessage(recentMessage);
                        department.setName(obj.getString("department_name"));
                        departments.add(department);

                    }

				}catch(JSONException e){
					e.printStackTrace();
				}
			}
				break;
			case ECClassCategoryType:
				break;
			case ECGroupCategoryType:
			{
				ArrayList<ECCategory> groups = new ArrayList<ECCategory>();
				try{

					//Add each group into an ArrayList and then return the entire arraylist of category objects.
					for(int i = 0; i<response.length(); i++){
						JSONObject obj = response.getJSONObject(i);

						String identifier = obj.getString("id");
						String fileURL = obj.getJSONObject("picture_file").getString("file_url");
						ECCategory category = new ECCategory(identifier, fileURL);

						ECMessage recentMessage = new ECMessage(obj.getJSONObject("most_recent_message_info"));

						category.setMostRecentMessage(recentMessage);
						category.setName(obj.getString("name"));
						groups.add(category);
					}
				}catch(JSONException e){e.printStackTrace();}
				return groups;
			}

		}
		return null;
	}
}
