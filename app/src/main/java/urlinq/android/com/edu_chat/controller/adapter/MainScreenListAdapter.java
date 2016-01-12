package urlinq.android.com.edu_chat.controller.adapter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.urlinq.edu_chat.R;
import urlinq.android.com.edu_chat.controller.ChatActivity;
import urlinq.android.com.edu_chat.model.ECCategory;
import urlinq.android.com.edu_chat.model.ECObject;
import urlinq.android.com.edu_chat.model.ECUser;
import urlinq.android.com.edu_chat.model.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by Kai on 10/28/2015.
 */
public class MainScreenListAdapter extends RecyclerView.Adapter<MainScreenListAdapter.CategoryViewHolder> {
	private final List<ECObject> mECObjects;
	private final Activity activity;

	public MainScreenListAdapter(Activity activity, List<ECObject> mECObjects) {
		this.mECObjects = mECObjects;
		this.activity = activity;
	}

	@Override
	public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewCase) {
		int layout;
		switch(viewCase){
			case 0:{
				layout = R.layout.empty_category;
				View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
				return new CategoryViewHolder(v);
			}
			default:{
				layout = R.layout.main_list_scroll_item;
				View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
				return new CategoryViewHolder(v);
			}

		}


	}
	@Override
	public int getItemViewType(int position){
		//Swaps RecyclerViews between 0 and 1 here.
		if(mECObjects == null){
			return 0;
		}
		return 1;
	}

	@Override
	public void onBindViewHolder(final CategoryViewHolder viewHolder, int position) {

		ECObject currObj = mECObjects.get(position);
		viewHolder.setECObject(currObj);
		String fileURL = null;
		if (currObj instanceof ECUser) {
			ECUser user = (ECUser) currObj;
			viewHolder.setRowHeader(user.getFullName());
			viewHolder.setUserText(user.getFirstName());
			if (user.getMostRecentMessage() != null) {
				viewHolder.setMessageText(user.getMostRecentMessage().getMessageTitle());
			}

			fileURL = Constants.bitmapURL + user.getFileURL();
		}
		if (currObj instanceof ECCategory) {
			ECCategory category = (ECCategory) currObj;
			viewHolder.setRowHeader(category.getName());
			if (category.getMostRecentMessage() != null) {
				viewHolder.setLastActivityTextView(category.getMostRecentMessage().getMessageDate(), category.getColor());
				viewHolder.setUserText(category.getMostRecentMessage().getAuthor().getFullName());
				viewHolder.setMessageText(category.getMostRecentMessage().getMessageTitle());
			}
			fileURL = Constants.bitmapURL + category.getFileURL();
		}
		Picasso.with(activity).load(fileURL).resize(Constants.globalImageSize, Constants.globalImageSize)
				.centerInside().into(viewHolder.img);

	}

	@Override
	public int getItemCount() {
		return mECObjects.size();
	}


	public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private final ImageView img;
		private final TextView userText;
		private ECObject ecObject;
		private final TextView messageTextView;
		private final TextView headerTextView;
		private final TextView lastActivityTextView;


		public CategoryViewHolder(View view) {
			super(view);
			//set the onclick listener in the constructor.
			view.setOnClickListener(this);
			img = (ImageView) view.findViewById(R.id.profilePicture);
			userText = (TextView) view.findViewById(R.id.userTextView);
			messageTextView = (TextView) view.findViewById(R.id.messageTextView);
			headerTextView = (TextView) view.findViewById(R.id.rowHeader);
			lastActivityTextView = (TextView) view.findViewById(R.id.lastActivityTextView);
		}

		public void setMessageText(String messageTest) {
			messageTextView.setText(messageTest);
		}

		public void setRowHeader(String rowHeader) {
			headerTextView.setText(rowHeader);
		}

		public void setLastActivityTextView(Date time, String color) {
			lastActivityTextView.setTextColor(Color.parseColor(color));
			SimpleDateFormat localDateFormat = new SimpleDateFormat("h:mm a");
			localDateFormat.setTimeZone(TimeZone.getDefault());
			String textTime = localDateFormat.format(time);

			lastActivityTextView.setText(textTime);
		}

		public void setUserText(String username) {
			userText.setText(username);
		}

		public void setECObject(ECObject ecObject) {
			this.ecObject = ecObject;
		}

		@Override
		public void onClick(View v) {
			Intent i = new Intent(activity, ChatActivity.class);
			i.putExtra("PARCEL", ecObject);
			activity.startActivity(i);
		}


	}
}
