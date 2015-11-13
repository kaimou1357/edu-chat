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
        super(id, fileURL);
        this.name = name;
        this.professorFirstName = professorFirstName;
        this.professorLastName = professorLastName;
        this.professorID = professorID;
        this.departmentTag = departmentTag;
        this.mostRecentMessage = mostRecentMessage;
    }



    public static ArrayList<ECCategory> buildManyWithJSON(JSONArray response, ECCategoryType groupType) {
        // TODO: Make this... It'll be pretty long. Set everything from the supers too
        switch (groupType) {
            case ECDepartmentCategoryType: {
                ArrayList<ECCategory> departments = new ArrayList<ECCategory>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        departments.add(buildOneECCategory(obj, ECCategoryType.ECDepartmentCategoryType));
                    }
                } catch (JSONException e) {}
                return departments;
            }
            case ECClassCategoryType: {
                ArrayList<ECCategory> classes = new ArrayList<ECCategory>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        classes.add(buildOneECCategory(obj, ECCategoryType.ECClassCategoryType));
                    }
                } catch (JSONException e) {}
                return classes;
            }
            case ECGroupCategoryType: {
                ArrayList<ECCategory> groups = new ArrayList<ECCategory>();
                try {
                    //Add each group into an ArrayList and then return the entire arraylist of category objects.
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        groups.add(buildOneECCategory(obj, ECCategoryType.ECGroupCategoryType));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return groups;
            }

        }
        return null;
    }

    /**
     * Helper function to help build one ECCategory in order to use with the ArrayList building later.
     * @param obj
     * @param groupType
     * @return
     * @throws JSONException
     */
    public static ECCategory buildOneECCategory(JSONObject obj, ECCategoryType groupType)throws JSONException{

        switch(groupType){
            case ECDepartmentCategoryType:{
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
            case ECClassCategoryType:{

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
            case ECGroupCategoryType:{
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
