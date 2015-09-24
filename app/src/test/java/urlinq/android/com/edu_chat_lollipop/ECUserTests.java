package urlinq.android.com.edu_chat_lollipop;

import android.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.LoginActivity;
import urlinq.android.com.edu_chat.LoginBackground;
import urlinq.android.com.edu_chat.R;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;

import org.json.JSONException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import urlinq.android.com.edu_chat.LoginActivity;
import urlinq.android.com.edu_chat.LoginBackground;
import urlinq.android.com.edu_chat.LoginFragment;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.manager.ECApiManager;
import urlinq.android.com.edu_chat.model.ECUser;

/**
 * Created by Jacob on 9/21/15.
 *
 * Test class to test ECUser and POST/GET Requests from the API.
 */
public class ECUserTests extends InstrumentationTestCase{

    private final String userEmail = "km2743@nyu.edu";
    private final String passWord = "adventure";
    /**
     * PLEASE DON"T CHANGE THESE TWO VARIABLES OR DELETE OR TOUCH.
     */
    private final String failedLogin = "{error_id: 3, error_msg: Account does not exist, success: false}";
    private final String successfulLogin = "{token: 11d1c7496ead9d6543db662afeb11747, user: {status: unverified, available: null, school: {twitter_link: http://www.twitter.com/nyustern, school_location: 44 West 4th Street, New York, NY 10012, university: 1, weblink: http://www.stern.nyu.edu/, school_description: New York University Stern School of Business, located in the heart of Greenwich Village, is one of the nations premier management education schools and research centers. NYU Stern offers a broad portfolio of academic programs at the graduate and undergraduate levels, all of them informed and enriched by the dynamism, energy and deep resources of the world\\u2019s business capital., founded: 1900, alias: Stern, cover_file: 2776, fb_link: https://www.facebook.com/nyustern, id: 7, school_name: Leonard N. Stern School of Business, picture_file: 2776}, firstname: Kai, picture_file: {file_source: regular, user_id: null, origin_id: null, file_type: jpg, file_name: cas.jpg, file_extension: jpg, original_name: test.jpg, file_url: /assets/test.jpg, file_description: , created_timestamp: 2014-10-26T02:11:10, download_count: 0, id: 1, origin_type: null, size: 0}, lastname: Mou, show_planner_tutorial: null, user_type: s, last_email: 2015-09-22T20:27:04, last_activity: 2015-09-22T17:29:05, show_profile_tutorial: null, id: 59295, closed_showcase_instructions: null, user_bio: , department: null, user_email: km2743@nyu.edu, show_edit_profile_post: null, timezone_offset: 240, show_fbar_tutorial: null, university_id: 1}, success: true}";

    InstrumentationTestCase runnerHelper = new InstrumentationTestCase();
    final StringBuilder sb = new StringBuilder();
    final AsyncHttpClient httpClient = new AsyncHttpClient();
    ECUser person1;

    public void test() throws Exception{
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }

    @org.junit.Test
    public void testUserExists(){
        /**
         * Tests the login connection and sees if ECUser is created at all.
         */
        try{
            ECUser.setCurrentUser(new ECUser(successfulLogin));
            //person1 = new ECUser(successfulLogin);
        }catch(JSONException e){

        }


        Assert.assertNotNull(person1);



//        runTestOnUiThread(new Runnable() {

//            public void run() {
//                RequestParams params = new RequestParams();
//                params.put("email", userEmail);
//                params.put("password", passWord);
//                httpClient.post("https://edu.chat/api/login/", params, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        //called when response code 200
//                        sb.append(responseBody);
//                        try{
//                            ECUser.setCurrentUser(new ECUser(sb));
//                        }catch(JSONException e){
//                        }
//                    }
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                    }
//                });
//            }
//        });


    }

    @org.junit.Test
    public void testNames(){
        String expectedFirst = "Kai1";
        String expectedLast = "Mou";
        Log.d("personname", person1.getFirstName());

        assertEquals(expectedFirst, person1.getFirstName());
        assertEquals(expectedLast, person1.getLastName());
    }
    @org.junit.Test
    public void testSuccessVariable(){
        boolean expectedLogin = true;
        assertSame(expectedLogin, person1.getLoginSuccessful());

    }








}