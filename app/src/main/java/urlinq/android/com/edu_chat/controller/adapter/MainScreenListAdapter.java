package urlinq.android.com.edu_chat.controller.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.controller.ChatActivity;
import urlinq.android.com.edu_chat.model.Constants;
import urlinq.android.com.edu_chat.model.ECCategory;
import urlinq.android.com.edu_chat.model.ECObject;
import urlinq.android.com.edu_chat.model.ECUser;

import java.util.List;

/**
 * Created by Kai on 10/28/2015.
 */
public class MainScreenListAdapter extends RecyclerView.Adapter<MainScreenListAdapter.CategoryViewHolder> {
	private final List<ECObject> mECObjects;
	private final Context mainActivity;

	public MainScreenListAdapter(Context context, List<ECObject> mECObjects) {
		this.mECObjects = mECObjects;
		this.mainActivity = context;
	}

	@Override
	public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewCase) {
		//Shouldn't be item scroll chat. Change later to the appropriate layout.
		int layout = R.layout.item_scroll_chat;
		View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
		return new CategoryViewHolder(v);

	}

	@Override
	public void onBindViewHolder(CategoryViewHolder viewHolder, int position) {
		ECObject currObj = mECObjects.get(position);
		viewHolder.setMessages(currObj.toString());
		viewHolder.setECObject(currObj);
		if(currObj instanceof ECUser){
			ECUser user = (ECUser) currObj;
			String fileURL = Constants.bitmapURL + user.getFileURL();
			Picasso.with(mainActivity).load(fileURL).resize(1280, 720).into(viewHolder.img);
		}
		if(currObj instanceof ECCategory){
			ECCategory category = (ECCategory) currObj;
			String fileURL = Constants.bitmapURL + category.getFileURL();
			Picasso.with(mainActivity).load(fileURL).resize(1280, 720).into(viewHolder.img);
		}

	}

	@Override
	public int getItemCount() {
		return mECObjects.size();
	}


	public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private final ImageView img;
		private final TextView userText;
		private ECObject ecObject;


		public CategoryViewHolder(View view) {
			super(view);
			//set the onclick listener in the constructor.
			view.setOnClickListener(this);
			img = (ImageView) view.findViewById(R.id.profilePicture);
			userText = (TextView) view.findViewById(R.id.userTextView);
		}

		public void setUserName(String userName) {
			if (userName == null) return;
			userText.setText(userName);
		}
		//Test method to set a message.

		public void setMessages(String messageTest) {
			userText.setText(messageTest);
		}


		@Override
		public void onClick(View v) {
			Intent i = new Intent(mainActivity, ChatActivity.class);
			if (ecObject instanceof ECCategory) {
				ECCategory cat = (ECCategory) ecObject;
				i.putExtra("USER_NAME", cat.getName());
				i.putExtra("target_type",cat.getTypeOfCategory().getCategoryString());
				i.putExtra("target_id", cat.getObjectIdentifier());
			} else if (ecObject instanceof ECUser) {
				ECUser user = (ECUser) ecObject;
				i.putExtra("USER_NAME", user.getFirstName() + " " + user.getLastName());
				i.putExtra("target_type", user.getUserType().getUserTypeString());
				i.putExtra("target_id", user.getObjectIdentifier());
			}
			mainActivity.startActivity(i);
		}


		public void setECObject(ECObject ecObject) {
			this.ecObject = ecObject;
		}
	}
}
