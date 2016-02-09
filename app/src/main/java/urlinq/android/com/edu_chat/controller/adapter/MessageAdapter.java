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
import com.urlinq.edu_chat.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import urlinq.android.com.edu_chat.model.ECMessage;
import urlinq.android.com.edu_chat.model.constants.Constants;


/**
 * Created by Kai on 10/26/2015.
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private final List<Object> mMessages;
	private final Activity activity;

	public MessageAdapter (Activity activity, List<Object> messages) {
		mMessages = messages;
		this.activity = activity;

	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		int layout;
		View v;
		switch(viewType){
			case Constants.MESSAGE_DATE:{
				layout = R.layout.chat_message_date;
				v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
				return new DateHolder(v);
			}
			default:{
				layout = R.layout.item_message;
				v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
				return new MessageHolder(v);

			}
		}


	}

	@Override
	public void onBindViewHolder (RecyclerView.ViewHolder viewHolderCase, int position) {
		ECMessage message = null;
		Date date;
		if(mMessages.get(position) instanceof ECMessage){
			MessageHolder messageHolder = (MessageHolder) viewHolderCase;
			message = (ECMessage)mMessages.get(position);
			messageHolder.setMessage(message.getMessageTitle());
			messageHolder.setUsername(message.getAuthor().getFullName());
			//parse the time and return the time as a String.

			messageHolder.setMessageDateView(message.getMessageDate());
			String path = Constants.bitmapURL + message.getAuthor().getFileURL();
			Picasso.with(activity).load(path).resize(Constants.globalImageSize, Constants.globalImageSize)
					.centerInside().into(messageHolder.userProfilePicture);
		}
		else{
			date = (Date)mMessages.get(position);
			DateHolder dateHolder = (DateHolder) viewHolderCase;
			dateHolder.setDate(date);

		}



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
		if(mMessages.get(position) instanceof ECMessage){
			return ((ECMessage) mMessages.get(position)).getMessageType().getValue();
		}
		else{
			return Constants.MESSAGE_DATE;
		}
	}


	public class DateHolder extends RecyclerView.ViewHolder{
		private final TextView mDateTextView;
		public DateHolder(View itemView){
			super(itemView);
			mDateTextView = (TextView)itemView.findViewById(R.id.message_date_textview);
		}
		public void setDate(Date messageDate){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
			String dayOfWeek = simpleDateFormat.format(messageDate);
			mDateTextView.setText(dayOfWeek);

		}
	}

	public class MessageHolder extends RecyclerView.ViewHolder {
		private final TextView mUserNameView;
		private final TextView mMessageView;
        private final TextView mMessageDateView;
		private final ImageView userProfilePicture;

		public MessageHolder(View itemView) {
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
