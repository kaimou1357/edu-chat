package urlinq.android.com.edu_chat.model;

import android.app.Activity;
import android.content.Context;

import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import urlinq.android.com.edu_chat.R;

/**
 * Created by Kai on 10/26/2015.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<ECMessage> mMessages;

    public MessageAdapter(Context context, List<ECMessage> messages){
        mMessages = messages;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        int layout = -1;
        switch(viewType) {
            case ECMessage.TYPE_MESSAGE:
                layout = R.layout.item_message;
                break;
            case ECMessage.TYPE_LOG:
                layout = R.layout.item_log;
                break;
            case ECMessage.TYPE_ACTION:
                layout = R.layout.item_action;
                break;
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);

    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        ECMessage message = mMessages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setUsername(message.getUsername());

    }
    @Override
    public int getItemCount(){
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position){
        return mMessages.get(position).getType();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mUserNameView;
        private TextView mMessageView;

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
