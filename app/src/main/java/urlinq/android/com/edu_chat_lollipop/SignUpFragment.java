package urlinq.android.com.edu_chat_lollipop;

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

                String[] loginInfo = new String[2];
                loginInfo[0] = userEmail.getText().toString();
                loginInfo[1] = userPass.getText().toString();
                ApiManager task = new ApiManager();
                task.execute(loginInfo);
            }
        });

        return v;
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String[] parameters) {
            try {
                Map<String, Object> params = new LinkedHashMap<String, Object>();
                params.put("email", parameters[0]);
                params.put("password", parameters[1]);
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                URL urlLogin = new URL("https://edu.chat/api/login/");
                HttpURLConnection urlConnection = (HttpURLConnection) urlLogin.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                urlConnection.getOutputStream().write(postDataBytes);

                Reader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String result = "";
                /**
                 * Debugging purposes.
                 */
                for (int c = in.read(); c != -1; c = in.read()) {
                    result += String.valueOf((char) c);
                }
                //Log.d("login", result);
            } catch (MalformedURLException e) {
                Log.d("url", "Login URL doesn't work!");
            } catch (IOException e) {


            }

    return null;
        }
    }
}