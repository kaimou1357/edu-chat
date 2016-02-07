package urlinq.android.com.edu_chat.controller.adapter;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.urlinq.edu_chat.R;

import urlinq.android.com.edu_chat.controller.ChatFragment;
import urlinq.android.com.edu_chat.model.ECCategory;
import urlinq.android.com.edu_chat.model.ECObject;
import urlinq.android.com.edu_chat.model.ECUser;
import urlinq.android.com.edu_chat.model.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Kai on 10/28/2015.
 */
public class MainScreenListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private final List<ECObject> mECObjects;
	private final AppCompatActivity activity;
	private static final int EMPTY_VIEW = 10;

	public MainScreenListAdapter(AppCompatActivity activity, List<ECObject> mECObjects) {
		this.mECObjects = mECObjects;
		this.activity = activity;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewCase) {
		View v;
        int layout;
        if(viewCase == EMPTY_VIEW){
            layout = R.layout.empty_group_view;
            v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new EmptyViewHolder(v);

        }
        else{
            layout = R.layout.main_list_scroll_item;
            v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new CategoryViewHolder(v);
            //Log.d("empty view", "Default View Case Loaded");
        }
	}
	@Override
	public int getItemViewType(int position){
		if(mECObjects.size() == 0){
			return EMPTY_VIEW;
		}
		return super.getItemViewType(position);
	}


	public void onBindViewHolder(RecyclerView.ViewHolder viewHolderCase, int position) {
        switch(viewHolderCase.getItemViewType()){
            case EMPTY_VIEW: {
                EmptyViewHolder emptyViewHolder = (EmptyViewHolder)viewHolderCase;
                break;
            }
            default:
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder)viewHolderCase;
                ECObject currObj = mECObjects.get(position);
                categoryViewHolder.setECObject(currObj);
                String fileURL = null;

                if (currObj instanceof ECUser) {
                    //Cast currObj to ECUser and then fill in headers.
                    ECUser user = (ECUser) currObj;
                    categoryViewHolder.setRowHeader(user.getFullName());
                    categoryViewHolder.setUserText(user.getFirstName());

                    if (user.getMostRecentMessage() != null) {
                        categoryViewHolder.setMessageText(user.getMostRecentMessage().getMessageTitle());
                    }
                    fileURL = Constants.bitmapURL + user.getFileURL();
                }
                if (currObj instanceof ECCategory) {
                    ECCategory category = (ECCategory) currObj;
                    categoryViewHolder.setRowHeader(category.getName());
                    if (category.getMostRecentMessage() != null) {
                        categoryViewHolder.setLastActivityTextView(category.getMostRecentMessage().getMessageDate(), category.getColor());
                        categoryViewHolder.setUserText(category.getMostRecentMessage().getAuthor().getFullName());
                        categoryViewHolder.setMessageText(category.getMostRecentMessage().getMessageTitle());
                    }
                    fileURL = Constants.bitmapURL + category.getFileURL();
                }
                Picasso.with(activity).load(fileURL).resize(Constants.globalImageSize, Constants.globalImageSize)
                        .centerInside().into(categoryViewHolder.img);

        }




	}

	@Override
	public int getItemCount() {
        return mECObjects.size() > 0 ? mECObjects.size() : 1;
	}
    public class EmptyViewHolder extends RecyclerView.ViewHolder{
        private final Button browseButton;

        public EmptyViewHolder(View view){
            super(view);
            browseButton = (Button)view.findViewById(R.id.browseGroupsButton);
            browseButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Log.d("RecyclerView Button", "Button Pressed!");
                }
            });
        }


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
			Handler handler = new Handler();
			//Use a handler to pause the thread for the ripple effect.
			handler.postDelayed(new Runnable() {
				public void run() {

					ChatFragment chat = new ChatFragment();
					Bundle b = new Bundle();
					b.putParcelable("PARCEL", ecObject);
					chat.setArguments(b);
					FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
					ft.setCustomAnimations(R.anim.enter_chat_fragment, R.anim.exit_chat_fragment);
					ft.add(R.id.layoutExternal, chat);
					ft.addToBackStack(null);
					ft.commit();

				}
			}, 200);




		}


	}
}
