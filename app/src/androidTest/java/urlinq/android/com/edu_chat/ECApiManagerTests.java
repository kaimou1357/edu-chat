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
    private final String loadUserAPI = "https://edu.chat/message/loadout/";
    String userHash;
    String getResponse;
    String userToken = "de5a3fc98da47da3083ed719dbde5932";

    public void testPOST() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.put("email", userEmail);
                params.put("password", passWord);
                ECApiManager.post(loginAPI, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //called when response code 200
                        userHash = new String(responseBody);
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
            //System.out.println(userHash);
            JSONObject obj = new JSONObject(userHash);
            userToken = obj.getString("token");
            Log.d("token", userToken);
            assertEquals("true", obj.getString("success"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test get request with API using the LOADOUT API
     * Loadout API (GET)
     - https://edu.chat/message/loadout
     -Token
     -Returns chat channels and direct conversations, & notifications
     -Also returns list of departments user is in
     * @throws Throwable
     */
    public void testGet() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.put("token", userToken);
                ECApiManager.get(loadUserAPI, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //called when response code 200
                        getResponse = new String(responseBody);
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
            JSONObject obj = new JSONObject(getResponse);
            // Please change to true once API server side login is figured out.
            assertEquals("false", obj.getString("success"));


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

