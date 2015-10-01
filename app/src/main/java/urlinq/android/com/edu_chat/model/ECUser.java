package urlinq.android.com.edu_chat.model;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Global to store authentication tokens, parse the String returned from POST/GET requests.
 * Created by Kai on 9/18/2015.
 */
public class ECUser {

    private static ECUser currentUser;
    private static String userToken;

    private String firstName;
    private String lastName;
    private String userID;
    private JSONObject jObject;
    private boolean loginSuccess;


    public ECUser(JSONObject data) throws JSONException {
        this.jObject = data;
        try {
            this.loginSuccess = Boolean.parseBoolean(jObject.getString("success"));
            this.userToken = this.jObject.getString("token");
            this.firstName = this.jObject.getJSONObject("user").getString("firstname");
            this.lastName = this.jObject.getJSONObject("user").getString("lastname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentUser(ECUser user) {

        ECUser.currentUser = user;
    }

    /**
     * Method that refreshes the state of the current user by calling the API again.
     */
    public static void refreshCurrentUser() {
        //To be implemented.
    }

    public static ECUser getCurrentUser() {
        return ECUser.currentUser;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getUserToken() {
        return ECUser.userToken;
    }

    public boolean getLoginSuccessful() {
        return this.loginSuccess;
    }


}
