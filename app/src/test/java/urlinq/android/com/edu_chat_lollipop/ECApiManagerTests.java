package urlinq.android.com.edu_chat_lollipop;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.manager.ECApiManager;

/**
 * Created by Jacob on 9/24/15.
 */
public class ECApiManagerTests {
    InstrumentationTestCase runnerHelper = new InstrumentationTestCase();
    private final String userEmail = "km2743@nyu.edu";
    private final String passWord = "adventure";
    String userHash;


    @Test
    public void testPOST()throws Throwable{
        final AsyncHttpClient httpClient = new AsyncHttpClient();
        final CountDownLatch signal = new CountDownLatch(1);
        runnerHelper.runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.put("email", userEmail);
                params.put("password", passWord);
                httpClient.post("https://edu.chat/api/login/", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //called when response code 200
                        userHash = new String(responseBody);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                    @Override
                    public void onFinish(){
                        signal.countDown();
                    }
                });


            }
        });
        try {
            signal.await(30, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(userHash);
            JSONObject obj = new JSONObject(userHash);
            Assert.assertEquals("true", obj.getString("success"));

        } catch (JSONException e) {
            e.printStackTrace();
        }





    }
}
