import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HTMLTransformation extends Transformation {

	private final static String HTML_TAG = "html";
	private final static String XMLNS_TAG = "xmlns";
	private final static String XMLNS_H_TAG = "xmlns:h";
	private boolean flag;
	private JSONObject json;

	public HTMLTransformation(JSONObject json) {
		this.json = json;
	}

	public boolean getFlag() {
	    return flag;
	}

	public void setFlag(boolean flag) {
	    this.flag = flag;
	}

	public String transform(Document document, String dom) {
		JSONArray values = (JSONArray) json.get(HTML_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String xmlns = (String) inArray.get(XMLNS_TAG);
		String xmlnsh = (String) inArray.get(XMLNS_H_TAG);
		Elements token = document.getElementsByTag(HTML_TAG);
		token.attr(XMLNS_TAG, xmlns);
		token.attr(XMLNS_H_TAG, xmlnsh);
		return token.toString();
	}
}
