import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ATransformation extends Transformation {
	
	private static final String A_TAG = "a";
	private JSONObject json;

	public ATransformation(JSONObject json) {
		this.json = json;
	}

	public static String transform(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		String toWrite = original;
		for (Object key : inArray.keySet()) {
			toWrite = toWrite.replaceFirst("href", "value").replaceFirst(key.toString(), inArray.get(key).toString());
		}
		return toWrite;
	}

	public String transformJSOUP(Document document, String dom) {
		JSONArray values = (JSONArray) json.get(A_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String value = (String) inArray.get("href");
		String img = (String) inArray.get(A_TAG);//tag
		Elements tokens = document.getElementsByTag(A_TAG);
		for (Element token : tokens) {
			String original = token.toString();
			token.attr(value, token.attr("href"));
			token.attr("outcome", token.text());
			token.removeAttr("href");
			token.text("");
			String jsfTag = token.toString().replaceFirst(A_TAG, img).replace("</a>", "");
			dom = dom.replace(original, jsfTag);
		}
		return dom;
	}
}
