package urlinq.android.com.edu_chat.controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.Parse;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.controller.adapter.MessageAdapter;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.constants.Constants;
import urlinq.android.com.edu_chat.model.ECMessage;
import urlinq.android.com.edu_chat.model.ECUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kai on 10/26/2015.
 */
public class ChatActivity extends AppCompatActivity {

	private static final int TYPING_TIMER_LENGTH = 600;
	private static final String REQUEST_LENGTH = "40";

	@Bind(R.id.messages) RecyclerView mMessagesView;
	@Bind(R.id.message_input) EditText mInputMessageView;
	@Bind(R.id.send_button) ImageButton sendButton;
	@Bind(R.id.nameTextView) TextView nameTextView;
	private final Handler mTypingHandler = new Handler();
	private boolean mTyping = false;
	//Don't forget to set the username to something before we begin.
	private String mUsername;
	private final List<ECMessage> mMessages = new ArrayList<>();
	private MessageAdapter mAdapter;
	private String fileURL;
    private String target_type;
    private String target_id;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_fragment);
		ButterKnife.bind(this);
		//get extras from bundle.
		updateRecyclerView();
        target_type = getIntent().getStringExtra("target_type");
        target_id = getIntent().getStringExtra("target_id");

		updateChatRoom(target_type, target_id, ECUser.getUserToken());
		mUsername = getIntent().getStringExtra("USER_NAME");
		nameTextView.setText(mUsername);

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
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (mUsername == null) return;
				if (!mTyping) {
					mTyping = true;

				}
				mTypingHandler.removeCallbacks(onTypingTimeOut);
				mTypingHandler.postDelayed(onTypingTimeOut, TYPING_TIMER_LENGTH);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptSend();
			}
		});

	}

	private void updateChatRoom(String targetType, String targetID, String token) {
		RequestParams params = new RequestParams();
		params.add("target_type", targetType);
		params.add("target_id", targetID);
		params.add("token", token);
		params.add("limit", REQUEST_LENGTH);
		ECApiManager.get(Constants.loadChatRoomURL, params, new AsyncHttpResponseHandler() {

			JSONArray obj;

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String response = new String(responseBody);
				Log.d("chatResponse", getRequestURI().toString());
				try {
					obj = new JSONObject(response).getJSONArray("messages");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}

			@Override
			public void onFinish() {
				makeObjects(obj);
			}

		});

	}

	private void makeObjects(JSONArray obj) {
		try {
			for (int i = 0; i < obj.length(); i++) {
				JSONObject singleMessage = obj.getJSONObject(i);
				mMessages.add(new ECMessage(singleMessage));
			}
		} catch (JSONException | ParseException e) {
			e.printStackTrace();
		}
		updateRecyclerView();

	}

	private void updateRecyclerView() {
		mAdapter = new MessageAdapter(this, mMessages);
		mMessagesView.setLayoutManager(new LinearLayoutManager(this));
		mMessagesView.setAdapter(mAdapter);
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
        params.add("token", ECUser.getUserToken());

        ECApiManager.post(Constants.sendMessageURL, params, new AsyncHttpResponseHandler() {
            JSONObject object;
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("send_msg_response", response);
                try{
                    object = new JSONObject(response);
                }catch(JSONException e){e.printStackTrace();}


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish(){
                //Add message to the adapterview after sending.
                try{
                    addMessage(new ECMessage(object.getJSONObject("message")));
                }catch(ParseException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }

	/**
	 * Adds message to the recyclerview.
	 */
	private void addMessage(ECMessage message) {
        mMessages.add(message);
        mAdapter.notifyItemInserted(mMessages.size() -1);
        scrollToBottom();
	}

	/**
	 * Method to scroll to the last position in an adapter.
	 */
	private void scrollToBottom() {
		mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
	}

	private final Runnable onTypingTimeOut = new Runnable() {
		@Override
		public void run() {
			if (!mTyping) return;
			mTyping = false;
		}
	};

}
