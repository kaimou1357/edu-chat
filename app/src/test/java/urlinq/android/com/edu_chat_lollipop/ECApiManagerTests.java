package urlinq.android.com.edu_chat_lollipop;

import android.test.InstrumentationTestCase;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.junit.Test;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.manager.ECApiManager;

/**
 * Created by Jacob on 9/24/15.
 */
public class ECApiManagerTests {
    InstrumentationTestCase runnerHelper = new InstrumentationTestCase();
    private final String userEmail = "km2743@nyu.edu";
    private final String passWord = "adventure";


    @Test
    public void testPOST() throws Throwable{

        runnerHelper.runTestOnUiThread(new Runnable() {

            public void run() {

                RequestParams params = new RequestParams();
                params.put("email", userEmail);
                params.put("password", passWord);
                ECApiManager.post("https://edu.chat/api/login/", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        //userData = new String(responseBody);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }


                });
            }
        });
    }
}
