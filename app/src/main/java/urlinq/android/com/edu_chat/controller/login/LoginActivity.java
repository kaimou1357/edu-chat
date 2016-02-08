package urlinq.android.com.edu_chat.controller.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.instabug.library.Feature;
import com.instabug.library.IBGInvocationEvent;
import com.instabug.library.Instabug;
import com.urlinq.edu_chat.R;
import urlinq.android.com.edu_chat.controller.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginBackground.OnLoginListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		new Instabug.Builder(this.getApplication(), "4719bf1a7d10635f7c1520989f084d4c")
				.setInvocationEvent(IBGInvocationEvent.IBGInvocationEventShake)
				.setDebugEnabled(true)
				.setInstabugLogState(Feature.State.ENABLED)
				.build();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_main);
		ButterKnife.bind(this);

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
