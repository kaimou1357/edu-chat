package urlinq.android.com.edu_chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.model.ECUser;

/**
 * Created by Kai on 10/28/2015.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private List<ECUser> userList;

    public ChatListAdapter(Context context, List<ECUser> userList){
        this.userList = userList;
    }
    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewCase){
        int layout = R.layout.item_scroll_chat;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ChatViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ChatViewHolder viewHolder, int position){
        ECUser currUser = userList.get(position);
        viewHolder.setUserName(currUser.getFirstName() + " " + currUser.getLastName());
        //Worry about profile pictures later.
        //viewHolder.setImg(currUser.getProfilePicture());

    }
    @Override
    public int getItemCount(){return userList.size();}



    public class ChatViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView userText;


        public ChatViewHolder(View view){
            super(view);
            img = (ImageView)view.findViewById(R.id.imageView);
            userText = (TextView) view.findViewById(R.id.userTextView);
        }

        public void setUserName(String userName){
            if(userName == null) return;
            userText.setText(userName);
        }
        public void setImg(Bitmap b){
            if(b == null) return;
            img.setImageBitmap(b);
        }

    }
}