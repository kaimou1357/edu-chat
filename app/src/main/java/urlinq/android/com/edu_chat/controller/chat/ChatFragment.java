package urlinq.android.com.edu_chat.controller.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.loopj.android.http.RequestParams;
import com.urlinq.edu_chat.R;
import cz.msebera.android.httpclient.Header;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import urlinq.android.com.edu_chat.controller.adapter.MessageAdapter;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.*;

import javax.net.ssl.SSLContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kai Mou on 1/12/2016.
 */
public class ChatFragment extends Fragment {
	private static final String REQUEST_LENGTH = "30";
	@Bind(R.id.messages) RecyclerView mMessagesView;
	@Bind(R.id.message_input) EditText mInputMessageView;
	@Bind(R.id.send_button) Button sendButton;
	@Bind(R.id.chatToolBar) Toolbar toolbar;
	@Bind(R.id.subChatSpinner) Spinner subChatSpinner;
	@Bind(R.id.isTypingTextView)TextView isTypingTextView;

	private final List<Object> mMessages = new LinkedList<>();
	private MessageAdapter mAdapter;
	private String target_type;
	private String target_id;
	private String subchannel_id;
	private String chatTitle;
	private ArrayList<ECSubchat> subchats;
	private boolean isSubChannel = false;
	private boolean isUserChat = false;
	private SocketIO socket;
	private ECObject passedObject;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ChatFragment", "onCreate Called");
		Bundle b = this.getArguments();
		passedObject = b.getParcelable("PARCEL");
		if (passedObject instanceof ECCategory) {
			ECCategory cat = (ECCategory) passedObject;
			chatTitle = cat.getName();
			target_id = Integer.toString(cat.getObjectIdentifier());
			target_type = cat.getTypeOfCategory().getCategoryString();
			subchats = cat.getSubchannels();
			isUserChat = false;
			socketSetup(target_type, target_id);
		}
		if (passedObject instanceof ECUser) {
			ECUser user = (ECUser) passedObject;
			chatTitle = user.getFullName();
			target_id = Integer.toString(user.getObjectIdentifier());
			target_type = "user";
			isUserChat = true;
			socketSetup(target_type, Integer.toString(ECUser.getCurrentUser().getObjectIdentifier()));
		}


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.chat_layout, container, false);
		v.setBackgroundColor(Color.WHITE);

		ButterKnife.bind(this, v);
		Log.d("ChatFragment", "onCreateView Called");
		/**
		 * Handle Passed Object in this section of the code.
		 */
		SharedPreferences sp = getActivity().getSharedPreferences(Integer.toString(passedObject.getObjectIdentifier()), 0);

		mInputMessageView.setText(sp.getString(Integer.toString(passedObject.getObjectIdentifier()), ""));
		updateChatRoom(target_type, target_id, null, ECUser.getUserToken());
		mAdapter = new MessageAdapter(getActivity(), mMessages);
		mMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mMessagesView.setAdapter(mAdapter);
		sendButton.setVisibility(View.GONE);


		/**
		 * Handle ToolBar setup in this section of the code.
		 */
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		toolbar.inflateMenu(R.menu.chat_menu);


		ArrayList<String> nameOfSubChats = new ArrayList<>();
		//If subchats = null, no need for a drop down spinner button, so disable the spinner.
		if (subchats == null) {
			nameOfSubChats.add(chatTitle);
			ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_list, nameOfSubChats);
			subChatSpinner.setAdapter(adapter);
			subChatSpinner.setEnabled(false);
			Log.d("Subchats", "subchats is null");
		} else {
			nameOfSubChats.add(chatTitle);
			Log.d("Subchats", "subchats is not null");
			if (subchats.size() == 0) {
				subChatSpinner.setEnabled(false);
			}
			if (subchats.size() != 0) {
				for (int i = 0; i < subchats.size(); i++) {
					nameOfSubChats.add(subchats.get(i).getName());
				}
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_list, nameOfSubChats);
			//Do not modify this order. The setSelection following setAdapter makes sure Spinner doesn't fire off before user selection.
			subChatSpinner.setAdapter(adapter);
			subChatSpinner.setSelection(0, false);
			subChatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if (subchats.size() != 0) {
						if (position == 0) {
							ECCategory cat = (ECCategory) passedObject;
							mMessages.clear();
							updateChatRoom(cat.getTypeOfCategory().getCategoryString(), Integer.toString(cat.getObjectIdentifier()), null, ECUser.getUserToken());
							mAdapter.notifyDataSetChanged();
							isSubChannel = false;

						} else {
							ECSubchat chat = subchats.get(position - 1);
							socket.disconnect();
							isSubChannel = true;
							subchannel_id = Integer.toString(chat.getSubchannel_id());
							Log.d("subchat position", position + "");
							mMessages.clear();
							socketSetup(chat.getOrigin_type(), Integer.toString(chat.getOrigin_id()));
							updateChatRoom(chat.getOrigin_type(), Integer.toString(chat.getOrigin_id()), Integer.toString(chat.getSubchannel_id()), ECUser.getUserToken());
							mAdapter.notifyDataSetChanged();

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
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				sendButton.setVisibility(View.VISIBLE);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptSend();
				sendButton.setVisibility(View.GONE);
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});

		return v;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("ChatFragment", "onDestroy Called");
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(new View(getContext()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		SharedPreferences sp = getActivity().getSharedPreferences(Integer.toString(passedObject.getObjectIdentifier()), 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(Integer.toString(passedObject.getObjectIdentifier()), mInputMessageView.getText().toString());
		editor.commit();

		socket.disconnect();

	}



	/**
	 * Method to setup socket to listen on specific channel.
	 */
	private void socketSetup(final String socketTargetType, final String socketTargetID) {


		try {
			SSLContext sslContext = SSLContext.getInstance("TLS", "AndroidOpenSSL");
			sslContext.init(null, null, null);
			SocketIO.setDefaultSSLSocketFactory(sslContext);
			socket = new SocketIO("https://edu.chat/");
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
					Log.d("Socket", socketTargetType + socketTargetID);
				}

				@Override
				public void on(String event, IOAcknowledge ack, Object... args) {
					try {
						final JSONObject serverJSON = new JSONObject(args[0].toString());
						String currentChat = socketTargetType + "_" + socketTargetID;
						Log.d("socket", serverJSON.toString());

						if (event.equals(currentChat) && serverJSON.getString("type").equals("text")) {
							Log.d("socket", "First stage entered");

							final ECMessage message = new ECMessage(serverJSON);
							String subchannelText = serverJSON.getString("subchannel_id");
							Log.d("socket", subchannelText);

							if (isSubChannel) {
								if (subchannelText != null && serverJSON.getString("subchannel_id").equals(subchannel_id)) {
									getActivity().runOnUiThread(new Runnable() {
										@Override
										public void run() {
											addMessage(message);
										}
									});
									Log.d("subchannel", "Subchannel Case Handled");
								}
							} else if (subchannelText.equals("null")) {
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										addMessage(message);
									}
								});
								Log.d("subchannel", "regular case handled");
							}


						}
						/**
						 * Handle some stuff when user is typing/not typing
						 */
						if (event.equals(currentChat) && serverJSON.getString("type").equals("typing")) {

							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									String userFirstName = null;
									try{
										userFirstName = serverJSON.getJSONObject("user").getString("firstname");
									}catch(JSONException e){
										Log.e("ChatFragment", "User JSON String not found!");
									}

									isTypingTextView.setText(userFirstName + " is typing...");
								}
							});

						}
						if (event.equals(currentChat) && serverJSON.getString("type").equals("stop_typing")) {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									isTypingTextView.setText("");
								}
							});

						}

					} catch (JSONException | ParseException e) {
						e.printStackTrace();
					}

				}
			});
		} catch (Exception e) {
			Log.d("Socket", e.getMessage());
		}


	}


	private void updateChatRoom(String targetType, String targetID, String subchannel_id, String token) {
		RequestParams params = new RequestParams();
		if (isSubChannel) {
			params.add("subchannel_id", subchannel_id);
		}

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
					//Insert date objects into the message list here.
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinishGlobal() {
				super.onFinishGlobal();

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

	private void makeObjects(JSONArray obj) {


		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

			//adds the first message date before the message is added.
			for (int i = 1; i < obj.length(); i++) {
				JSONObject singleMessage = obj.getJSONObject(i);
				mMessages.add(new ECMessage(singleMessage));
			}

			for (int i = 1; i < mMessages.size(); i += 2) {
				ECMessage firstMessage = (ECMessage) mMessages.get(i - 1);
				ECMessage secondMessage = (ECMessage) mMessages.get(i);

				if (!fmt.format(firstMessage.getMessageDate()).equals(fmt.format(secondMessage.getMessageDate()))) {
					mMessages.add(i, secondMessage.getMessageDate());
				}

			}
			//don't forget to add in the first date at the very beginning.

			if (mMessages.size() != 0) {
				ECMessage first = (ECMessage) mMessages.get(0);
				mMessages.add(0, first.getMessageDate());
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
		if (isSubChannel) {
			params.add("subchannel_id", subchannel_id);
		}
		params.add("token", ECUser.getUserToken());
		final ECApiManager.SendMessageObject sendMessageObject = new ECApiManager.SendMessageObject(params) {
			@Override
			public void onSuccessGlobal(int statusCode, Header[] headers, byte[] responseBody) {
				super.onSuccessGlobal(statusCode, headers, responseBody);
			}

			@Override
			public void onFinishGlobal() {
				super.onFinishGlobal();
				if (isUserChat) {
					try {
						addMessage(new ECMessage(super.getObj().getJSONObject("message")));
						Log.d("message debug", super.getObj().getJSONObject("message").toString());
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
	private void addMessage(ECMessage message) {
		if (mMessages.size() != 0) {
			ECMessage lastMessage = (ECMessage) mMessages.get(mMessages.size() - 1);
			//TODO Should really write a comparator for ECMessage Object...
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

			if (!fmt.format(message.getMessageDate()).equals(fmt.format(lastMessage.getMessageDate()))) {
				mMessages.add(mMessages.size(), message.getMessageDate());
			}
		}


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
