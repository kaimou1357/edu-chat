package urlinq.android.com.edu_chat.controller.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.urlinq.edu_chat.BuildConfig;
import com.urlinq.edu_chat.R;

import butterknife.Bind;
import butterknife.ButterKnife;

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

	@Bind(R.id.buildVersionText)
	TextView buildVersion;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//initialize viewpager object.
		//set build version to show programmatically.
		View v = inflater.inflate(R.layout.login_main, container, false);
		ButterKnife.bind(this, v);

		String versionName = BuildConfig.VERSION_NAME;
		int version = BuildConfig.VERSION_CODE;

		buildVersion.setText(versionName + " (" + version + ")");
		return v;
	}


}







