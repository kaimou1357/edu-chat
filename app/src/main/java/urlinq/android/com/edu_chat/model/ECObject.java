package urlinq.android.com.edu_chat.model;

import android.util.Log;
import org.json.JSONObject;

/**
 * Created by Jacob on 10/27/15.
 */
public class ECObject {

	private final String objectIdentifier;
	private final String fileURL;
	private String color;

	public ECObject(String objectIdentifier, String fileURL) {
		if (objectIdentifier == null || fileURL == null) {
			Log.w("EDU.CHAT", "ECObject create with null property");
		}
		this.objectIdentifier = objectIdentifier;
		this.fileURL = fileURL;
	}

//	public ECObject buildWithJSON(JSONObject response) {
//		// TODO: Make this
//
//		return new ECObject(null, null);
//	}

	public String getFileURL() {
		return fileURL;
	}

	public String getObjectIdentifier() {
		return objectIdentifier;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
