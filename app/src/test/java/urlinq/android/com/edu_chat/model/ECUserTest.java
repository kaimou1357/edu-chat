package urlinq.android.com.edu_chat.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Jacob on 9/21/15.
 * <p/>
 * Test class to test ECUser and POST/GET Requests from the API.
 */
public class ECUserTest {

    private final String successfulLogin = "{\"token\": \"5cd44aad6bbc4e6644a0f8218f3f7c9a\", \"user\": {\"status\": \"unverified\", \"available\": null, \"school\": {\"twitter_link\": \"http://www.twitter.com/nyustern\", \"school_location\": \"44 West 4th Street, New York, NY 10012\", \"university\": 1, \"weblink\": \"http://www.stern.nyu.edu/\", \"school_description\": \"New York University Stern School of Business, located in the heart of Greenwich Village, is one of the nation\\u2019s premier management education schools and research centers. NYU Stern offers a broad portfolio of academic programs at the graduate and undergraduate levels, all of them informed and enriched by the dynamism, energy and deep resources of the world\\u2019s business capital.\", \"founded\": \"1900\", \"alias\": \"Stern\", \"cover_file\": 2776, \"fb_link\": \"https://www.facebook.com/nyustern\", \"id\": 7, \"school_name\": \"Leonard N. Stern School of Business\", \"picture_file\": 2776}, \"firstname\": \"Kai\", \"picture_file\": {\"file_source\": \"regular\", \"user_id\": null, \"origin_id\": null, \"file_type\": \"jpg\", \"file_name\": \"cas.jpg\", \"file_extension\": \"jpg\", \"original_name\": \"test.jpg\", \"file_url\": \"/assets/test.jpg\", \"file_description\": \"\", \"created_timestamp\": \"2014-10-26T02:11:10\", \"download_count\": 0, \"id\": 1, \"origin_type\": null, \"size\": 0}, \"lastname\": \"Mou\", \"show_planner_tutorial\": null, \"user_type\": \"s\", \"last_email\": \"2015-09-24T18:17:19\", \"last_activity\": \"2015-09-22T17:29:05\", \"show_profile_tutorial\": null, \"id\": 59295, \"closed_showcase_instructions\": null, \"user_bio\": \"\", \"department\": null, \"user_email\": \"km2743@nyu.edu\", \"show_edit_profile_post\": null, \"timezone_offset\": 240, \"show_fbar_tutorial\": null, \"university_id\": 1}, \"success\": \"true\"}";
    JSONObject obj;
    ECUser person1;

    @Before
    public void setUp() throws JSONException {
        obj = new JSONObject(successfulLogin);
        //obj = new JSONObject(successfulLogin);
        ECUser.setCurrentUser(new ECUser(obj));
        person1 = ECUser.getCurrentUser();
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * Check that the Object is being formatted and created correctly.
     */
    @Test
    public void testUserExists() {
        assertNotNull(obj);
    }

    /**
     * Tests the static method of getting current user.
     */
    @Test
    public void testStaticGetnSetMethods() {
        assertNotNull(person1);
    }

    /**
     * Test if object is actually a json object or array
     */
    @Test
    public void testJSONObjOrArray() {
        assertEquals(true, obj instanceof JSONObject);
    }

    /**
     * Checks if token is correct.
     *
     * @throws JSONException
     */
    @Test
    public void testTokenCorrect() throws JSONException {
        assertEquals(person1.getUserToken(), "5cd44aad6bbc4e6644a0f8218f3f7c9a");
    }


    /**
     * Checks if method retrieves first name correctly.
     */
    @Test
    public void testFirstName() {
        String expectedFirst = "Kai";
        assertEquals(expectedFirst, person1.getFirstName());
    }

    @Test
    public void testLastName() {
        String expectedLast = "Mou";
        assertEquals(expectedLast, person1.getLastName());
    }


    /**
     * Checks if retrieves success boolean successfully.
     */
    @Test
    public void testSuccessVariable() throws JSONException {
        boolean expectedLogin = true;
        assertEquals(expectedLogin, person1.getLoginSuccessful());

    }

    @Test
    public void testSetCurrentUser() throws Exception {

    }

    @Test
    public void testRefreshCurrentUser() throws Exception {

    }

    @Test
    public void testGetCurrentUser() throws Exception {

    }

    @Test
    public void testGetLastName() throws Exception {

    }

    @Test
    public void testGetFirstName() throws Exception {

    }

    @Test
    public void testGetUserToken() throws Exception {

    }

    @Test
    public void testGetLoginSuccessful() throws Exception {

    }
}



