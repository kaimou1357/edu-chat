package urlinq.android.com.edu_chat;



import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class LoginActivity extends AppCompatActivity {

    public LoginFragment myFragment;
    public LoginBackground loginForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start the LoginFragment. This Activity will act as a container for the fragment.
        //This activity will only hold the Fragment and communicate between fragments and go onto the next activity.
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            myFragment = new LoginFragment();
            loginForm = new LoginBackground();
            ft.add(R.id.loginContainerView, myFragment);
            //Below the fragment is added to a small framelayout embedded within the main login screen.
            ft.add(R.id.loginFormFragmentContainer, loginForm);
            ft.commit();


        }

    }



}
