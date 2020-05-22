import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ButtonTransformation extends Transformation {

	private final static String BUTTON_TAG = "button";
	private final static String TYPE_ATTR = "type";
	private final static String VALUE_ATTR = "value";
	private boolean flag;
	private JSONObject json;

	public ButtonTransformation(JSONObject json) {
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
		JSONArray values = (JSONArray) json.get(BUTTON_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String button = (String) inArray.get(BUTTON_TAG);
		Elements tokens = document.getElementsByTag(BUTTON_TAG);
		if (tokens.size() > 0) {
			setFlag(true);
			for (Element token : tokens) {
				String original = token.toString();
				token.attr(VALUE_ATTR, token.text());
				token.removeAttr(TYPE_ATTR);
				token.text("");
				String jsfTag = token.toString().replace("</button>", "")
						.replaceFirst(BUTTON_TAG, button).replace(">", " />\n");
				dom = dom.replace(original, jsfTag);
			}
		}
		return dom;
	}

	public String notes() {
		return "-- Button Tag Notes --\n"
				+ "Check that there are no button tags in the file\n"
				+ "Check that there are no empty attributes in the h:commandButton tag\n";
	}
}
