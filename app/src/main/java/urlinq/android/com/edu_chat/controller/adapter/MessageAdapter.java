package urlinq.android.com.edu_chat.controller.adapter;

import android.content.Context;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.model.ECMessage;

/**
 * Created by Kai on 10/26/2015.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final List<ECMessage> mMessages;

    public MessageAdapter(Context context, List<ECMessage> messages){
        mMessages = messages;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        int layout = R.layout.item_message;

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);

    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        ECMessage message = mMessages.get(position);
        viewHolder.setMessage(message.getMessageTitle());
        viewHolder.setUsername(message.getAuthor());

    }
    @Override
    public int getItemCount(){
        return mMessages.size();
    }

    @UiThread
    public void dataSetChanged(){
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position){
        return mMessages.get(position).getMessageType().getValue();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mUserNameView;
        private final TextView mMessageView;

        public ViewHolder(View itemView){
            super(itemView);
            mUserNameView = (TextView)itemView.findViewById(R.id.username);
            mMessageView = (TextView) itemView.findViewById(R.id.message);

        }

        public void setUsername(String username){
            if(mUserNameView == null) return;
            mUserNameView.setText(username);
        }

        public void setMessage(String message){
            if(mMessageView == null) return;
            mMessageView.setText(message);
        }

    }
}
