package urlinq.android.com.edu_chat.model;

import android.os.Parcel;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.model.enums.ECCategoryType;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private final ECCategoryType typeOfCategory;
	private final ArrayList<ECSubchat> subchannels;


	@Override
	public String toString() {
		String subchatText;
		if(subchannels == null){
			subchatText = null;
		}
		else{
			subchatText = subchannels.toString();
		}
		return "ECCategory{" +
				"departmentTag='" + departmentTag + '\'' +
				", name='" + name + '\'' +
				", professorFirstName='" + professorFirstName + '\'' +
				", professorLastName='" + professorLastName + '\'' +
				", professorID='" + professorID + '\'' +
				", mostRecentMessage=" + mostRecentMessage +
				", typeOfCategory=" + typeOfCategory +
				", # subchannels=" + subchatText +
				'}' + super.toString();
	}

	public ECCategory(JSONObject obj, ECCategoryType groupType) throws JSONException, ParseException {
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

				JSONArray subchatJson = obj.getJSONArray("subchannels");
				subchannels = new ArrayList<ECSubchat>();
				//loop through and generate subchannels.
				if(subchatJson!=null){
					for(int i = 0; i<subchatJson.length(); i++){
						subchannels.add(new ECSubchat(subchatJson.getJSONObject(i)));
					}
					Log.d("subchats", "# of subchats " + subchannels.size());
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
				JSONArray subchatJson = obj.getJSONArray("subchannels");
				subchannels = new ArrayList<ECSubchat>();
				//loop through and generate subchannels.
				if(subchatJson!=null){
					for(int i = 0; i<subchatJson.length(); i++){
						subchannels.add(new ECSubchat(subchatJson.getJSONObject(i)));
					}
					Log.d("subchats", "# of subchats " + subchannels.size());
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
				JSONArray subchatJson = obj.getJSONArray("subchannels");
				subchannels = new ArrayList<ECSubchat>();
				//loop through and generate subchannels.
				if(subchatJson!=null){
					for(int i = 0; i<subchatJson.length(); i++){
						subchannels.add(new ECSubchat(subchatJson.getJSONObject(i)));
					}
					Log.d("subchats", "# of subchats " + subchannels.size());
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
				this.subchannels = null;
			}

		}
		//It should never reach this point....

	}

	public static List<ECObject> buildManyWithJSON(JSONArray response, ECCategoryType groupType) {
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

	public ECCategoryType getTypeOfCategory() {
		return typeOfCategory;
	}

	public ECMessage getMostRecentMessage() {
		return mostRecentMessage;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.name);
		dest.writeString(this.professorFirstName);
		dest.writeString(this.professorLastName);
		dest.writeString(this.professorID);
		dest.writeString(this.departmentTag);
		dest.writeParcelable(this.mostRecentMessage, 0);
		dest.writeInt(this.typeOfCategory == null ? -1 : this.typeOfCategory.ordinal());
		dest.writeTypedList(this.subchannels);
	}

	protected ECCategory(Parcel in) {
		super(in);
		this.name = in.readString();
		this.professorFirstName = in.readString();
		this.professorLastName = in.readString();
		this.professorID = in.readString();
		this.departmentTag = in.readString();
		this.mostRecentMessage = in.readParcelable(ECMessage.class.getClassLoader());
		int tmpTypeOfCategory = in.readInt();
		this.typeOfCategory = tmpTypeOfCategory == -1 ? null : ECCategoryType.values()[tmpTypeOfCategory];
		this.subchannels = in.createTypedArrayList(ECSubchat.CREATOR);
	}

	public static final Creator<ECCategory> CREATOR = new Creator<ECCategory>() {
		public ECCategory createFromParcel(Parcel source) {
			return new ECCategory(source);
		}

		public ECCategory[] newArray(int size) {
			return new ECCategory[size];
		}
	};
}
