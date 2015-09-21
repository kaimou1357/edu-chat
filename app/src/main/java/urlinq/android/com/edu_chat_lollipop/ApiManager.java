package urlinq.android.com.edu_chat_lollipop;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

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

/**
 * Created by Kai on 9/18/2015.
 */
public class ApiManager extends AsyncTask<String, Void, String> {

    public static int TASK_STATUS = 0;
    private String result;

    //set 0 for login
    //set 1 for signup
    //set 2 for refreshUserdata.
    @Override
    protected String doInBackground(String[] parameters) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://www.google.com", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
        
        result = "";
        try {
            Map<String, Object> params = new LinkedHashMap<String, Object>();


            if (TASK_STATUS == 0) {
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
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                urlConnection.getOutputStream().write(postDataBytes);
                Reader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                for (int c = in.read(); c != -1; c = in.read()) {
                    result += String.valueOf((char) c);
                }
            } else if (TASK_STATUS == 2) {
                /**
                 * Request to update user information.
                 */
                params.put("token", parameters[0]);
                params.put("id", parameters[1]);
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                URL urlLogin = new URL("https://edu.chat/api/user/");
                HttpURLConnection urlConnection = (HttpURLConnection) urlLogin.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                urlConnection.getOutputStream().write(postDataBytes);
                Reader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                for (int c = in.read(); c != -1; c = in.read()) {
                    result += String.valueOf((char) c);
                }
            }
            /**
             * Debugging purposes.
             */

            //Log.d("login", result);
        } catch (MalformedURLException e) {
            Log.d("url", "Login URL doesn't work!");
        } catch (IOException e) {

        }
        return result;
    }

    protected void onPostExecute(String result) {

    }

}