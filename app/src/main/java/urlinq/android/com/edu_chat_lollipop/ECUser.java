package urlinq.android.com.edu_chat_lollipop;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Global to store authentication tokens, parse the String returned from POST/GET requests.
 * Created by Kai on 9/18/2015.
 */
public class ECUser {

    private static ECUser currentUser;
    private String firstName;
    private String lastName;
    private static String userToken;
    private static String userID;
    private JSONObject jObject;
    private ApiManager task = new ApiManager();


    public ECUser(String userHashFromServer) throws JSONException {

        this.jObject = new JSONObject(userHashFromServer);
        this.userToken = jObject.getJSONObject("token").toString();
        this.firstName = jObject.getJSONObject("user").getString("firstname");
        this.lastName = jObject.getJSONObject("user").getString("lastname");
    }

    public static void setCurrentUser(ECUser user) {

        currentUser = user;
    }

    /**
     * Method that refreshes the state of the current user by calling the API again.
     */
    public static void refreshCurrentUser() {


    }

    public static ECUser getCurrentUser() {
        return currentUser;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getUserToken() {
        return this.userToken;
    }


}
