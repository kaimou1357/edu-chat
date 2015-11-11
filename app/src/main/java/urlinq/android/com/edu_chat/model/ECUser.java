package urlinq.android.com.edu_chat.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    /**
     * This constructor will be for the current user using the application.
     * @param data
     * @throws JSONException
     */
    public ECUser(JSONObject data) throws JSONException {
        super(data.getJSONObject("user").getString("id"), data.getJSONObject("user").getJSONObject("picture_file").getString("file_url"));
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
     * Constructor for the static Build with JSON method.
     * @param identifier
     * @param fileURL
     * @param firstName
     * @param lastName
     * @param userType
     * @param mostRecentMessage
     * @param lastActivity
     * @param department
     */
    public ECUser(String identifier, String fileURL, String firstName, String lastName, ECUserType userType, ECMessage mostRecentMessage, Date lastActivity, String department){
        super(identifier, fileURL);
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.mostRecentMessage = mostRecentMessage;
        this.lastActivity = lastActivity;
        this.department = department;
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
    /**
     * This method will build an ArrayList of ECUsers to process in the Main RecyclerView List.
     * @param response people object from the loadout response.
     */
    public static ArrayList<ECUser> buildManyWithJSON(JSONArray response){

            ArrayList<ECUser> personList = new ArrayList<ECUser>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    String identifier = obj.getString("id");
                    String fileURL = obj.getJSONObject("picture_file").getString("file_url");
                    String firstName = obj.getString("firstname");
                    String lastName = obj.getString("lastname");
                    ECUserType userType = null;
                    String department = obj.getString("department");

                    if(obj.getString("type").equals("user")){
                        userType = ECUserType.ECUserTypeStudent;
                    }

                    ECMessage recentMessage = null;
                    Date lastActivity = null;
                    try{
                        recentMessage = new ECMessage(obj.getJSONObject("most_recent_message_info"));
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        lastActivity = format.parse(obj.getString("last_email").replace("T", " "));
                    }catch(ParseException e){
                        e.printStackTrace();
                    }
                    ECUser user = new ECUser(identifier, fileURL, firstName, lastName, userType, recentMessage,lastActivity, department );
                    personList.add(user);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        return personList;
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

    public static void setUserToken(String userToken) {
        ECUser.userToken = userToken;
    }

    public ECMessage getMostRecentMessage() {
        return mostRecentMessage;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public String getDepartment() {
        return department;
    }

    public ECUserType getUserType() {
        return userType;
    }

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
