import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkTransformation extends Transformation {

	private final static String LINK_TAG = "link";
	private JSONObject json;

	public LinkTransformation(JSONObject json) {
		this.json = json;
	}

	public String transformJSOUP(Document document, String dom) {
		JSONArray values = (JSONArray) json.get(LINK_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String library = (String) inArray.get("type");
		String name = (String) inArray.get("href");
		String tag = (String) inArray.get(LINK_TAG);
		Elements tokens = document.getElementsByTag(LINK_TAG);
		for (Element token : tokens) {
			String original = token.toString();
			String nameAttr = token.attr("href");
			token.attr(library, "css");
			token.attr(name, nameAttr);
			token.removeAttr("rel");
			token.removeAttr("type");
			token.removeAttr("href");
			token.text("");
			String jsfTag = token.toString().replaceFirst(LINK_TAG, tag);
			dom = dom.replace(original, jsfTag);
		}
		dom = dom.replace("</link>", "");
		return dom;
	}
}
