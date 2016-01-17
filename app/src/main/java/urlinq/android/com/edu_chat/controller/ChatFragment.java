package urlinq.android.com.edu_chat.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.urlinq.edu_chat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import urlinq.android.com.edu_chat.controller.adapter.MessageAdapter;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.ECCategory;
import urlinq.android.com.edu_chat.model.ECMessage;
import urlinq.android.com.edu_chat.model.ECObject;
import urlinq.android.com.edu_chat.model.ECSubchat;
import urlinq.android.com.edu_chat.model.ECUser;

/**
 * Created by Kai Mou on 1/12/2016.
 */
public class ChatFragment extends Fragment {
    private static final String REQUEST_LENGTH = "30";
    @Bind(R.id.messages)
    RecyclerView mMessagesView;
    @Bind(R.id.message_input)
    EditText mInputMessageView;
    @Bind(R.id.send_button)
    Button sendButton;
    @Bind(R.id.chatToolBar)
    Toolbar toolbar;
    @Bind(R.id.subChatSpinner)
    Spinner subChatSpinner;

    private final List<ECMessage> mMessages = new ArrayList<>();
    private MessageAdapter mAdapter;
    private String target_type;
    private String target_id;
    private String subchannel_id;
    private String chatTitle;
    private ArrayList<ECSubchat> subchats;
    private boolean isSubChannel = false;
    private boolean isUserChat = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.chat_layout, container, false);
        v.setBackgroundColor(Color.WHITE);
        ButterKnife.bind(this, v);
        /**
         * Handle Passed Object in this section of the code.
         */
        Bundle b = this.getArguments();
        final ECObject passedObject = b.getParcelable("PARCEL");
        if(passedObject instanceof ECCategory) {
            ECCategory cat = (ECCategory) passedObject;
            chatTitle = cat.getName();
            target_id = Integer.toString(cat.getObjectIdentifier());
            target_type = cat.getTypeOfCategory().getCategoryString();
            subchats = cat.getSubchannels();
            isUserChat = false;
        }
        if(passedObject instanceof ECUser){
            ECUser user = (ECUser) passedObject;
            chatTitle = user.getFullName();
            target_id = Integer.toString(user.getObjectIdentifier());
            target_type = "user";
            isUserChat = true;
        }
        if(passedObject instanceof ECSubchat){
            ECSubchat subchat = (ECSubchat) passedObject;
            chatTitle = subchat.getName();
            target_id = Integer.toString(subchat.getOrigin_id());
            target_type = subchat.getOrigin_type();
            subchannel_id = Integer.toString(subchat.getSubchannel_id());
            isSubChannel = true;
            Log.d("subchat", "ECSUBCHAT Processed!");
        }
        updateChatRoom(target_type, target_id, null, ECUser.getUserToken());
        mAdapter = new MessageAdapter(getActivity(), mMessages);
        mMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessagesView.setAdapter(mAdapter);
        if(isUserChat){
            socketSetup(target_type, Integer.toString(ECUser.getCurrentUser().getObjectIdentifier()));
        }
        else{
            socketSetup(target_type, target_id);
        }


        /**
         * Handle ToolBar setup in this section of the code.
         */
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        ArrayList<String> nameOfSubChats = new ArrayList<>();
        //If subchats = null, no need for a drop down spinner button, so disable the spinner.
        if(subchats == null){
            nameOfSubChats.add(chatTitle);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_list, nameOfSubChats);
            subChatSpinner.setAdapter(adapter);
            subChatSpinner.setEnabled(false);
            Log.d("Subchats", "subchats is null");
        }
        else{
            nameOfSubChats.add(chatTitle);
            Log.d("Subchats", "subchats is not null");
            if(subchats.size() == 0){
                subChatSpinner.setEnabled(false);
            }
            if(subchats.size()!=0){
                for(int i = 0; i<subchats.size(); i++)
                {
                    nameOfSubChats.add(subchats.get(i).getName());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_list, nameOfSubChats);
            //Do not modify this order. The setSelection following setAdapter makes sure Spinner doesn't fire off before user selection.
            subChatSpinner.setAdapter(adapter);
            subChatSpinner.setSelection(0, false);
            subChatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(subchats.size() !=0){
                        if(position == 0){
                            ECCategory cat = (ECCategory) passedObject;
                            mMessages.clear();
                            updateChatRoom(cat.getTypeOfCategory().getCategoryString(), Integer.toString(cat.getObjectIdentifier()), null, ECUser.getCurrentUser().getUserToken());
                            mAdapter.notifyDataSetChanged();
                            isSubChannel = false;
                            socketSetup(cat.getTypeOfCategory().getCategoryString(), Integer.toString(cat.getObjectIdentifier()));
                        }
                        else{
                            ECSubchat chat = (ECSubchat) subchats.get(position-1);
                            Log.d("subchat position", position + "");
                            mMessages.clear();
                            updateChatRoom(chat.getOrigin_type(), Integer.toString(chat.getOrigin_id()), Integer.toString(chat.getSubchannel_id()), ECUser.getUserToken());
                            mAdapter.notifyDataSetChanged();
                            socketSetup("subchannel", Integer.toString(chat.getSubchannel_id()));
                        }

                    }
                    Log.d("subchats", "Item Selected" + position);

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Configure the activity to end once user clicks back button.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });



        /**
         * Handle EditText Functions here.
         */
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.send || actionId == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });

        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    /**
     * Method to setup socket to listen on specific channel.
     */
    private void socketSetup(final String target_type, final String target_id){


        final String type = target_type;
        final String id = target_id;
        try{
            SocketIO.setDefaultSSLSocketFactory(SSLContext.getInstance("Default"));
            SocketIO socket = new SocketIO("https://edu.chat/");
            socket.connect(new IOCallback() {
                @Override
                public void onMessage(JSONObject json, IOAcknowledge ack) {
                    Log.d("Socket", "The server sent a message");
                }

                @Override
                public void onMessage(String data, IOAcknowledge ack) {
                }

                @Override
                public void onError(SocketIOException socketIOException) {
                    Log.d("Socket", "Connection error with Socket");
                    socketIOException.printStackTrace();
                }
                @Override
                public void onDisconnect() {
                    Log.d("Socket", "Socket Disconnected");
                }

                @Override
                public void onConnect() {
                    Log.d("Socket", "Connection Established");
                }

                @Override
                public void on(String event, IOAcknowledge ack, Object... args) {
                    try{
                        JSONObject serverJSON = new JSONObject(args[0].toString());
                        String currentChat = type+ "_" + id;
                        if(event.equals(currentChat) && serverJSON.getString("type").equals("text")){
                            final ECMessage message = new ECMessage(serverJSON);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addMessage(message);
                                }
                            });
                        }
                    }catch(JSONException | ParseException e){
                        e.printStackTrace();
                    }

                }
            });
        }catch(Exception e){
            Log.d("Socket", e.getMessage());
        }
    }





    private void updateChatRoom(String targetType, String targetID, String subchannel_id, String token) {
        RequestParams params = new RequestParams();
        params.add("subchannel_id", subchannel_id);
        params.add("target_type", targetType);
        params.add("target_id", targetID);
        params.add("token", token);
        params.add("limit", REQUEST_LENGTH);
        ECApiManager.LoadChatMessageObject loadChatObj = new ECApiManager.LoadChatMessageObject(params) {
            @Override
            public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccessGlobal(statusCode, headers, responseBody);
            }
            @Override
            public void onFinishGlobal() {
                super.onFinishGlobal();
                try {
                    makeObjects(getObj().getJSONArray("messages"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateRecyclerView();
                    }
                });
            }
            @Override
            public void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailureGlobal(statusCode, headers, responseBody, error);
            }
        };
        loadChatObj.invokeGet();
    }

    public void makeObjects(JSONArray obj) {
        try {
            for (int i = 0; i < obj.length(); i++) {
                JSONObject singleMessage = obj.getJSONObject(i);
                mMessages.add(new ECMessage(singleMessage));
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateRecyclerView() {
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }
    /**
     * Private method to send message. Make sure to make the HTTP/Socket calls here.
     */
    private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }
        mInputMessageView.setText("");
        RequestParams params = new RequestParams();
        params.add("text", message);
        params.add("target_id", target_id);
        params.add("target_type", target_type);
        if(isSubChannel){
            params.add("subchannel_id", subchannel_id );
        }
        Log.d("subchat sending", target_id + target_type + " ");
        params.add("token", ECUser.getUserToken());
        final ECApiManager.SendMessageObject sendMessageObject = new ECApiManager.SendMessageObject(params) {
            @Override
            public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccessGlobal(statusCode, headers, responseBody);
            }

            @Override
            public void onFinishGlobal() {
                super.onFinishGlobal();
                if(isUserChat){
                    try {
                        addMessage(new ECMessage(super.getObj().getJSONObject("message")));
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailureGlobal(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailureGlobal(statusCode, headers, responseBody, error);
            }
        };
        sendMessageObject.invokePost();
    }
    /**
     * Adds message to the recyclerview.
     */
    public void addMessage(ECMessage message) {
        mMessages.add(message);
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    /**
     * Method to scroll to the last position in an adapter.
     */
    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }
}
