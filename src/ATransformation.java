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

	public String transform(Document document, String dom) {
		JSONArray values = (JSONArray) json.get(A_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String value = (String) inArray.get("href");
		String tag = (String) inArray.get(A_TAG);
		Elements tokens = document.getElementsByTag(A_TAG);
		for (Element token : tokens) {
			String original = token.toString();
			String href = token.attr("href").contains("<%=") ? token.attr("href")
				.substring(token.attr("href").indexOf("<%=") + 3, token.attr("href").indexOf(" %>")).trim() : token.attr("href");
			token.attr(value, href);
			token.attr("outcome", token.text());
			token.removeAttr("href");
			token.text("");
			String jsfTag = token.toString().replaceFirst(A_TAG, tag).replace("</a>", "");
			dom = dom.replace(original, jsfTag);
		}
		return dom;
	}
}
