import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HTMLTransformation extends Transformation {

	private final static String HTML_TAG = "html";
	private JSONObject json;

	public HTMLTransformation(JSONObject json) {
		this.json = json;
	}

	public String transformJSOUP(Document document, String dom) {
		JSONArray values = (JSONArray) json.get(HTML_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String xmlns = (String) inArray.get("xmlns");
		String xmlnsh = (String) inArray.get("xmlns:h");
//==========================
		Elements token = document.getElementsByTag(HTML_TAG);
		token.attr("xmlns", xmlns);
		token.attr("xmlns:h", xmlnsh);
		return token.toString();
	}
}
