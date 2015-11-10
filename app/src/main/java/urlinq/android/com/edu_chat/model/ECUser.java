package urlinq.android.com.edu_chat.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.enums.ECUserType;


/**
 * Global to store authentication tokens, parse the String returned from POST/GET requests.
 * Created by Kai on 9/18/2015.
 */
public class ECUser extends ECObject {

    //static
    private static ECUser currentUser;
    private static String userToken;

    //dynamic
    private final String firstName;
    private final String lastName;
    private final ECUserType userType;

    // TODO: Field for profile picture

    // TODO: Use this variable
    private final ECMessage mostRecentMessage;

    // TODO: Use this variable
    private final Date lastActivity;

    // TODO: Use this variable
    private final String department;

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

    public ECUser(JSONObject data) throws JSONException {
        super(data.getJSONObject("user").getString("id"), null);
        this.firstName = data.getJSONObject("user").getString("firstname");
        this.lastName = data.getJSONObject("user").getString("lastname");
        this.userToken = data.getString("token");
        this.userType = null;
        mostRecentMessage = null;
        lastActivity = null;
        department = null;
        Log.v(String.format("EDU.CHAT %s", getClass().getSimpleName()), this.toString());
    }

    /**
     * Method that refreshes the state of the current user by calling the API again.
     */
    public static void refreshCurrentUser() {
        RequestParams params = new RequestParams();
        params.put("token", ECUser.getUserToken());
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
        return ECUser.currentUser.getObjectIdentifier();
    }

    // Dynamic

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Bitmap getProfilePicture() {
        return null;
    }


}
