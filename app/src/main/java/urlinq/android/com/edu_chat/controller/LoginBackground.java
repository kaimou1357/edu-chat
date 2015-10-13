package urlinq.android.com.edu_chat.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.manager.ECApiManager;


/**
 * Created by Kai on 9/6/2015.
 * This Fragment handles all the information associated with the different layouts between the signin form and the login form.
 */
public class LoginBackground extends Fragment {

	private ImageButton logInBtn;

	private String userHash;
	private EditText userEmail;
	private EditText userPass;
	private View v;
	private ViewFlipper flipper;

	@Bind(R.id.signUpToggle) ImageButton signUpBtn;
	@Bind(R.id.logInBlue) ImageButton logInBlue;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.combined_login, container, false);

		flipper = (ViewFlipper) v.findViewById(R.id.viewFlipper);
		//logInBtn = (ImageButton) v.findViewById(R.id.logInToggleBtn);

		userEmail = (EditText) v.findViewById(R.id.emailTextView);
		userPass = (EditText) v.findViewById(R.id.passwordTextView);

		logInBlue.setOnClickListener(new View.OnClickListener() {
			public void onClick (View v) {

				RequestParams params = new RequestParams();
				params.put("email", userEmail.getText().toString());
				params.put("password", userPass.getText().toString());

				ECApiManager.post("https://edu.chat/api/login/", params, new AsyncHttpResponseHandler() {
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
		});

		return v;
	}


}