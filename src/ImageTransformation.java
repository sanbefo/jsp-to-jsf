import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ImageTransformation {

	public static String transform(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		String toWrite = original;
		for (Object key : inArray.keySet()) {
			toWrite = toWrite.replaceFirst(key.toString(), inArray.get(key).toString());
		}
		return toWrite;
	}
}
