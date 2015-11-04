package urlinq.android.com.edu_chat.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.Constants;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.adapter.ChatListAdapter;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.ECCategory;
import urlinq.android.com.edu_chat.model.ECCategoryType;
import urlinq.android.com.edu_chat.model.ECObject;
import urlinq.android.com.edu_chat.model.ECUser;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity will act as a container for the recycler views for the individual chats and classes.
 * Created by Kai on 10/16/2015.
 */
public class MainActivity extends AppCompatActivity {
	private List<ECUser> ecUserList = new ArrayList<>();
	private List<ECCategory> ecCategoryGroupList = new ArrayList<>();
	private ChatListAdapter mAdapter;
	private ECUser currentUser;
	private ECObject classes;
	private ECObject departments;
	private ECObject people;
	@Bind(R.id.classList) RecyclerView userList;
	@Bind(R.id.groupList) RecyclerView groupList;
	@Bind(R.id.userFullName) TextView userFullName;
	@Bind(R.id.userSchool) TextView userSchoolName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_container);
		ButterKnife.bind(this);
		mAdapter = new ChatListAdapter(this, ecUserList);

		userList.setLayoutManager(new LinearLayoutManager(this));
		userList.setAdapter(mAdapter);
		loadCurrentUser();
		refreshUserChatList();
	}


	/**
	 * This method will populate ecUserList with the users loaded in from the login call.
	 */
	private void refreshUserChatList() {
		RequestParams params = new RequestParams();
		params.put("token", ECUser.getUserToken());
		ECApiManager.get(Constants.loadoutAPI, params, new AsyncHttpResponseHandler() {
			JSONObject obj;

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String response = new String(responseBody);
				try {
					obj = new JSONObject(response);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}

			@Override
			public void onFinish() {
				makeObjects(obj);
			}
		});
	}

	/**
	 * Takes the output from the loadout API call and makes them into objects.
	 */
	private void makeObjects(JSONObject response) {
		//Create each ECCategory object. Fill into RecyclerView later.
		try {
			//Add for classes, departments, people, groups.
			ecCategoryGroupList = ECCategory.buildManyWithJSON(response.getJSONArray("groups"), ECCategoryType.ECGroupCategoryType);

		} catch (JSONException e) {
			e.printStackTrace();
		}


	}

	/**
	 * This method will load the profile picture and the student full name and school.
	 */
	private void loadCurrentUser() {
		currentUser = ECUser.getCurrentUser();
		userFullName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());

	}

}
