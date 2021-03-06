package urlinq.android.com.edu_chat.controller.main;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.urlinq.edu_chat.R;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.solovyev.android.views.llm.LinearLayoutManager;
import urlinq.android.com.edu_chat.controller.chat.ChatFragment;
import urlinq.android.com.edu_chat.controller.adapter.MainScreenListAdapter;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.ECCategory;
import urlinq.android.com.edu_chat.model.ECObject;
import urlinq.android.com.edu_chat.model.ECUser;
import urlinq.android.com.edu_chat.model.constants.Constants;
import urlinq.android.com.edu_chat.model.enums.ECCategoryType;
import urlinq.android.com.edu_chat.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * This activity will act as a container for the recycler views for the individual chats and classes.
 * Created by Kai on 10/16/2015.
 */
public class MainActivity extends AppCompatActivity {
	private List<ECObject> ECCategoryGroupList = new ArrayList<>();
	private List<ECObject> ECCategoryClassList = new ArrayList<>();
	private List<ECObject> ECCategoryDepartmentList = new ArrayList<>();
	private final List<ECObject> ECCategoryLabList = new ArrayList<>();
	private List<ECObject> recentList = new ArrayList<>();
	private int totalNumOfChats;

	@Bind(R.id.classList) RecyclerView classList;
	@Bind(R.id.groupList) RecyclerView groupList;
	@Bind(R.id.departmentList) RecyclerView departmentList;
	@Bind(R.id.labList) RecyclerView labList;
	@Bind(R.id.peopleList) RecyclerView peopleList;

	@Bind(R.id.userFullName) TextView userFullName;
	@Bind(R.id.userSchool) TextView userSchoolName;
	@Bind(R.id.userProfilePicture) ImageView userProfilePicture;
	@Bind(R.id.chatsUnreadText) TextView chatsUnreadButton;
	@Bind(R.id.tool_bar) Toolbar toolbar;
	private ChatFragment mChat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState!= null){
			mChat = (ChatFragment)getSupportFragmentManager().getFragment(savedInstanceState, "mChat");
		}
		setContentView(R.layout.main_activity_layout);
		ButterKnife.bind(this);


		loadCurrentUserText();
		getChatLoadOut();
		setSupportActionBar(toolbar);

		ECApiManager.LogToServer("Successful Login", "VERBOSE");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.logout:
				logOut();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Log the user out and return them back to the loginactivity.
	 */
	private void logOut() {
		RequestParams params = new RequestParams();
		params.put("token", ECUser.getUserToken());
		final ECApiManager.LogoutObject logoutObject = new ECApiManager.LogoutObject(params) {
			@Override
			public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
				super.onSuccessGlobal(statusCode, headers, responseBody);
			}

			@Override
			public void onFinishGlobal() {
				super.onFinishGlobal();
				//Once done, return back to LoginActivity.
				finish();
			}

			@Override
			public void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				super.onFailureGlobal(statusCode, headers, responseBody, error);
			}
		};
		logoutObject.invokePost();
	}

	/**
	 * This method will populate ecUserList with the users loaded in from the login call.
	 */
	private void getChatLoadOut() {
		RequestParams params = new RequestParams();
		params.put("token", ECUser.getUserToken());
		final ECApiManager.MainLoadOutObject chatObj = new ECApiManager.MainLoadOutObject(params) {
			@Override
			public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
				super.onSuccessGlobal(statusCode, headers, responseBody);
				makeObjectListsFromResponse(super.getObj());
			}

			@Override
			public void onFinishGlobal() {
				super.onFinishGlobal();
				Log.d("JSONResponse", super.getObj().toString());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						populateRecyclerView();
						chatsUnreadButton.setText(String.format(getString(R.string.ChatsUnreadString), totalNumOfChats));



					}
				});
			}

			@Override
			public void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				super.onFailureGlobal(statusCode, headers, responseBody, error);
			}
		};
		chatObj.invokeGet();
	}

	/**
	 * Takes the output from the loadout API call and makes them into objects.
	 */
	private void makeObjectListsFromResponse(JSONObject response) {
		//Create each ECCategory object. Fill into RecyclerView later.
		try {
			//Add for classes, departments, people, groups.
			ECCategoryClassList = ECCategory.buildManyWithJSON(response.getJSONArray("classes"), ECCategoryType.ECClassCategoryType);
			ECCategoryGroupList = ECCategory.buildManyWithJSON(response.getJSONArray("groups"), ECCategoryType.ECGroupCategoryType);
			ECCategoryDepartmentList = ECCategory.buildManyWithJSON(response.getJSONArray("departments"), ECCategoryType.ECDepartmentCategoryType);
			recentList = ECUser.buildManyWithJSON(response.getJSONArray("recent"));
			totalNumOfChats = recentList.size() + ECCategoryClassList.size() + ECCategoryDepartmentList.size() + ECCategoryGroupList.size();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will load the profile picture and the student full name and school.
	 */
	private void loadCurrentUserText() {
		userFullName.setText(String.format("%s %s", ECUser.getCurrentUser().getFirstName(), ECUser.getCurrentUser().getLastName()));
		userSchoolName.setText(String.format("%s", ECUser.getCurrentUserSchool()));
		Picasso.with(this).load(Constants.bitmapURL + ECUser.getCurrentUser().getFileURL()).resize(Constants.globalImageSize, Constants.globalImageSize).into(userProfilePicture);

	}

	private void populateRecyclerView() {
		classList.addItemDecoration(new DividerItemDecoration(this));
		classList.setHasFixedSize(true);

		groupList.addItemDecoration(new DividerItemDecoration(this));
		groupList.setHasFixedSize(true);

		departmentList.addItemDecoration(new DividerItemDecoration(this));
		departmentList.setHasFixedSize(true);

		peopleList.addItemDecoration(new DividerItemDecoration(this));
		peopleList.setHasFixedSize(true);

		MainScreenListAdapter groupAdapter = new MainScreenListAdapter(this, ECCategoryGroupList);
		groupList.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(this));
		groupList.setAdapter(groupAdapter);

		MainScreenListAdapter classAdapter = new MainScreenListAdapter(this, ECCategoryClassList);
		classList.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(this));
		classList.setItemAnimator(new DefaultItemAnimator());
		classList.setAdapter(classAdapter);

		MainScreenListAdapter departmentAdapter = new MainScreenListAdapter(this, ECCategoryDepartmentList);
		departmentList.setLayoutManager(new LinearLayoutManager(this));
		departmentList.setAdapter(departmentAdapter);

		MainScreenListAdapter peopleAdapter = new MainScreenListAdapter(this, recentList);
		peopleList.setLayoutManager(new LinearLayoutManager(this));
		peopleList.setAdapter(peopleAdapter);

		MainScreenListAdapter labAdapter = new MainScreenListAdapter(this, ECCategoryLabList);
		labList.setLayoutManager(new LinearLayoutManager(this));
		labList.setAdapter(labAdapter);


	}



}
