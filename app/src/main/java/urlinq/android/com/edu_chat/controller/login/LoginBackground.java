package urlinq.android.com.edu_chat.controller.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.ParseConfig;
import com.parse.ParseObject;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.constants.Constants;
import urlinq.android.com.edu_chat.model.ECUser;

import java.text.ParseException;


/**
 * Created by Kai on 9/6/2015.
 * This Fragment handles all the information associated with the different layouts between the signin form and the login form.
 */
public class LoginBackground extends Fragment {
	private static String userHash;
	private SharedPreferences prefs;
	//@Bind(R.id.signUpToggle)  ImageButton signUpBtn;
	@Bind(R.id.logInBlue) ImageButton logInBlue;
	//@Bind(R.id.viewFlipper)  ViewFlipper flipper;
	@Bind(R.id.emailTextView) EditText userEmail;
	@Bind(R.id.passwordTextView) EditText userPass;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.combined_login, container, false);
		//get reference to other fragment
		ButterKnife.bind(this, v);
		// TODO: Don't do what I am doing lol.
		prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		if (prefs.getBoolean("saveLogin", false)) {
			userEmail.setText(prefs.getString("email", ""));
			userPass.setText(prefs.getString("pass", ""));
			ParseConfig config = ParseConfig.getCurrentConfig();
			if (config.getBoolean(getString(R.string.PARSE_CONFIG_AUTO_LOGIN), false)) {
				attemptLogin();
			}
		}
		logInBlue.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//This will delay the spinner circle a bit so it's  not too fast.
				attemptLogin();
			}
		});
		//You need to pass the data from here via an intent to the MainActivity.

		return v;
	}

	/**
	 * Attempt to login separated into another method.
	 */
	private void attemptLogin() {
		//final CountDownLatch latch = new CountDownLatch(1);
		RequestParams params = new RequestParams();

		params.put("email", userEmail.getText().toString());
		params.put("password", userPass.getText().toString());
		ECApiManager.post(Constants.loginAPI, params, new AsyncHttpResponseHandler() {
			JSONObject obj;

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				SharedPreferences.Editor editPrefs = prefs.edit();
				editPrefs.putBoolean("saveLogin", true);
				editPrefs.putString("email", userEmail.getText().toString());
				// TODO: Fix this once alex lets us hash! This is bad!!
				editPrefs.putString("pass", userPass.getText().toString());
				editPrefs.apply();


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

			}

			@Override
			public void onFinish() {
				try {
					ECUser.setCurrentUser(new ECUser(obj.getJSONObject("user")));
					ECUser.setUserToken(obj.getString("token"));

					ParseObject login = new ParseObject("Logins");
					login.put("userid", ECUser.getCurrentUser().getObjectIdentifier());
					login.put("OS", "Android");
					login.saveInBackground();

					launchMainActivity();
				} catch (ParseException | JSONException e) {
					e.printStackTrace();
				}


			}
		});
	}

	private void launchMainActivity() {
		if (ECUser.getCurrentUser() == null) {
			((OnLoginListener) getActivity()).loginSuccessful(false);
		} else {
			((OnLoginListener) getActivity()).loginSuccessful(true);
		}
	}


	public interface OnLoginListener {
		void loginSuccessful(boolean success);
	}


}