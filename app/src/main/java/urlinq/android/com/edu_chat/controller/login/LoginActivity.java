package urlinq.android.com.edu_chat.controller.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.parse.ConfigCallback;
import com.parse.Parse;
import com.parse.ParseConfig;
import com.parse.ParseException;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.controller.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginBackground.OnLoginListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_main);
		ButterKnife.bind(this);
		Parse.enableLocalDatastore(this);

		Parse.initialize(this, "8HRtau0wTVMAJCL6QAb0qVi1iI661qsdy7CT9xnN", "Bd4rgDMBk5OIHbYB9wdlyA75Ys2QckhhtQpZmvxO");

		ParseConfig.getInBackground(new ConfigCallback() {
			@Override
			public void done(ParseConfig config, ParseException e) {
				if (e == null) {
					Log.d(LoginActivity.this.getClass().getSimpleName(), "Yay! Config was fetched from the server.");
				} else {
					Log.e("TAG", "Failed to fetch. Using Cached Config.");
//					config = ParseConfig.getCurrentConfig();
				}
			}
		});

		//Start the LoginFragment. This Activity will act as a container for the fragment.
		//This activity will only hold the Fragment and communicate between fragments and go onto the next activity.
		if (savedInstanceState == null) {
			android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			LoginBackground loginForm = new LoginBackground();
			LoginFragment myFragment = new LoginFragment();
			ft.add(R.id.loginContainerView, myFragment);
			//Below the fragment is added to a small framelayout embedded within the main login screen.
			ft.add(R.id.loginFormFragmentContainer, loginForm);
			ft.commit();
		}

	}


	@Override
	public void loginSuccessful(boolean success) {
		if (success) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		} else {
			Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
		}


	}


}
