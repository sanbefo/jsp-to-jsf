import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class InputTransformation {

	public static String transform(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		String line = original;
		String toWrite = original;
		Pattern pattern = Pattern.compile("type=*\"(.*?)\"");
		Matcher matcher = pattern.matcher(line);
		System.out.println(line);
		if (matcher.find())
		{
			String toRemove = matcher.group(0);
			String inputType = matcher.group(1);
			toWrite = original.replace(toRemove + " ", "");
			JSONArray array = (JSONArray) inArray.get("type");
			JSONObject types = (JSONObject) array.get(0);
			String type = (String) types.get(inputType);
			inputType = (String) inArray.get(inputType);
			toWrite = toWrite.replaceFirst(tag, type);
		}
		return toWrite;
	}
}
