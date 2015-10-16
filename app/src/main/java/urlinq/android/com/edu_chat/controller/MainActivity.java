package urlinq.android.com.edu_chat.controller;

import android.app.Activity;
import android.os.Bundle;

import urlinq.android.com.edu_chat.R;

/**
 * This activity will act as a container for the recycler views for the individual chats and classes.
 * Created by Kai on 10/16/2015.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chat_container);

    }

}
