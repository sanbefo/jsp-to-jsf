import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InputTransformation extends Transformation {

	private final static String INPUT_TAG = "input";

	public static String transform(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		String line = original;
		String toWrite = original;
		Pattern pattern = Pattern.compile("type=*\"(.*?)\"");
		Matcher matcher = pattern.matcher(line);
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

	public String transformJSOUP(Document document, JSONObject json, String dom, Elements html) {
		JSONArray values = (JSONArray) json.get(INPUT_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		JSONArray array = (JSONArray) inArray.get("type");
		JSONObject types = (JSONObject) array.get(0);
		Elements tokens = document.getElementsByTag(INPUT_TAG);
		for (Element token : tokens) {
			String type = token.attr("type");
			String tag = (String) types.get(type);
			String original = token.toString();
			token.removeAttr("type");
			token.text("");
			String jsfTag = token.toString().replaceFirst(INPUT_TAG, tag)
					.replace("</input>", "").replace(">", " />");
			dom = dom.replace(original, jsfTag);
		}
		return dom;
	}
}
