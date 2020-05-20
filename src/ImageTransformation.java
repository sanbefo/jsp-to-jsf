import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageTransformation extends Transformation {

	private final static String IMG_TAG = "img";
	private final static String SRC_ATTR = "src";
	private JSONObject json;

	public ImageTransformation(JSONObject json) {
		this.json = json;
	}

	public String transform(Document document, String dom) {
		JSONArray values = (JSONArray) json.get(IMG_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String value = (String) inArray.get(SRC_ATTR);
		String tag = (String) inArray.get(IMG_TAG);
		Elements tokens = document.getElementsByTag(IMG_TAG);
		for (Element token : tokens) {
			String original = token.toString();
			token.attr(value, token.attr(SRC_ATTR));
			token.removeAttr(SRC_ATTR);
			String jsfTag = token.toString().replaceFirst(IMG_TAG, tag).replace(">", "/>\n");
			dom = dom.replace(original, jsfTag);
		}
		return dom;
	}
}
