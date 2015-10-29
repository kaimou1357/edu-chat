package urlinq.android.com.edu_chat.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import butterknife.ButterKnife;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.adapter.ChatListAdapter;
import urlinq.android.com.edu_chat.model.ECUser;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity will act as a container for the recycler views for the individual chats and classes.
 * Created by Kai on 10/16/2015.
 */
public class MainActivity extends Activity {
	private List<ECUser> ecUserList = new ArrayList<>();
	private ChatListAdapter mAdapter;
	@Bind(R.id.userList) RecyclerView userList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_container);
		ButterKnife.bind(this);
		mAdapter = new ChatListAdapter(this, ecUserList);

		userList.setLayoutManager(new LinearLayoutManager(this));
		userList.setAdapter(mAdapter);
		refreshUserChatList();

//   Saving code later for testing.
//        if (savedInstanceState == null) {
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ChatFragment chatFrag = new ChatFragment();
//            ft.add(R.id.mainMenuContainer, chatFrag);
//            //Below the fragment is added to a small framelayout embedded within the main login screen.
//            ft.commit();
//        }

	}

	/**
	 * This method will populate ecUserList with the users loaded in from the login call.
	 */
	private void refreshUserChatList() {

		//quick test to make sure adapter is working.
		ecUserList.add(ECUser.getCurrentUser());
		mAdapter.notifyItemInserted(ecUserList.size() - 1);


	}

}
