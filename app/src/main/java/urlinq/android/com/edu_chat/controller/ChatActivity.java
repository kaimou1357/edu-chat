package urlinq.android.com.edu_chat.controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.controller.adapter.MessageAdapter;
import urlinq.android.com.edu_chat.model.ECMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kai on 10/26/2015.
 */
public class ChatActivity extends AppCompatActivity {
	private static final int REQUEST_LOGIN = 0;
	private static final int TYPING_TIMER_LENGTH = 600;

	@Bind(R.id.messages) RecyclerView mMessagesView;
	@Bind(R.id.message_input) EditText mInputMessageView;
	@Bind(R.id.send_button) ImageButton sendButton;
	@Bind(R.id.nameTextView) TextView nameTextView;
	private Handler mTypingHandler = new Handler();
	private boolean mTyping = false;
	//Don't forget to set the username to something before we begin.
	private String mUsername;
	private List<ECMessage> mMessages = new ArrayList<ECMessage>();
	private RecyclerView.Adapter mAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_fragment);
		ButterKnife.bind(this);
		mAdapter = new MessageAdapter(this, mMessages);
		//get extras from bundle.

		String user_name = getIntent().getExtras().getString("USER_NAME");
		nameTextView.setText(user_name);


		mMessagesView.setLayoutManager(new LinearLayoutManager(this));
		mMessagesView.setAdapter(mAdapter);

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


	/**
	 * Private method to send message. Make sure to make the HTTP/Socket calls here.
	 */
	private void attemptSend() {
		if (null == mUsername) return;

		mTyping = false;

		String message = mInputMessageView.getText().toString().trim();
		if (TextUtils.isEmpty(message)) {
			mInputMessageView.requestFocus();
			return;
		}

		mInputMessageView.setText("");
		addMessage(mUsername, message);

	}

	/**
	 * Adds message to the recyclerview.
	 *
	 * @param username
	 * @param message
	 */
	private void addMessage(String username, String message) {
		//Add the message to the RecyclerView so the user can see it.
//        mMessages.add(new ECMessage.Builder(ECMessage.TYPE_MESSAGE)
//                .username(username).message(message).build());
//        mAdapter.notifyItemInserted(mMessages.size() - 1);
//        scrollToBottom();
	}

	/**
	 * Method to scroll to the last position in an adapter.
	 */
	private void scrollToBottom() {
		mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
	}

	private Runnable onTypingTimeOut = new Runnable() {
		@Override
		public void run() {
			if (!mTyping) return;
			mTyping = false;
		}
	};
}