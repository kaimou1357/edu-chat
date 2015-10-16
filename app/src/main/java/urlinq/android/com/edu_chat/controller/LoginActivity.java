package urlinq.android.com.edu_chat.controller;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import urlinq.android.com.edu_chat.R;

public class LoginActivity extends Activity {

    private LoginFragment myFragment;
    private LoginBackground loginForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ButterKnife.bind(this);

        //Start the LoginFragment. This Activity will act as a container for the fragment.
        //This activity will only hold the Fragment and communicate between fragments and go onto the next activity.
        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            loginForm = new LoginBackground();
            myFragment = new LoginFragment();
            ft.add(R.id.loginContainerView, myFragment,"loginFragment");
            //Below the fragment is added to a small framelayout embedded within the main login screen.
            ft.add(R.id.loginFormFragmentContainer, loginForm);
            ft.commit();
        }





    }





}
