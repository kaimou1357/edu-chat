package urlinq.android.com.edu_chat.controller;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import urlinq.android.com.edu_chat.R;

public class LoginActivity extends Activity implements LoginBackground.OnLoginListener {

    private LoginFragment myFragment;
    private LoginBackground loginForm;
    private ProgressBarFragment progressFragment;


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
            progressFragment = new ProgressBarFragment();
            myFragment = new LoginFragment();
            ft.add(R.id.loginContainerView, myFragment);
            //Below the fragment is added to a small framelayout embedded within the main login screen.
            ft.add(R.id.loginFormFragmentContainer, loginForm);
            ft.commit();
        }

    }

    /**
     * Add a new fragment.
     * @param frag
     */
    private void replaceFragment(Fragment frag){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.loginContainerView, frag);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void loginSuccessful(boolean success){
        if(success){
            replaceFragment(new ProgressBarFragment());
        }
        else{
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }


    }





}
