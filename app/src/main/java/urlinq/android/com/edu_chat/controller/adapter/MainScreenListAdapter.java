package urlinq.android.com.edu_chat.controller.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.controller.ChatActivity;
import urlinq.android.com.edu_chat.model.ECCategory;
import urlinq.android.com.edu_chat.model.ECObject;
import urlinq.android.com.edu_chat.model.ECUser;
import urlinq.android.com.edu_chat.model.constants.Constants;


/**
 * Created by Kai on 10/28/2015.
 */
public class MainScreenListAdapter extends RecyclerView.Adapter<MainScreenListAdapter.CategoryViewHolder> {
	private final List<ECObject> mECObjects;
	private final Activity activity;

	public MainScreenListAdapter (Activity activity, List<ECObject> mECObjects) {
		this.mECObjects = mECObjects;
		this.activity = activity;
	}

	@Override
	public CategoryViewHolder onCreateViewHolder (ViewGroup parent, int viewCase) {
		//Shouldn't be item scroll chat. Change later to the appropriate layout.
		int layout = R.layout.item_scroll_chat;
		View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
		return new CategoryViewHolder(v);

	}

	@Override
	public void onBindViewHolder (final CategoryViewHolder viewHolder, int position) {
		Picasso.with(activity).setIndicatorsEnabled(true);
		Picasso.with(activity).setLoggingEnabled(true);

		ECObject currObj = mECObjects.get(position);
		viewHolder.setECObject(currObj);
		String fileURL = null;
		if (currObj instanceof ECUser) {
			ECUser user = (ECUser) currObj;
			viewHolder.setRowHeader(user.getFullName());
			viewHolder.setUserText(user.getFirstName());
			viewHolder.setMessageText(user.getMostRecentMessage().getMessageTitle());
			fileURL = Constants.bitmapURL + user.getFileURL();
		}
		if (currObj instanceof ECCategory) {
			ECCategory category = (ECCategory) currObj;
			viewHolder.setRowHeader(category.getName());
			viewHolder.setUserText(category.getMostRecentMessage().getAuthor().getFullName());
			viewHolder.setMessageText(category.getMostRecentMessage().getMessageTitle());
			fileURL = Constants.bitmapURL + category.getFileURL();
		}
		Picasso.with(activity).load(fileURL).resize(Constants.globalImageSize, Constants.globalImageSize)
				.centerInside().into(viewHolder.img);

	}

	@Override
	public int getItemCount () {
		return mECObjects.size();
	}


	public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private final ImageView img;
		private final TextView userText;
		private ECObject ecObject;
		private final TextView messageTextView;
		private final TextView headerTextView;


		public CategoryViewHolder (View view) {
			super(view);
			//set the onclick listener in the constructor.
			view.setOnClickListener(this);
			img = (ImageView) view.findViewById(R.id.profilePicture);
			userText = (TextView) view.findViewById(R.id.userTextView);
			messageTextView = (TextView) view.findViewById(R.id.messageTextView);
			headerTextView = (TextView) view.findViewById(R.id.rowHeader);
		}

		public void setMessageText (String messageTest) {
			messageTextView.setText(messageTest);
		}

		public void setRowHeader (String rowHeader) {
			headerTextView.setText(rowHeader);
		}

		public void setUserText (String username) {
			userText.setText(username);
		}

		public void setECObject (ECObject ecObject) {
			this.ecObject = ecObject;
		}

		@Override
		public void onClick (View v) {
			Intent i = new Intent(activity, ChatActivity.class);
			if (ecObject instanceof ECCategory) {
				ECCategory cat = (ECCategory) ecObject;
				i.putExtra("USER_NAME", cat.getName());
				i.putExtra("target_type", cat.getTypeOfCategory().getCategoryString());
				i.putExtra("target_id", cat.getObjectIdentifier());
				i.putExtra("file_id", cat.getFileURL());
			} else if (ecObject instanceof ECUser) {
				ECUser user = (ECUser) ecObject;
				i.putExtra("USER_NAME", user.getFirstName() + " " + user.getLastName());
				i.putExtra("target_type", user.getUserType().getUserTypeString());
				i.putExtra("target_id", user.getObjectIdentifier());
				i.putExtra("file_id", user.getFileURL());
			}
			activity.startActivity(i);
		}


	}
}
