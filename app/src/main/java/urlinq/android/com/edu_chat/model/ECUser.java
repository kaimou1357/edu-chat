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


    public ECUser(String userHash) {
        // TODO: Jacob: Please rebuild without try catch. One error will domino.
        try {
            this.jObject = new JSONObject(userHash);
            this.userToken = this.jObject.getJSONObject("token").toString();
            this.loginSuccess = Boolean.parseBoolean(jObject.getJSONObject("success").toString());
            this.firstName = this.jObject.getJSONObject("user").getString("firstname");
            this.lastName = this.jObject.getJSONObject("user").getString("lastname");
        } catch (JSONException e) {

        }
    }

    public static void setCurrentUser(ECUser user) {

        ECUser.currentUser = user;
    }

    /**
     * Method that refreshes the state of the current user by calling the API again.
     */
    public static void refreshCurrentUser() {
    }

    public static ECUser getCurrentUser() {
        return ECUser.currentUser;
    }

    public static String getUserToken() {
        return ECUser.userToken;
    }

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
