package urlinq.android.com.edu_chat.manager;

import android.os.Looper;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.model.ECMessage;
import urlinq.android.com.edu_chat.model.ECUser;


/**
 * Created by Kai on 9/21/2015.
 */
public class ECApiManager {

    public static final String loginAPI = "https://edu.chat/api/login/";
    public static final String loadoutAPI = "https://edu.chat/message/loadout";
    public static final String sendMessageURL = "https://edu.chat/message/send/";
    public static final String loadChatRoomURL = "https://edu.chat/message/load_chat";


    // A SyncHttpClient is an AsyncHttpClient
    private static final AsyncHttpClient syncHttpClient = new SyncHttpClient();
    private static final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static void setCookieStore(PersistentCookieStore cookieStore) {
        getClient().setCookieStore(cookieStore);
    }


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().post(url, params, responseHandler);
    }

    /**
     * @return an async client when calling from the main thread, otherwise a sync client.
     */
    private static AsyncHttpClient getClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return asyncHttpClient;
    }

    private static class AllECApiCalls {
        private String url;
        private RequestParams params;
        private JSONObject obj;


        public JSONObject getObj() {
            return obj;
        }

        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            allAPICallErrorHandler(statusCode, headers, responseBody, error);
        }

        public void onFinish() {

        }

        public static void allAPICallErrorHandler(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            error.printStackTrace();
            Log.e(ECApiManager.class.getSimpleName(), "^ ^ ^ An ECApiManager call has failed!!");
        }

        public void invokePost() {
            ECApiManager.post(url, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    AllECApiCalls.this.onSuccess(statusCode, headers, responseBody);
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    AllECApiCalls.this.onFailure(statusCode, headers, responseBody, error);
                }

                @Override
                public void onFinish() {
                    AllECApiCalls.this.onFinish();
                }
            });
        }

        public void invokeGet() {
            ECApiManager.get(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    AllECApiCalls.this.onSuccess(statusCode, headers, responseBody);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    AllECApiCalls.this.onFailure(statusCode, headers, responseBody, error);
                }

                @Override
                public void onFinish() {
                    AllECApiCalls.this.onFinish();
                }
            });
        }
    }


    /**
     * This class will load update the chatroom with new messages as soon as the user enters the chat room.
     */
    public static class LoadChatMessageObject extends AllECApiCalls {
        private String userHash;

        public LoadChatMessageObject(RequestParams params) {
            super.params = params;
            super.url = loadChatRoomURL;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            super.onSuccess(statusCode, headers, responseBody);
            userHash = new String(responseBody);
            try {
                super.obj = new JSONObject(userHash).getJSONObject("messages");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            super.onFailure(statusCode, headers, responseBody, error);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            activity.makeObjects(getObj());
        }
    }

    /**
     * This class will set current user token, school, and will login the current user in.
     */
    public static class LoginObject extends AllECApiCalls {

        private String userHash;

        public LoginObject(RequestParams params) {
            super.params = params;
            super.url = loginAPI;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            super.onSuccess(statusCode, headers, responseBody);
            userHash = new String(responseBody);
            Log.d("login", userHash);
            try {
                super.obj = new JSONObject(userHash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            super.onFailure(statusCode, headers, responseBody, error);
        }

        @Override
        public void onFinish() {
            super.onFinish();

            try {
                ECUser.setCurrentUser(new ECUser(getObj().getJSONObject("user")));
                ECUser.setUserToken(getObj().getString("token"));
                ECUser.setCurrentUserSchool(getObj().getJSONObject("user").getJSONObject("school").getString("school_name"));

                ParseInstallation install = ParseInstallation.getCurrentInstallation();
                install.put("ID", ECUser.getCurrentUser().getObjectIdentifier());
                install.put("First", ECUser.getCurrentUser().getFirstName());
                install.put("Last", ECUser.getCurrentUser().getLastName());
                install.save();

                ParseObject login = new ParseObject("Logins");
                login.put("userid", ECUser.getCurrentUser().getObjectIdentifier());
                login.put("OS", "Android");
                login.put("Install", install);
                login.save();

            } catch (ParseException | JSONException | com.parse.ParseException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * This class will build and populate each Recyclerview in the application's MainActivity.
     */
    public static class MainLoadOutObject extends AllECApiCalls {
        private String userHash;

        public MainLoadOutObject(RequestParams params) {
            super.params = params;
            super.url = loadoutAPI;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            super.onSuccess(statusCode, headers, responseBody);
            userHash = new String(responseBody);
            try {
                super.obj = new JSONObject(userHash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            super.onFailure(statusCode, headers, responseBody, error);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            activity.makeObjectListsFromResponse(getObj());
            activity.populateRecyclerView();
        }

    }

    /**
     * This class will send messages for the user.
     */
    public static class SendMessageObject extends AllECApiCalls {
        private String userHash;

        public SendMessageObject(RequestParams params) {
            super.params = params;
            super.url = sendMessageURL;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            super.onSuccess(statusCode, headers, responseBody);
            userHash = new String(responseBody);
            try {
                super.obj = new JSONObject(userHash);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            super.onFailure(statusCode, headers, responseBody, error);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            try {
                activity.addMessage(new ECMessage(getObj().getJSONObject("message")));
            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }

        }
    }

}

