package urlinq.android.com.edu_chat;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start the LoginFragment. This Activity will act as a container for the fragment.
        //This activity will only hold the Fragment and communicate between fragments and go onto the next activity.
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.loginContainerView, new LoginFragment());
            ft.commit();
        }

    }


}
