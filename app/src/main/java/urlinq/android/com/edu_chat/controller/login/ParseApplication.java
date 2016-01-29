package urlinq.android.com.edu_chat.controller.login;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by kai on 1/28/16.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "8HRtau0wTVMAJCL6QAb0qVi1iI661qsdy7CT9xnN", "Bd4rgDMBk5OIHbYB9wdlyA75Ys2QckhhtQpZmvxO");
    }
}
