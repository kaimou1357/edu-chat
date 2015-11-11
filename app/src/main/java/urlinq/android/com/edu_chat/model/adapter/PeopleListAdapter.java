package urlinq.android.com.edu_chat.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.controller.ChatActivity;
import urlinq.android.com.edu_chat.model.ECUser;

/**
 * Created by Kai on 10/28/2015.
 */
public class PeopleListAdapter extends RecyclerView.Adapter<PeopleListAdapter.PeopleViewHolder> {
    private List<ECUser> peopleList;
    private Context mainActivity;

    public PeopleListAdapter(Context context, List<ECUser> peopleList){
        this.peopleList = peopleList;
        mainActivity = context;
    }
    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewCase){
        //Shouldn't be item scroll chat. Change later to the appropriate layout.
        int layout = R.layout.item_scroll_chat;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new PeopleViewHolder(v);

    }

    @Override
    public void onBindViewHolder(PeopleViewHolder viewHolder, int position){
        ECUser currUser = peopleList.get(position);
        viewHolder.setMessages(currUser.toString());

        //Set category toString() method for testing later.

        //Worry about profile pictures later.
        //viewHolder.setImg(currUser.getProfilePicture());

    }
    @Override
    public int getItemCount(){return peopleList.size();}




    public class PeopleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView img;
        private TextView userText;


        public PeopleViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            img = (ImageView)view.findViewById(R.id.imageView);
            userText = (TextView) view.findViewById(R.id.userTextView);
        }

        public void setUserName(String userName){
            if(userName == null) return;
            userText.setText(userName);
        }
        //Test method to set a message.

        public void setMessages(String messageTest){
            userText.setText(messageTest);
        }
        public void setImg(Bitmap b){
            if(b == null) return;
            img.setImageBitmap(b);
        }

        @Override
        public void onClick(View v){
            Intent i = new Intent(mainActivity, ChatActivity.class);
            //Put bundle information here
            Bundle bundle = new Bundle();
            bundle.putString("test", "hello");
            mainActivity.startActivity(i);
        }

    }
}