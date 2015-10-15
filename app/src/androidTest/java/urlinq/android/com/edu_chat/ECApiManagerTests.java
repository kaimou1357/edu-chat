package urlinq.android.com.edu_chat;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.client.protocol.ClientContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;
import cz.msebera.android.httpclient.protocol.HttpContext;
import urlinq.android.com.edu_chat.manager.ECApiManager;

/**
 * Created by Kai on 9/30/2015.
 */
public class ECApiManagerTests extends InstrumentationTestCase {
    private final String userEmail = "km2743@nyu.edu";
    private final String passWord = "adventure";

    private final String loginAPI = "https://edu.chat/api/login/";
    private final String refreshUserAPI = "https://edu.chat/api/user";
    static String postUserHash;
    static String getUserHash;
    String userToken;
    String userID;
    String getResponse;
    String postResponse;



    public void testPOSTnGET() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.put("email", userEmail);
                params.put("password", passWord);
                ECApiManager.post(Constants.loginAPI, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //called when response code 200
                        postUserHash = new String(responseBody);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        Log.d("login", "call failed");
                    }

                    @Override
                    public void onFinish() {
                        signal.countDown();
                        Log.d("login", "finished");
                    }
                });
            }
        });


        try {
            signal.await(5, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject obj = new JSONObject(postUserHash);
            userToken = obj.getString("token");
            userID = obj.getJSONObject("user").getString("id");
            assertEquals("true", obj.getString("success"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * Now try retrieving user data with a GET Request with the token retrieved from the POST request.
         */
        final CountDownLatch getSignal = new CountDownLatch(1);

        runTestOnUiThread(new Runnable(){
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.put("token", userToken);
                params.put("user_id", userID);
                ECApiManager.get(Constants.refreshUserAPI, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //called when response code 200
                        getUserHash = new String(responseBody);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }

                    @Override
                    public void onFinish() {
                        getSignal.countDown();
                    }
                });
            }

        });

        try {
            getSignal.await(5, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONObject getObj = new JSONObject(getUserHash);
            assertEquals("true", getObj.getString("success"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void testSetCookieStore() throws Throwable {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getInstrumentation().getContext());
        ECApiManager.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie("id", "17882");
        myCookieStore.addCookie(newCookie);

        assertNotNull(myCookieStore);

    }

}

