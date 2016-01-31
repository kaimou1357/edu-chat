package urlinq.android.com.edu_chat.controller.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.loopj.android.http.RequestParams;
import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.urlinq.edu_chat.BuildConfig;
import com.urlinq.edu_chat.R;
import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.ECUser;


/**
 * Created by Kai on 9/6/2015.
 * This Fragment handles all the information associated with the different layouts between the signin form and the login form.
 */
public class LoginBackground extends Fragment {
	private SharedPreferences prefs;
	@Bind(R.id.logInBlue) Button logInBlue;
	@Bind(R.id.emailTextView) EditText userEmail;
	@Bind(R.id.passwordTextView) EditText userPass;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.combined_login, container, false);
		ButterKnife.bind(this, v);
		prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		if (prefs.getBoolean("saveLogin", false)) {
			userEmail.setText(prefs.getString("email", ""));
			userPass.setText(prefs.getString("pass", ""));
			ParseConfig.getInBackground(new ConfigCallback() {
				@Override
				public void done(ParseConfig config, ParseException e) {
					if (config.getBoolean(getString(R.string.PARSE_CONFIG_AUTO_LOGIN), false)) {
						attemptLogin();
					} else {
						Log.v(getClass().getSimpleName(), "Do not auto login");
					}
				}
			});
		}
		logInBlue.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				attemptLogin();
			}
		});



		return v;
	}

	/**
	 * Attempt to login separated into another method.
	 */
	private void attemptLogin() {
		RequestParams params = new RequestParams();
		params.put("email", userEmail.getText().toString());
		params.put("password", userPass.getText().toString());
		ECApiManager.LoginObject loginObj = new ECApiManager.LoginObject(params) {
			@Override
			public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
				super.onSuccessGlobal(statusCode, headers, responseBody);
				SharedPreferences.Editor editPrefs = prefs.edit();
				editPrefs.putBoolean("saveLogin", true);
				editPrefs.putString("email", userEmail.getText().toString());
				editPrefs.putString("pass", userPass.getText().toString());
				editPrefs.apply();
			}

			@Override
			public void onFinishGlobal() {
				super.onFinishGlobal();
				launchMainActivity();
			}

			@Override
			public void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				super.onFailureGlobal(statusCode, headers, responseBody, error);
			}
		};
		loginObj.invokePost();
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