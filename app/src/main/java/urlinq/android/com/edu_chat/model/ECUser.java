package urlinq.android.com.edu_chat.model;

import android.graphics.Bitmap;
import android.util.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.Constants;
import urlinq.android.com.edu_chat.manager.ECApiManager;

import java.util.Date;


/**
 * Global to store authentication tokens, parse the String returned from POST/GET requests.
 * Created by Kai on 9/18/2015.
 */
public class ECUser extends ECObject {

	//static
	private static ECUser currentUser;
	private static String userToken;
	private static String userID;

	//dynamic
	private String firstName;
	private String lastName;
	private String fullName;
	private boolean loginSuccess;

	private ECUserType userType;

	// TODO: Field for profile picture

	// TODO: Use this variable
	private ECMessage mostRecentMessage;

	// TODO: Use this variable
	private Date lastActivity;

	// TODO: Use this variable
	private String department;


	public ECUser(JSONObject data) throws JSONException {
		// TODO: Replace null with the actual object identifier
		super(null, null);
		try {
			this.loginSuccess = Boolean.parseBoolean(data.getString("success"));
			if (this.loginSuccess) {
				ECUser.userToken = data.getString("token");
				this.firstName = data.getJSONObject("user").getString("firstname");
				this.lastName = data.getJSONObject("user").getString("lastname");
				ECUser.userID = data.getJSONObject("user").getString("id");
			}


		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that refreshes the state of the current user by calling the API again.
	 */
	public static void refreshCurrentUser() {
		RequestParams params = new RequestParams();
		params.put("token", ECUser.getUserToken());
		//Gotta put in user ID too.
		params.put("id", ECUser.getUserID());
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
				}


			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}

	// Static

	public static ECUser getCurrentUser() {
		return ECUser.currentUser;
	}

	public static void setCurrentUser(ECUser user) {
		ECUser.currentUser = user;
	}

	public static String getUserToken() {
		return ECUser.userToken;
	}

	public static String getUserID() {
		return ECUser.userID;
	}

	// Dynamic

	public String getLastName() {
		return this.lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public boolean getLoginSuccessful() {
		return this.loginSuccess;
	}

	public Bitmap getProfilePicture() {
		return null;
	}


}
