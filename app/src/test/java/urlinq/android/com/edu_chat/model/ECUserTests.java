package urlinq.android.com.edu_chat.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Kai on 10/1/2015.
 */
public class ECUserTests {
    private final String successfulLogin = "{\"token\": \"5cd44aad6bbc4e6644a0f8218f3f7c9a\", \"user\": {\"status\": \"unverified\", \"available\": null, \"school\": {\"twitter_link\": \"http://www.twitter.com/nyustern\", \"school_location\": \"44 West 4th Street, New York, NY 10012\", \"university\": 1, \"weblink\": \"http://www.stern.nyu.edu/\", \"school_description\": \"New York University Stern School of Business, located in the heart of Greenwich Village, is one of the nation\\u2019s premier management education schools and research centers. NYU Stern offers a broad portfolio of academic programs at the graduate and undergraduate levels, all of them informed and enriched by the dynamism, energy and deep resources of the world\\u2019s business capital.\", \"founded\": \"1900\", \"alias\": \"Stern\", \"cover_file\": 2776, \"fb_link\": \"https://www.facebook.com/nyustern\", \"id\": 7, \"school_name\": \"Leonard N. Stern School of Business\", \"picture_file\": 2776}, \"firstname\": \"Kai\", \"picture_file\": {\"file_source\": \"regular\", \"user_id\": null, \"origin_id\": null, \"file_type\": \"jpg\", \"file_name\": \"cas.jpg\", \"file_extension\": \"jpg\", \"original_name\": \"test.jpg\", \"file_url\": \"/assets/test.jpg\", \"file_description\": \"\", \"created_timestamp\": \"2014-10-26T02:11:10\", \"download_count\": 0, \"id\": 1, \"origin_type\": null, \"size\": 0}, \"lastname\": \"Mou\", \"show_planner_tutorial\": null, \"user_type\": \"s\", \"last_email\": \"2015-09-24T18:17:19\", \"last_activity\": \"2015-09-22T17:29:05\", \"show_profile_tutorial\": null, \"id\": 59295, \"closed_showcase_instructions\": null, \"user_bio\": \"\", \"department\": null, \"user_email\": \"km2743@nyu.edu\", \"show_edit_profile_post\": null, \"timezone_offset\": 240, \"show_fbar_tutorial\": null, \"university_id\": 1}, \"success\": \"true\"}";
    private final String failedLogin = "{\"error_id\": 3, \"error_msg\": \"Account does not exist\", \"success\": \"false\"}";
    private JSONObject successObj;
    private JSONObject failedObj;
    /**
     * Person 1 is when case is successful. Person 2 is when login is unsuccessful.
     */
	private ECUser person1;
    private ECUser person2;

    @Before
    public void jsonTest() throws JSONException, ParseException {
        successObj = new JSONObject(successfulLogin);
        ECUser.setCurrentUser(new ECUser(successObj));
        person1 = ECUser.getCurrentUser();
    }

    @Test
    public void testLoginFailed() throws JSONException, ParseException {
        failedObj = new JSONObject(failedLogin);
        ECUser.setCurrentUser(new ECUser(failedObj));
        person2 = ECUser.getCurrentUser();
        assertFalse(person2.getLoginSuccessful());
    }

    @org.junit.Test
    /**
     * Check that the Object is being formatted and created correctly.
     *
     */
    public void testUserExists() {
        assertNotNull(successObj);
    }

    /**
     * Tests the static method of getting current user.
     */
    @org.junit.Test
    public void testStaticGetnSetMethods() {
        assertNotNull(person1);
    }

    @org.junit.Test
    /**
     * Test if object is actually a json object or array
     */
    public void testJSONObjOrArray() {
        assertEquals(true, successObj instanceof JSONObject);
    }

    /**
     * Checks if token is correct.
     *
     * @throws JSONException
     */
    @org.junit.Test
    public void testTokenCorrect() throws JSONException {
        assertEquals(ECUser.getUserToken(), "5cd44aad6bbc4e6644a0f8218f3f7c9a");
    }

    @org.junit.Test
    /**
     * Checks if method retrieves first name correctly.
     */
    public void testFirstName() {
        String expectedFirst = "Kai";
        assertEquals(expectedFirst, person1.getFirstName());
    }
    @Test
    public void testLastName() {
        String expectedLast = "Mou";
        assertEquals(expectedLast, person1.getLastName());
    }
    @Test
    public void testShouldFail(){
        String expectedLast = "Pang";
        assertNotEquals(expectedLast, person1.getLastName());
    }

    @org.junit.Test
    /**
     * Checks if retrieves success boolean successfully.
     */
    public void testSuccessVariable() throws JSONException {
        boolean expectedLogin = true;
        assertEquals(expectedLogin, person1.getLoginSuccessful());

    }
}