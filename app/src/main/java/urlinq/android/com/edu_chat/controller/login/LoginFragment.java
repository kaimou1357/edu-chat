package urlinq.android.com.edu_chat.controller.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.urlinq.edu_chat.R;

/**
 * Created by Kai on 9/4/2015.
 */
public class LoginFragment extends Fragment {
	/**
	 * Fragment to load the LoginMain Layout.
	 *
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//initialize viewpager object.
		return inflater.inflate(R.layout.login_main, container, false);
	}


}







