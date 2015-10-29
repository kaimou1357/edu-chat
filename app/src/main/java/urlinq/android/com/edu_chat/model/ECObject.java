package urlinq.android.com.edu_chat.model;

import org.json.JSONObject;

/**
 * Created by Jacob on 10/27/15.
 */
public class ECObject {

	private final String objectIdentifier;
	private final String fileURL;
	// TODO Field for Color

	public ECObject(String objectIdentifier, String fileURL) {
		this.objectIdentifier = objectIdentifier;
		this.fileURL = fileURL;
	}

	public ECObject buildWithJSON(JSONObject response) {
		// TODO: Make this
		return new ECObject(null, fileURL);
	}

	// TODO: Get Color Method
	// TODO: Get Type Method


	public String getFileURL() {
		return fileURL;
	}

	public String getObjectIdentifier() {
		return objectIdentifier;
	}
}
