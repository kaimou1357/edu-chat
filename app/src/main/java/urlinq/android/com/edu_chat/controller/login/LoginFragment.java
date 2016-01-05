package urlinq.android.com.edu_chat.controller.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.urlinq.edu_chat.R;

/**
 * Created by Kai on 9/4/2015.
 */
public class LoginFragment extends Fragment {
	int numberOfViewPagerChildren = 3;
	int lastIndexOfViewPagerChildren = numberOfViewPagerChildren;


	/**
	 * Load the login_main.xml layout.
	 * In addition, load the ViewPager.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//initialize viewpager object.
		View v = inflater.inflate(R.layout.login_main, container, false);



		return v;
	}



}







