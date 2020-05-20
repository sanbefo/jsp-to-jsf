import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InputTransformation extends Transformation {

	private final static String INPUT_TAG = "input";
	private final static String TYPE_ATTR = "type";
	private JSONObject json;

	public InputTransformation(JSONObject json) {
		this.json = json;
	}

	public String transform(Document document, String dom) {
		JSONArray values = (JSONArray) json.get(INPUT_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		JSONArray array = (JSONArray) inArray.get(TYPE_ATTR);
		JSONObject types = (JSONObject) array.get(0);
		Elements tokens = document.getElementsByTag(INPUT_TAG);
		for (Element token : tokens) {
			if (token.hasAttr(TYPE_ATTR)) {
				String type = token.attr(TYPE_ATTR);
				String tag = (String) types.get(type);
				String original = token.toString();
				token.removeAttr(TYPE_ATTR);
				token.text("");
				String jsfTag = token.toString().replaceFirst(INPUT_TAG, tag)
						.replace("</input>", "").replace(">", " />");
				dom = dom.replace(original, jsfTag);
			}
		}
		return dom;
	}
}
