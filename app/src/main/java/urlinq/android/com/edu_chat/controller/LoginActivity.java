package urlinq.android.com.edu_chat.controller;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import urlinq.android.com.edu_chat.R;

public class LoginActivity extends Activity {

    public LoginFragment myFragment;
    public LoginBackground loginForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_main);

        //Start the LoginFragment. This Activity will act as a container for the fragment.
        //This activity will only hold the Fragment and communicate between fragments and go onto the next activity.
        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            loginForm = new LoginBackground();
            ft.add(R.id.loginContainerView, myFragment);
            //Below the fragment is added to a small framelayout embedded within the main login screen.
            ft.add(R.id.loginFormFragmentContainer, loginForm);
            ft.commit();
        }

    }


}
