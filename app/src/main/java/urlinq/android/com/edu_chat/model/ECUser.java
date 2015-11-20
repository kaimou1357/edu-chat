package urlinq.android.com.edu_chat.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.constants.Constants;
import urlinq.android.com.edu_chat.model.enums.ECUserType;


/**
 * Global to store authentication tokens, parse the String returned from POST/GET requests.
 * Created by Kai on 9/18/2015.
 */
public class ECUser extends ECObject {

	private static ECUser currentUser;
	private static String userToken;

	private final String firstName;
	private final String lastName;
	private final String email = null;
	private final ECUserType userType;
	private final ECMessage mostRecentMessage;
	private final Date lastActivity;
	private final String department;
	private final String[] subchannel = null;
	private Bitmap profilePicture;


	@Override
	public String toString () {
		return "ECUser{" +
				"department='" + department + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", userType=" + userType +
				", mostRecentMessage=" + mostRecentMessage +
				", lastActivity=" + lastActivity +
				", subchannel=" + Arrays.toString(subchannel) +
				", profilePicture=" + profilePicture +
				'}' + super.toString();
	}

	/**
	 * This constructor will be for the current user using the application.
	 *
	 * @param data
	 * @throws JSONException
	 */
	public ECUser (JSONObject data) throws JSONException, ParseException {
		super(data.getString("id"), data.getJSONObject("picture_file").getString("file_url"), null);
		this.firstName = data.getString("firstname");
		this.lastName = data.getString("lastname");
		//UserType needs to be fixed later. Keep it at student for now.
		this.userType = ECUserType.ECUserTypeStudent;
		ECMessage mostRecentMessage1;
		try {
			mostRecentMessage1 = new ECMessage(data.getJSONObject("most_recent_message_info").getJSONObject("message_data"));
		} catch (JSONException e) {
			mostRecentMessage1 = null;
		}
		this.mostRecentMessage = mostRecentMessage1;
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
	public static void refreshCurrentUser () {
		RequestParams params = new RequestParams();
		params.put("token", ECUser.getUserToken());
		params.put("id", ECUser.getCurrentUser().getObjectIdentifier());
		ECApiManager.get(Constants.refreshUserAPI, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess (int statusCode, Header[] headers, byte[] responseBody) {
				String response = new String(responseBody);
				Log.d("refreshUser", response);
				try {
					JSONObject obj = new JSONObject(response);
					ECUser.setCurrentUser(new ECUser(obj));
				} catch (JSONException | ParseException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure (int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}

	/**
	 * This method will build an ArrayList of ECUsers to process in the Main RecyclerView List.
	 *
	 * @param response people object from the loadout response.
	 */
	public static List<ECObject> buildManyWithJSON (JSONArray response) {

		ArrayList<ECObject> personList = new ArrayList<>();
		try {
			for (int i = 0; i < response.length(); i++) {
				JSONObject obj = response.getJSONObject(i);
				personList.add(new ECUser(obj));
			}
		} catch (JSONException | ParseException e) {
			e.printStackTrace();
		}
		return personList;
	}

	private void getProfilePicture (String fileURL) {

		ECApiManager.get(fileURL, null, new BinaryHttpResponseHandler() {
			Bitmap image;

			@Override
			public void onSuccess (int statusCode, Header[] headers, byte[] responseBody) {
				image = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
			}

			@Override
			public void onFailure (int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}

			@Override
			public void onFinish () {
				setProfilePicture(image);
			}
		});

	}

	private void setProfilePicture (Bitmap img) {
		this.profilePicture = img;
	}

	// Static
	public static void setCurrentUser (ECUser user) {
		ECUser.currentUser = user;

	}

	public static String getUserToken () {
		return userToken;
	}

	public static void setUserToken (String userToken) {
		ECUser.userToken = userToken;
	}

	public static ECUser getCurrentUser () {
		return ECUser.currentUser;
	}


	// Dynamic

	public ECMessage getMostRecentMessage () {
		return this.mostRecentMessage;
	}

	public Date getLastActivity () {
		return this.lastActivity;
	}

	public String getDepartment () {
		return this.department;
	}

	public ECUserType getUserType () {
		return this.userType;
	}

	public String getLastName () {
		return this.lastName;
	}
    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

	public String getFirstName () {
		return this.firstName;
	}

	public Bitmap getProfilePicture () {
		return this.profilePicture;
	}


}
