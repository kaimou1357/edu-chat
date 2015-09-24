package urlinq.android.com.edu_chat.model;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import junit.framework.Assert;

import org.json.JSONException;

import javax.security.auth.login.LoginException;

import cz.msebera.android.httpclient.Header;
import urlinq.android.com.edu_chat.LoginActivity;
import urlinq.android.com.edu_chat.LoginBackground;
import urlinq.android.com.edu_chat.LoginFragment;
import urlinq.android.com.edu_chat.R;
import urlinq.android.com.edu_chat.manager.ECApiManager;

/**
 * Created by Jacob on 9/21/15.
 */
public class ECUserTests {
    /**
     * DO not use this class for testing of ECUser Class.
     *
     * Click Build Variants and select JUnit Testing. THis is not an Android Instrumentation Test.
     */
}
