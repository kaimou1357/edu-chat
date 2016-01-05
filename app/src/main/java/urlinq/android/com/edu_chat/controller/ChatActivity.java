package urlinq.android.com.edu_chat.controller;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.loopj.android.http.RequestParams;
import com.urlinq.edu_chat.R;

import butterknife.OnItemClick;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import urlinq.android.com.edu_chat.controller.adapter.MessageAdapter;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.ECMessage;
import urlinq.android.com.edu_chat.model.ECUser;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kai on 10/26/2015.
 */
public class ChatActivity extends AppCompatActivity {

	private static final String REQUEST_LENGTH = "40";

	@Bind(R.id.messages) RecyclerView mMessagesView;
	@Bind(R.id.message_input) EditText mInputMessageView;
	@Bind(R.id.send_button) Button sendButton;
	private boolean mTyping = false;

	//Don't forget to set the username to something before we begin.
	private final List<ECMessage> mMessages = new ArrayList<>();
	private MessageAdapter mAdapter;
	private String target_type;
	private String target_id;
	private Socket mSocket;




	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			IO.Options opts = new IO.Options();
			opts.port = 443;
			mSocket = IO.socket("http://54.208.114.222:443");
			Log.d("Socket", "Socket successfully connected");
		}catch(URISyntaxException ignored){
			ignored.printStackTrace();
			Log.e("Socket", "Socket connections has failed");
		}
		String userID = "#user_" + ECUser.getCurrentUser().getObjectIdentifier();

		//socket IO stuff
		mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
			@Override
			public void call(Object... args){
				Log.d("socket","Connected to chats");
			}

		}).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener(){
			public void call(Object... args){
				Log.d("socket", "connection failed");
			}
		}).on(userID, new Emitter.Listener() {
			public void call(Object... args) {
				Log.d("socket", "I received data!");
			}
		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){
			public void call(Object... args){
				Log.d("socket", "disconnected");
			}

		});

		mSocket.connect();

		setContentView(R.layout.chat_layout);
		ButterKnife.bind(this);


		mAdapter = new MessageAdapter(this, mMessages);
		mMessagesView.setLayoutManager(new LinearLayoutManager(this));
		mMessagesView.setAdapter(mAdapter);





		final ActionBar actionBar = getSupportActionBar();
		View cView = getLayoutInflater().inflate(R.layout.chat_custom_action_bar_view, null);
		if (actionBar != null) {
			actionBar.setCustomView(cView);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
		}

		//get extras from bundle.
		target_type = getIntent().getStringExtra("target_type");
		target_id = getIntent().getStringExtra("target_id");
		String chatTitle = getIntent().getStringExtra("USER_NAME");
		TextView actionBarTitleTextView = (TextView) cView.findViewById(R.id.actionBarTitleText);
		actionBarTitleTextView.setText(chatTitle);

		updateChatRoom(target_type, target_id, ECUser.getUserToken());



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
				if (!mTyping) {
					mTyping = true;

				}

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

	@Override
	public void onDestroy() {
		super.onDestroy();
		mSocket.disconnect();
		mSocket.off(Socket.EVENT_MESSAGE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	private void updateChatRoom(String targetType, String targetID, String token) {
		RequestParams params = new RequestParams();
		params.add("target_type", targetType);
		params.add("target_id", targetID);
		params.add("token", token);
		params.add("limit", REQUEST_LENGTH);
		ECApiManager.LoadChatMessageObject loadChatObj = new ECApiManager.LoadChatMessageObject(params) {
			@Override
			public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
				super.onSuccessGlobal(statusCode, headers, responseBody);

				try {
					makeObjects(getObj().getJSONArray("messages"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinishGlobal() {
				super.onFinishGlobal();
				updateRecyclerView();

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
		mAdapter.notifyItemInserted(mMessages.size()-1);
		scrollToBottom();
	}

	private Emitter.Listener onNewMessage = new Emitter.Listener(){
		@Override
		public void call(final Object... args){
			runOnUiThread(new Runnable(){
				@Override
				public void run(){
					Log.d(getClass().getSimpleName(), "Socket IO Data");
					JSONObject data = (JSONObject) args[0];
					ECMessage message;

					try {
						message = new ECMessage(data);
					} catch (JSONException | ParseException e) {
						e.printStackTrace();
						return;
					}
					addMessage(message);
				}
			});
		}
	};
	private Emitter.Listener onConnect = new Emitter.Listener(){
		@Override
		public void call(final Object... args){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					Log.d("Socket IO", "There is data");
				}
			});
		}
	};


	/**
	 * Private method to send message. Make sure to make the HTTP/Socket calls here.
	 */
	private void attemptSend() {

		mSocket.emit("foo", "hi");

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
		final ECApiManager.SendMessageObject sendMessageObject = new ECApiManager.SendMessageObject(params) {
			@Override
			public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
				super.onSuccessGlobal(statusCode, headers, responseBody);

			}

			@Override
			public void onFinishGlobal() {
				super.onFinishGlobal();
				try {
					addMessage(new ECMessage(super.getObj().getJSONObject("message")));
				} catch (JSONException | ParseException e) {
					e.printStackTrace();
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
