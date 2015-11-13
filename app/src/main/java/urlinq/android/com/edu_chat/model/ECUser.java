package urlinq.android.com.edu_chat.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import urlinq.android.com.edu_chat.controller.MainActivity;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.enums.ECUserType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Global to store authentication tokens, parse the String returned from POST/GET requests.
 * Created by Kai on 9/18/2015.
 */
public class ECUser extends ECObject {

	private static ECUser currentUser;
	private static String userToken;

	private static String currUserSchool;
	private static String currUserID;
	private static String currUserFirstName;
	private static String currUserLastName;

	private final String firstName;
	private final String lastName;
	private final ECUserType userType;
	private final ECMessage mostRecentMessage;
	private final Date lastActivity;
	private final String department;
	private Bitmap profilePicture;
	// TODO: Field for profile picture


	@Override
	public String toString() {
		return "ECUser{" +
				"department='" + department + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", userType=" + userType +
				", mostRecentMessage=" + mostRecentMessage +
				", lastActivity=" + lastActivity +
				'}' + super.toString();
	}



	/**
	 * This constructor will be for the current user using the application.
	 *
	 * @param data
	 * @throws JSONException
	 */
	public ECUser(JSONObject data) throws JSONException, ParseException {
		super(data.getString("id"), data.getJSONObject("picture_file").getString("file_url"), null);

		this.firstName = data.getString("firstname");

		this.lastName = data.getString("lastname");
		//UserType needs to be fixed later. Keep it at student for now.

		this.userType = ECUserType.ECUserTypeStudent;
		try{
			this.mostRecentMessage = ECMessage.ECMessageBuilder(data.getJSONObject("most_recent_message_info"));
		}catch(JSONException e){
			this.mostRecentMessage = null;
		}


		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.lastActivity = format.parse(data.getString("last_activity").replace("T", " "));
		this.department = data.getString("department");
		String fileURL = Constants.bitmapURL + super.getFileURL();
		getProfilePicture(fileURL);


		Log.v(String.format("EDU.CHAT %s", getClass().getSimpleName()), this.toString());
		Log.d("File URL Confirm", fileURL);
	}
	/**
	 * Method that refreshes the state of the current user by calling the API again.
	 */
	public static void refreshCurrentUser() {
		RequestParams params = new RequestParams();
		params.put("token", ECUser.getUserToken());
		params.put("id", ECUser.getCurrUserID());
		ECApiManager.get(Constants.refreshUserAPI, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String response = new String(responseBody);
				Log.d("refreshUser", response);
				try {
					JSONObject obj = new JSONObject(response);
					ECUser.setCurrentUser(new ECUser(obj));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}

	/**
	 * This method will build an ArrayList of ECUsers to process in the Main RecyclerView List.
	 *
	 * @param response people object from the loadout response.
	 */
	public static List<ECObject> buildManyWithJSON(JSONArray response) {

		ArrayList<ECObject> personList = new ArrayList<>();
		try {
			for (int i = 0; i < response.length(); i++) {
				JSONObject obj = response.getJSONObject(i);
				personList.add(new ECUser(obj));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}catch(ParseException e){
			e.printStackTrace();
		}
		return personList;
	}

	private void getProfilePicture(String fileURL){

		ECApiManager.get(fileURL, null, new BinaryHttpResponseHandler() {
			Bitmap image;
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				image = BitmapFactory.decodeByteArray(responseBody, 0 , responseBody.length);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
			@Override
			public void onFinish(){
				setProfilePicture(image);
			}
		});

	}

	private void setProfilePicture(Bitmap img){
		this.profilePicture = img;
	}




	// Static
	public static void setCurrentUser(ECUser user){
		ECUser.currentUser = user;

	}

	public static String getUserToken() {
		return userToken;
	}

	public static void setUserToken(String userToken) {
		ECUser.userToken = userToken;
	}

	public static ECUser getCurrentUser() {
		return ECUser.currentUser;
	}

	public static String getCurrUserSchool() {
		return currUserSchool;
	}

	public static void setCurrUserSchool(String currUserSchool) {
		ECUser.currUserSchool = currUserSchool;
	}

	public static String getCurrUserID() {
		return currUserID;
	}

	public static void setCurrUserID(String currUserID) {
		ECUser.currUserID = currUserID;
	}

	public static String getCurrUserFirstName() {
		return currUserFirstName;
	}

	public static void setCurrUserFirstName(String currUserFirstName) {
		ECUser.currUserFirstName = currUserFirstName;
	}

	public static String getCurrUserLastName() {
		return currUserLastName;
	}

	public static void setCurrUserLastName(String currUserLastName) {
		ECUser.currUserLastName = currUserLastName;
	}


	// Dynamic

	public ECMessage getMostRecentMessage() {
		return this.mostRecentMessage;
	}

	public Date getLastActivity() {
		return this.lastActivity;
	}

	public String getDepartment() {
		return this.department;
	}

	public ECUserType getUserType() {
		return this.userType;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public Bitmap getProfilePicture() {return this.profilePicture;
	}



}
