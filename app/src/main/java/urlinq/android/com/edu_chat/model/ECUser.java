package urlinq.android.com.edu_chat.model;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.Constants;
import urlinq.android.com.edu_chat.manager.ECApiManager;


/**
 * Global to store authentication tokens, parse the String returned from POST/GET requests.
 * Created by Kai on 9/18/2015.
 */
public class ECUser {


    private static ECUser currentUser;
    private static String userToken;
    private String firstName;
    private String lastName;
    private static String userID;
    private JSONObject jObject;
    private boolean loginSuccess;


    public ECUser(JSONObject data) throws JSONException {
        this.jObject = data;
        try {
            this.loginSuccess = Boolean.parseBoolean(this.jObject.getString("success"));
            if (this.loginSuccess) {
                this.userToken = this.jObject.getString("token");
                this.firstName = this.jObject.getJSONObject("user").getString("firstname");
                this.lastName = this.jObject.getJSONObject("user").getString("lastname");
                this.userID = this.jObject.getJSONObject("user").getString("id");
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
        params.put("token", userToken);
        //Gotta put in user ID too.
        params.put("user_id", userID);
        // TODO: This should only call https://edu.chat/api/user, @JACOB
        ECApiManager.get(Constants.refreshUserAPI, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {

                try {
                    ECUser.setCurrentUser(new ECUser(responseBody));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    // Static

    public static ECUser getCurrentUser() {
        return ECUser.currentUser;
    }

    public static void setCurrentUser(ECUser user) {ECUser.currentUser = user;}

    public static String getUserToken() {
        return ECUser.userToken;
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


}
