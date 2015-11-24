package urlinq.android.com.edu_chat.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import com.loopj.android.http.*;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import urlinq.android.com.edu_chat.controller.MainActivity;
import urlinq.android.com.edu_chat.model.ECUser;

import java.text.ParseException;


/**
 * Created by Kai on 9/21/2015.
 */
public class ECApiManager {

	public static final String loginAPI = "https://edu.chat/api/login/";
    public static final String loadoutAPI = "https://edu.chat/message/loadout";


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
        private MainActivity activity;

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
		public void invokeGet(){
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
				public void onFinish(){AllECApiCalls.this.onFinish();}
			});
		}
	}
    public static class ChatLoadOutObject extends AllECApiCalls{
        private JSONObject obj;
        private String userHash;
        private MainActivity activity;
        public ChatLoadOutObject(RequestParams params, MainActivity context){
            super.params = params;
            super.url = loadoutAPI;
            this.activity = context;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            super.onSuccess(statusCode, headers, responseBody);
            userHash = new String(responseBody);
            try {
                obj = new JSONObject(userHash);
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
            activity.makeObjectListsFromResponse(obj);
            activity.populateRecyclerView();
        }

    }

	public static class LoginObject extends AllECApiCalls {

		private JSONObject obj;
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
				obj = new JSONObject(userHash);
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
				ECUser.setCurrentUser(new ECUser(obj.getJSONObject("user")));
				ECUser.setUserToken(obj.getString("token"));
				ECUser.setCurrentUserSchool(obj.getJSONObject("user").getJSONObject("school").getString("school_name"));

				ParseInstallation.getCurrentInstallation().saveInBackground();
				ParseObject login = new ParseObject("Logins");
				login.put("userid", ECUser.getCurrentUser().getObjectIdentifier());
				login.put("OS", "Android");
				login.saveInBackground();

			} catch (ParseException | JSONException e) {
				e.printStackTrace();
			}


		}
	}
}

