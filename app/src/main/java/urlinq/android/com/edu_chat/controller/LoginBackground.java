package urlinq.android.com.edu_chat.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.Constants;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.ECUser;


/**
 * Created by Kai on 9/6/2015.
 * This Fragment handles all the information associated with the different layouts between the signin form and the login form.
 */
public class LoginBackground extends Fragment {
	private View v;
	static String userHash;
	//@Bind(R.id.signUpToggle)  ImageButton signUpBtn;
	@Bind(R.id.logInBlue) ImageButton logInBlue;
	//@Bind(R.id.viewFlipper)  ViewFlipper flipper;
	@Bind(R.id.emailTextView) EditText userEmail;
	@Bind(R.id.passwordTextView) EditText userPass;

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.combined_login, container, false);
		//get reference to other fragment
		ButterKnife.bind(this, v);
		logInBlue.setOnClickListener(new View.OnClickListener() {
			public void onClick (View v) {
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
	private void attemptLogin(){
		//final CountDownLatch latch = new CountDownLatch(1);
		RequestParams params = new RequestParams();
		params.put("email", userEmail.getText().toString());
		params.put("password", userPass.getText().toString());
		ECApiManager.post(Constants.loginAPI, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				userHash = new String(responseBody);
				Log.d("login", userHash);
				try {
					JSONObject obj = new JSONObject(userHash);
					ECUser.setCurrentUser(new ECUser(obj));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}


		});
		/**
		 * Currently bugged right now. Needs to press twice to login. Might have something to do with signal callback.
		 */

		if (ECUser.getCurrentUser() == null) {
			((OnLoginListener) getActivity()).loginSuccessful(false);
		}
		else if (ECUser.getCurrentUser().getLoginSuccessful()){
			((OnLoginListener) getActivity()).loginSuccessful(true);
		}
		else{
			((OnLoginListener) getActivity()).loginSuccessful(true);
		}

	}


	public interface OnLoginListener{
		public void loginSuccessful(boolean success);
	}



}