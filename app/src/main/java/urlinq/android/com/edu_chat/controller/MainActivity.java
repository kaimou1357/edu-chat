package urlinq.android.com.edu_chat.controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.solovyev.android.views.llm.LinearLayoutManager;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.controller.adapter.MainScreenListAdapter;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.ECCategory;
import urlinq.android.com.edu_chat.model.ECObject;
import urlinq.android.com.edu_chat.model.ECUser;
import urlinq.android.com.edu_chat.model.constants.Constants;
import urlinq.android.com.edu_chat.model.enums.ECCategoryType;

import java.util.ArrayList;
import java.util.List;


/**
 * This activity will act as a container for the recycler views for the individual chats and classes.
 * Created by Kai on 10/16/2015.
 */
public class MainActivity extends Activity {
	private List<ECObject> ECCategoryGroupList = new ArrayList<>();
	private List<ECObject> ECCategoryClassList = new ArrayList<>();
	private List<ECObject> ECCategoryDepartmentList = new ArrayList<>();
	private List<ECObject> recentList = new ArrayList<>();

	@Bind(R.id.classList) RecyclerView classList;
	@Bind(R.id.groupList) RecyclerView groupList;
	@Bind(R.id.departmentList) RecyclerView departmentList;
	@Bind(R.id.labList) RecyclerView labList;
	@Bind(R.id.peopleList) RecyclerView peopleList;
	@Bind(R.id.userFullName) TextView userFullName;
	@Bind(R.id.userSchool) TextView userSchoolName;
	@Bind(R.id.userProfilePicture)ImageView userProfilePicture;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_container);
		ButterKnife.bind(this);
		loadCurrentUserText();
		getChatLoadOut();
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method will populate ecUserList with the users loaded in from the login call.
	 */
	private void getChatLoadOut () {
		RequestParams params = new RequestParams();
		params.put("token", ECUser.getUserToken());
        final ECApiManager.MainLoadOutObject chatObj = new ECApiManager.MainLoadOutObject(params){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.e("JSONResponse", super.getObj().toString());
                makeObjectListsFromResponse(super.getObj());
                populateRecyclerView();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
            }
        };

        chatObj.invokeGet();
    }

	/**
	 * Takes the output from the loadout API call and makes them into objects.
	 */
	private void makeObjectListsFromResponse (JSONObject response) {
		//Create each ECCategory object. Fill into RecyclerView later.
		try {
			//Add for classes, departments, people, groups.
			ECCategoryGroupList = ECCategory.buildManyWithJSON(response.getJSONArray("groups"), ECCategoryType.ECGroupCategoryType);
			ECCategoryClassList = ECCategory.buildManyWithJSON(response.getJSONArray("classes"), ECCategoryType.ECClassCategoryType);
			ECCategoryDepartmentList = ECCategory.buildManyWithJSON(response.getJSONArray("departments"), ECCategoryType.ECDepartmentCategoryType);
			recentList = ECUser.buildManyWithJSON(response.getJSONArray("recent"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will load the profile picture and the student full name and school.
	 */
	private void loadCurrentUserText () {
		userFullName.setText(String.format("%s %s", ECUser.getCurrentUser().getFirstName(), ECUser.getCurrentUser().getLastName()));
		userSchoolName.setText(String.format("%s", ECUser.getCurrentUserSchool()));
		Picasso.with(this).load(Constants.bitmapURL + ECUser.getCurrentUser().getFileURL()).resize(Constants.globalImageSize, Constants.globalImageSize).into(userProfilePicture);

	}

	public void populateRecyclerView () {

		MainScreenListAdapter groupAdapter = new MainScreenListAdapter(this, ECCategoryGroupList);
		groupList.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(this));
		groupList.setAdapter(groupAdapter);

		MainScreenListAdapter classAdapter = new MainScreenListAdapter(this, ECCategoryClassList);
		classList.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(this));
		classList.setAdapter(classAdapter);

		MainScreenListAdapter departmentAdapter = new MainScreenListAdapter(this, ECCategoryDepartmentList);
		departmentList.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(this));
		departmentList.setAdapter(departmentAdapter);

		MainScreenListAdapter peopleAdapter = new MainScreenListAdapter(this, recentList);
		peopleList.setLayoutManager(new LinearLayoutManager(this));
		peopleList.setAdapter(peopleAdapter);

	}


}
