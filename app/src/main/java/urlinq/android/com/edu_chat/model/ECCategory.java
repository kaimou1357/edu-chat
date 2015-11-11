package urlinq.android.com.edu_chat.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import urlinq.android.com.edu_chat.model.enums.ECCategoryType;

/**
 * Created by Jacob on 10/27/15.
 */
public class ECCategory extends ECObject {
    
    // TODO: These must be final
    private String name;
    private String professorFirstName;
    private String professorLastName;
    private String professorID;
    private String departmentTag;
    private ECMessage mostRecentMessage;

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

    public ECCategory(String id, String fileURL) {
        super(id, fileURL);
    }

    public static ArrayList<ECCategory> buildManyWithJSON(JSONArray response, ECCategoryType groupType) {
        // TODO: Make this... It'll be pretty long. Set everything from the supers too
        switch (groupType) {
            case ECDepartmentCategoryType: {
                ArrayList<ECCategory> departments = new ArrayList<ECCategory>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        String identifier = obj.getString("id");
                        String fileURL = obj.getJSONObject("picture_file").getString("file_url");
                        ECCategory department = new ECCategory(identifier, fileURL);

                        ECMessage recentMessage = null;
                        try {
                            recentMessage = new ECMessage(obj.getJSONObject("most_recent_message_info"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        department.setMostRecentMessage(recentMessage);
                        department.setName(obj.getString("department_name"));
                        Log.v(String.format("EDU.CHAT %s", ECCategory.class.getSimpleName()), department.toString());
                        departments.add(department);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            case ECClassCategoryType: {
                ArrayList<ECCategory> classes = new ArrayList<ECCategory>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        String identifier = obj.getString("id");
                        String fileURL = obj.getJSONObject("picture_file").getString("file_url");
                        ECCategory classroom = new ECCategory(identifier, fileURL);
                        classroom.setProfessorFirstName(obj.getString("class_professor_firstname"));
                        classroom.setProfessorLastName(obj.getString("class_professor_lastname"));

                        ECMessage recentMessage = null;
                        try {

                            recentMessage = new ECMessage(obj.getJSONObject("most_recent_message_info"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        classroom.setMostRecentMessage(recentMessage);
                        classroom.setName(obj.getString("class_name"));
                        Log.v(String.format("EDU.CHAT %s", ECCategory.class.getSimpleName()), classroom.toString());
                        classes.add(classroom);
                    }
                } catch (JSONException e) {

                }
                return classes;
            }
            case ECGroupCategoryType: {
                ArrayList<ECCategory> groups = new ArrayList<ECCategory>();
                try {
                    //Add each group into an ArrayList and then return the entire arraylist of category objects.
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        String identifier = obj.getString("id");
                        String fileURL = obj.getJSONObject("picture_file").getString("file_url");
                        ECCategory category = new ECCategory(identifier, fileURL);

                        ECMessage recentMessage = null;
                        try {
                            recentMessage = new ECMessage(obj.getJSONObject("most_recent_message_info"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        category.setMostRecentMessage(recentMessage);
                        category.setName(obj.getString("name"));
                        Log.v(String.format("EDU.CHAT %s", ECCategory.class.getSimpleName()), category.toString());
                        groups.add(category);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return groups;
            }

        }
        return null;
    }

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
