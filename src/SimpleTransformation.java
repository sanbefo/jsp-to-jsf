import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SimpleTransformation extends Transformation {
	private JSONObject json;
	private final static String BODY_TAG = "body";
	private final static String BODY_END_TAG = "/body";
	private final static String HEAD_TAG = "head";
	private final static String HEAD_END_TAG = "/head";
	private final static String LABEL_TAG = "label";
	private final static String LABEL_END_TAG = "/label";
	private final static String DIV_TAG = "div";
	private final static String DIV_END_TAG = "/div";
	private final static String SPAN_TAG = "span";
	private final static String SPAN_END_TAG = "/span";

	public SimpleTransformation(JSONObject json) {
		this.json = json;
	}

	public String transformJSOUP(Document document, String dom) {
//		JSONArray values = (JSONArray) json.get(INPUT_TAG);
//		JSONObject inArray = (JSONObject) values.get(0);
//		JSONArray array = (JSONArray) inArray.get("type");
//		JSONObject types = (JSONObject) array.get(0);
//		Elements tokens = document.getElementsByTag(INPUT_TAG);
//		for (Element token : tokens) {
//			String type = token.attr("type");
//			String tag = (String) types.get(type);
//			String original = token.toString();
//			token.removeAttr("type");
//			token.text("");
//			String jsfTag = token.toString().replaceFirst(INPUT_TAG, tag)
//					.replace("</input>", "").replace(">", " />");
//			dom = dom.replace(original, jsfTag);
//		}
		return dom;
	}
}
