package urlinq.android.com.edu_chat.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.Constants;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.manager.ECApiManager;


/**
 * Created by Kai on 9/6/2015.
 * This Fragment handles all the information associated with the different layouts between the signin form and the login form.
 */
public class LoginBackground extends Fragment {
	private String userHash;
	private View v;
	private LoginFragment logFrag;
	private Handler mHandler = new Handler();
	@Bind(R.id.signUpToggle)  ImageButton signUpBtn;
	@Bind(R.id.logInBlue) ImageButton logInBlue;
	@Bind(R.id.viewFlipper)  ViewFlipper flipper;
	@Bind(R.id.emailTextView) EditText userEmail;
	@Bind(R.id.passwordTextView) EditText userPass;

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.combined_login, container, false);
		//get reference to other fragment

		logFrag = (LoginFragment)getActivity().getFragmentManager().findFragmentById(R.id.mainLoginFragment);
		ButterKnife.bind(this, v);
		logInBlue.setOnClickListener(new View.OnClickListener() {
			public void onClick (View v) {
				//reference the progressbar in the other fragment.
				logFrag.startProgressBar();

				//This will delay the spinner circle a bit so it's  not too fast.
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {

					}
				}, 2000);

			}
		});
		//You need to pass the data from here via an intent to the MainActivity.

		return v;
	}

	/**
	 * Attempt to login separated into another method.
	 */
	private void attemptLogin(){
		RequestParams params = new RequestParams();
		params.put("email", userEmail.getText().toString());
		params.put("password", userPass.getText().toString());
		ECApiManager.post(Constants.loginAPI, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess (int statusCode, Header[] headers, byte[] responseBody) {
				//called when response code 200
				userHash = new String(responseBody);
				Log.d("login", userHash);
			}

			@Override
			public void onFailure (int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});

	}

	private class ProgressTask extends AsyncTask <Void,Void,Void>{
		@Override
		protected void onPreExecute(){
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			attemptLogin();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			//remove from activity once finished.
			logFrag.stopProgressBar();
		}
	}



}