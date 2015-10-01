package urlinq.android.com.edu_chat.manager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacob on 9/27/15.
 */
public class ECApiManagerTest extends ActivityUnitTestCase{
    private final String userEmail = "km2743@nyu.edu";
    private final String passWord = "adventure";
    String userHash;

    public ECApiManagerTest(Class activityClass) {
        super(activityClass);
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSetCookieStore() throws Exception {

    }

    @Test
    public void testGet() throws Exception {
    }

    @Test
    public void testPOST() throws Throwable{
        //final AsyncHttpClient httpClient = new AsyncHttpClient();
        final CountDownLatch signal = new CountDownLatch(1);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.put("email", userEmail);
                params.put("password", passWord);
                ECApiManager.post("https://edu.chat/api/login/", params, new AsyncHttpResponseHandler() {
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
            assertEquals("true", obj.getString("success"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSetCookieStore1() throws Exception {

    }

    @Test
    public void testGet1() throws Exception {

    }

    @Test
    public void testPost1() throws Exception {

    }
}