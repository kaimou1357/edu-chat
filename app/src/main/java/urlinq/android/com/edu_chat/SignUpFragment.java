package urlinq.android.com.edu_chat;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.manager.HttpRequest;

/**
 * Created by Kai on 9/6/2015.
 * This Fragment handles all the information associated with the different layouts between the signin form and the login form.
 */
public class SignUpFragment extends Fragment {

    private ImageButton signUpBtn;
    private ImageButton logInBtn;
    private ImageButton logInBlue;
    private EditText userEmail;
    private EditText userPass;
    private View v;
    private ViewFlipper flipper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.combined_login, container, false);
        signUpBtn = (ImageButton) v.findViewById(R.id.signUpToggle);
        logInBlue = (ImageButton) v.findViewById(R.id.logInBlue);
        flipper = (ViewFlipper) v.findViewById(R.id.viewFlipper);
        logInBtn = (ImageButton) v.findViewById(R.id.logInToggleBtn);

        userEmail = (EditText) v.findViewById(R.id.emailTextView);
        userPass = (EditText) v.findViewById(R.id.passwordTextView);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //load next layout on button click.
                flipper.showNext();
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //load next layout on button click.
                flipper.showNext();
            }
        });
        logInBlue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                RequestParams params = new RequestParams();
                params.put("email", userEmail.getText().toString());
                params.put("password", userPass.getText().toString());

                HttpRequest.post("https://edu.chat/api/login/", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("login", responseBody.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            }
        });

        return v;
    }


}