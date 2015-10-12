package urlinq.android.com.edu_chat.model;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.manager.ECApiManager;


/**
 * Global to store authentication tokens, parse the String returned from POST/GET requests.
 * Created by Kai on 9/18/2015.
 */
public class ECUser {

    private static ECUser currentUser;
    private static String userToken;
    private final static  String loginAPI = "https://edu.chat/api/login/";
    private final static String loadUserAPI = "https://edu.chat/message/loadout/";

    private String firstName;
    private String lastName;
    private String userID;
    private JSONObject jObject;
    private boolean loginSuccess;


    public ECUser(JSONObject data) throws JSONException {
        this.jObject = data;
        try {
            this.loginSuccess = Boolean.parseBoolean(this.jObject.getString("success"));
            if(this.loginSuccess){
                this.userToken = this.jObject.getString("token");
                this.firstName = this.jObject.getJSONObject("user").getString("firstname");
                this.lastName = this.jObject.getJSONObject("user").getString("lastname");
            }


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
        RequestParams params = new RequestParams();
        params.put("token", userToken);
        ECApiManager.get(loadUserAPI, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String userHash = new String(responseBody);
                try{
                    JSONObject obj = new JSONObject(userHash);
                    ECUser.setCurrentUser(new ECUser(obj));

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
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
