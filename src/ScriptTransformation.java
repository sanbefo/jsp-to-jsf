import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScriptTransformation extends Transformation {

	private final static String SCRIPT_TAG = "script";
	private final static String SCRIPT_END_TAG = "</script>";
	private final static String TYPE_ATTR = "type";
	private final static String SRC_ATTR = "src";
	private final static String JS_LIBRARY = "js";
	private boolean flag;
	private JSONObject json;

	public ScriptTransformation(JSONObject json) {
		this.json = json;
		this.flag = false;
	}

	public boolean getFlag() {
	    return flag;
	}

	public void setFlag(boolean flag) {
	    this.flag = flag;
	}

	public String transform(Document document, String dom) {
		JSONArray values = (JSONArray) json.get(SCRIPT_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String library = (String) inArray.get(TYPE_ATTR);
		String name = (String) inArray.get(SRC_ATTR);
		String tag = (String) inArray.get(SCRIPT_TAG);
		Elements tokens = document.getElementsByTag(SCRIPT_TAG);
		if (tokens.size() > 0) {
			setFlag(true);
			for (Element token : tokens) {
				String nameAttr = token.attr(SRC_ATTR);
				String original = token.toString().replace(SCRIPT_END_TAG, "");
				token.attr(library, JS_LIBRARY);
				token.attr(name, nameAttr);
				token.removeAttr(TYPE_ATTR);
				token.removeAttr(SRC_ATTR);
				token.text("");
				String jsfTag = token.toString().replaceFirst(SCRIPT_TAG, tag)
						.replace(SCRIPT_END_TAG, "").replace(">", " />");
				dom = dom.replace(original, jsfTag);
			}
		}
		dom = dom.replace(SCRIPT_END_TAG, "");
		return dom;
	}
}
