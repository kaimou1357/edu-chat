package urlinq.android.com.edu_chat.manager;

import android.os.Looper;
import android.util.Log;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.model.ECUser;

import java.text.ParseException;


/**
 * Created by Kai on 9/21/2015.
 */
public class ECApiManager {

	private static final String loginAPI = "https://edu.chat/api/login/";
	private static final String loadoutAPI = "https://edu.chat/message/loadout";
	private static final String sendMessageURL = "https://edu.chat/message/send/";
	private static final String loadChatRoomURL = "https://edu.chat/message/load_chat";
	private static final String logoutURL = "https://edu.chat/api/logout/";

	private static final AsyncHttpClient syncHttpClient = new SyncHttpClient();
	private static final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

	public static void setCookieStore(PersistentCookieStore cookieStore) {
		getClient().setCookieStore(cookieStore);
	}


	private static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		getClient().get(url, params, responseHandler);
	}

	private static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
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

	public interface AllECApiCallsInterface {
		void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody);

		void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error);

		void onFinishGlobal();
	}

	private static class AllECApiCalls {
		private String url;
		private RequestParams params;
		private JSONObject obj;
		private AllECApiCallsInterface adapter;
		private String userHash;

		public void setUserHash(String userHash) {
			this.userHash = userHash;
		}

		public String getUserHash() {
			return userHash;
		}

		public void setAdapter(AllECApiCallsInterface adapter) {
			this.adapter = adapter;
		}

		public JSONObject getObj() {
			return obj;
		}

		public void invokePost() {
			ECApiManager.post(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					adapter.onSuccessGlobal(statusCode, headers, responseBody);
				}


				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					error.printStackTrace();
					Log.e(ECApiManager.class.getSimpleName(), "^ ^ ^ An ECApiManager call has failed!!");
					adapter.onFailureGlobal(statusCode, headers, responseBody, error);
				}

				@Override
				public void onFinish() {
					adapter.onFinishGlobal();
				}
			});
		}

		public void invokeGet() {
			ECApiManager.get(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					adapter.onSuccessGlobal(statusCode, headers, responseBody);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					error.printStackTrace();
					Log.e(ECApiManager.class.getSimpleName(), "^ ^ ^ An ECApiManager call has failed!!");
					adapter.onFailureGlobal(statusCode, headers, responseBody, error);
				}

				@Override
				public void onFinish() {
					adapter.onFinishGlobal();
				}
			});
		}

	}


	/**
	 * This class will load update the chatroom with new messages as soon as the user enters the chat room.
	 */
	public static class LoadChatMessageObject extends AllECApiCalls implements AllECApiCallsInterface {

		public LoadChatMessageObject(RequestParams params) {
			super.setAdapter(this);
			super.params = params;
			super.url = loadChatRoomURL;
		}

		@Override
		public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
			super.setUserHash(new String(responseBody));
			try {
				super.obj = new JSONObject(super.getUserHash());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		}

		@Override
		public void onFinishGlobal() {
		}
	}

	/**
	 * This class will set current user token, school, and will login the current user in.
	 */
	public static class LoginObject extends AllECApiCalls implements AllECApiCallsInterface {

		public LoginObject(RequestParams params) {
			super.setAdapter(this);
			super.params = params;
			super.url = loginAPI;
		}

		@Override
		public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
			super.setUserHash(new String(responseBody));
			Log.d("login", super.userHash);
			try {
				super.obj = new JSONObject(super.getUserHash());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		}

		@Override
		public void onFinishGlobal() {
			try {
				ECUser.setCurrentUser(new ECUser(getObj().getJSONObject("user")));
				ECUser.setUserToken(getObj().getString("token"));
				ECUser.setCurrentUserSchool(getObj().getJSONObject("user").getJSONObject("school").getString("school_name"));
			} catch (JSONException | ParseException e) {
				e.printStackTrace();
			}


//            try {

//
//                ParseInstallation install = ParseInstallation.getCurrentInstallation();
//                install.put("ID", Integer.toString(ECUser.getCurrentUser().getObjectIdentifier()));
//                install.put("First", ECUser.getCurrentUser().getFirstName());
//                install.put("Last", ECUser.getCurrentUser().getLastName());
//                install.save();
//
//                ParseObject login = new ParseObject("Logins");
//                login.put("useridnum", ECUser.getCurrentUser().getObjectIdentifier());
//                login.put("OS", "Android");
//                login.put("Install", install);
//                login.save();
//
//            } catch (ParseException | JSONException | com.parse.ParseException e) {
//                e.printStackTrace();
//            }


		}
	}

	/**
	 * This class will build and populate each Recyclerview in the application's MainActivity.
	 */
	public static class MainLoadOutObject extends AllECApiCalls implements AllECApiCallsInterface {

		public MainLoadOutObject(RequestParams params) {
			super.setAdapter(this);
			super.params = params;
			super.url = loadoutAPI;
		}

		@Override
		public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
			super.setUserHash(new String(responseBody));
			try {
				super.obj = new JSONObject(super.getUserHash());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		}

		@Override
		public void onFinishGlobal() {
		}

	}

	/**
	 * This class will log the user out.
	 */
	public static class LogoutObject extends AllECApiCalls implements AllECApiCallsInterface {

		public LogoutObject(RequestParams params) {
			super.setAdapter(this);
			super.params = params;
			super.url = logoutURL;
		}

		@Override
		public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
			super.setUserHash(new String(responseBody));
			try {
				super.obj = new JSONObject(super.getUserHash());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		}

		@Override
		public void onFinishGlobal() {
		}

	}

	/**
	 * This class will send messages for the user.
	 */
	public static class SendMessageObject extends AllECApiCalls implements AllECApiCallsInterface {

		public SendMessageObject(RequestParams params) {
			super.setAdapter(this);
			super.params = params;
			super.url = sendMessageURL;
		}

		@Override
		public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
			super.setUserHash(new String(responseBody));
			try {
				super.obj = new JSONObject(super.getUserHash());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		}

		@Override
		public void onFinishGlobal() {
		}
	}

}

