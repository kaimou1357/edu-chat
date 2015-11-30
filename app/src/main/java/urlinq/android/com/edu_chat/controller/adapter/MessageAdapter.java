package urlinq.android.com.edu_chat.controller.adapter;

import android.app.Activity;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.model.ECMessage;
import urlinq.android.com.edu_chat.model.constants.Constants;


/**
 * Created by Kai on 10/26/2015.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
	private final List<ECMessage> mMessages;
	private final Activity activity;

	public MessageAdapter (Activity activity, List<ECMessage> messages) {
		mMessages = messages;
		this.activity = activity;

	}

	@Override
	public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		int layout = R.layout.item_message;
		View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder (ViewHolder viewHolder, int position) {
		ECMessage message = mMessages.get(position);
		viewHolder.setMessage(message.getMessageTitle());
		viewHolder.setUsername(message.getAuthor().getFullName());
        //parse the time and return the time as a String.

        viewHolder.setMessageDateView(message.getMessageDate());
		String path = Constants.bitmapURL + message.getAuthor().getFileURL();
        Picasso.with(activity).load(path).resize(Constants.globalImageSize, Constants.globalImageSize)
				.centerInside().into(viewHolder.userProfilePicture);


	}

	@Override
	public int getItemCount () {
		return mMessages.size();
	}

	@UiThread
	public void dataSetChanged () {
		notifyDataSetChanged();
	}


	@Override
	public int getItemViewType (int position) {
		return mMessages.get(position).getMessageType().getValue();
	}


	public class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView mUserNameView;
		private final TextView mMessageView;
        private final TextView mMessageDateView;
		private final ImageView userProfilePicture;

		public ViewHolder (View itemView) {
			super(itemView);
			mUserNameView = (TextView) itemView.findViewById(R.id.username);
            mMessageDateView = (TextView) itemView.findViewById(R.id.messageDate);
			userProfilePicture = (ImageView) itemView.findViewById(R.id.userProfilePicture);
			mMessageView = (TextView) itemView.findViewById(R.id.message);

		}
        public void setMessageDateView(Date messageDate){
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
            mMessageDateView.setText(formatter.format(messageDate));
        }

		public void setUsername (String username) {
			if (mUserNameView == null) return;
			mUserNameView.setText(username);
		}

		public void setMessage (String message) {
			if (mMessageView == null) return;
			mMessageView.setText(message);
		}

	}
}
