import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OptionTransformation extends Transformation {

	private final static String OPTION_TAG = "option";
	private final static String VAULE_ATTR = "value";
	private final static String ITEM_ATTR = "itemLabel";
	private boolean flag;
	private JSONObject json;

	public OptionTransformation(JSONObject json) {
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
		JSONArray values = (JSONArray) json.get(OPTION_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String value = (String) inArray.get(VAULE_ATTR);
		String tag = (String) inArray.get(OPTION_TAG);
		Elements tokens = document.getElementsByTag(OPTION_TAG);
		if (tokens.size() > 0) {
			setFlag(true);
			for (Element token : tokens) {
				String original = token.toString();
				token.attr(value, token.attr(VAULE_ATTR));
				token.removeAttr(VAULE_ATTR);
				token.attr(ITEM_ATTR, token.text());
				token.text("");
				String jsfTag = "\n";
				jsfTag += token.toString().replaceFirst(OPTION_TAG, tag).replace("</option>", "\n").replace(">", "/>");
				dom = dom.replace(original, jsfTag);
			}
		}
		dom = dom.replaceAll("(?m)^[ \t]*\r?\n", "");
		return dom;
	}

	public String notes() {
		return "-- Option Tag Notes --\n"
				+ "Check that there are no option tags in the file\n"
				+ "Check that there are no empty attributes in the f:selectItem tag\n";
	}
}
