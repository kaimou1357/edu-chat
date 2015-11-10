package urlinq.android.com.edu_chat.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.model.Constants;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.model.adapter.ChatListAdapter;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.ECCategory;
import urlinq.android.com.edu_chat.model.enums.ECCategoryType;
import urlinq.android.com.edu_chat.model.ECUser;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity will act as a container for the recycler views for the individual chats and classes.
 * Created by Kai on 10/16/2015.
 */
public class MainActivity extends AppCompatActivity {
	private List<ECCategory> ECCategoryGroupList = new ArrayList<>();
    private List<ECCategory> ECCategoryClassList = new ArrayList<ECCategory>();
    private List<ECCategory> ECCategoryDepartmentList = new ArrayList<ECCategory>();
    private List<ECUser> peopleList = new ArrayList<ECUser>();
    private List<ECUser> mostRecentList = new ArrayList<ECUser>();

    private JSONObject loadOut;


	private ChatListAdapter labAdapter;
    private ChatListAdapter classAdapter;
    private ChatListAdapter departmentAdapter;
    private ChatListAdapter groupAdapter;

    private ECUser currentUser;

	@Bind(R.id.classList) RecyclerView classList;
	@Bind(R.id.groupList) RecyclerView groupList;
    @Bind(R.id.departmentList) RecyclerView departmentList;
    @Bind(R.id.labList) RecyclerView labList;
	@Bind(R.id.userFullName) TextView userFullName;
	@Bind(R.id.userSchool) TextView userSchoolName;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_container);
		ButterKnife.bind(this);
        loadCurrentUser();
        getChatLoadOut();
        //Check the populateRecyclerView() method. Will load after all objects are loaded.
    }
    /**
	 * This method will populate ecUserList with the users loaded in from the login call.
	 */
	private void getChatLoadOut() {
		RequestParams params = new RequestParams();
		params.put("token", ECUser.getUserToken());
		ECApiManager.get(Constants.loadoutAPI, params, new AsyncHttpResponseHandler() {
            JSONObject obj;

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("response", response);
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
                populateRecyclerView();
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
			ECCategoryGroupList = ECCategory.buildManyWithJSON(response.getJSONArray("groups"), ECCategoryType.ECGroupCategoryType);
            ECCategoryClassList = ECCategory.buildManyWithJSON(response.getJSONArray("classes"), ECCategoryType.ECClassCategoryType);
            ECCategoryDepartmentList = ECCategory.buildManyWithJSON(response.getJSONArray("departments"), ECCategoryType.ECDepartmentCategoryType);
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
    private void populateRecyclerView(){

        if(ECCategoryGroupList != null){
            groupAdapter = new ChatListAdapter(this, ECCategoryGroupList);
            groupList.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(this));
            groupList.setAdapter(groupAdapter);
        }

        if(ECCategoryClassList !=null){
            classAdapter = new ChatListAdapter(this, ECCategoryClassList);
            classList.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(this));
            classList.setAdapter(classAdapter);
        }
        if(ECCategoryDepartmentList !=null){
            departmentAdapter = new ChatListAdapter(this, ECCategoryDepartmentList);
            departmentList.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(this));
            departmentList.setAdapter(departmentAdapter);
        }

    }

}
