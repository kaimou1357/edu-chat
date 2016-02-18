package urlinq.android.com.edu_chat.controller.login;

import android.app.Application;
import com.instabug.library.IBGInvocationEvent;
import com.instabug.library.Instabug;
import com.parse.Parse;

/**
 * Created by kai on 1/28/16.
 */
public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "8HRtau0wTVMAJCL6QAb0qVi1iI661qsdy7CT9xnN", "Bd4rgDMBk5OIHbYB9wdlyA75Ys2QckhhtQpZmvxO");

		new Instabug.Builder(this, "4719bf1a7d10635f7c1520989f084d4c")
				.setInvocationEvent(IBGInvocationEvent.IBGInvocationEventFloatingButton)
				.build();
	}
}
