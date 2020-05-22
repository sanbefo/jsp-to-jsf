import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkTransformation extends Transformation {

	private final static String LINK_TAG = "link";
	private final static String LINK_END_TAG = "</link>";
	private final static String TYPE_ATTR = "type";
	private final static String HREF_ATTR = "href";
	private final static String REL_ATTR = "rel";
	private final static String CSS_LIBRARY = "css";
	private boolean flag;
	private JSONObject json;

	public LinkTransformation(JSONObject json) {
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
		JSONArray values = (JSONArray) json.get(LINK_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String library = (String) inArray.get(TYPE_ATTR);
		String name = (String) inArray.get(HREF_ATTR);
		String tag = (String) inArray.get(LINK_TAG);
		Elements tokens = document.getElementsByTag(LINK_TAG);
		if (tokens.size() > 0) {
			setFlag(true);
			for (Element token : tokens) {
				String original = token.toString();
				String nameAttr = token.attr(HREF_ATTR);
				token.attr(library, CSS_LIBRARY);
				token.attr(name, nameAttr);
				token.removeAttr(REL_ATTR);
				token.removeAttr(TYPE_ATTR);
				token.removeAttr(HREF_ATTR);
				token.text("");
				String jsfTag = token.toString().replaceFirst(LINK_TAG, tag);
				dom = dom.replace(original, jsfTag);
			}
		}
		dom = dom.replace(LINK_END_TAG, "");
		return dom;
	}
}
