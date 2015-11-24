package urlinq.android.com.edu_chat.model;

import android.util.Log;


/**
 * Created by Jacob on 10/27/15.
 */
public class ECObject {

	private final int objectIdentifier;
	private final String fileURL;
	private final String color;

	@Override
	public String toString() {
		return "ECObject{" +
				"color='" + color + '\'' +
				", objectIdentifier=" + objectIdentifier +
				", fileURL='" + fileURL + '\'' +
				'}';
	}

	public ECObject (int objectIdentifier, String fileURL, String color) {
		this.color = color;
		this.objectIdentifier = objectIdentifier;
		if (fileURL == null) {
			Log.e(String.format("EDU.CHAT %s", getClass().getSimpleName()), "ECObject create with null property");
		}
		this.fileURL = fileURL;
		Log.v(String.format("EDU.CHAT %s", getClass().getSimpleName()), this.toString());
	}

	public String getFileURL () {
		return fileURL;
	}

	public int getObjectIdentifier () {
		return objectIdentifier;
	}

	public String getColor () {
		return color;
	}

}
