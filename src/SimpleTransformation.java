import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;

public class SimpleTransformation extends Transformation {

	private JSONObject json;
	private boolean flag;
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
	private final static String SELECT_TAG = "select";
	private final static String SELECT_END_TAG = "/select";
	private final static String[] PROPERTIES = { BODY_TAG, BODY_END_TAG, HEAD_TAG,
			HEAD_END_TAG, LABEL_TAG, LABEL_END_TAG, DIV_TAG, DIV_END_TAG, SPAN_TAG,
			SPAN_END_TAG, SELECT_TAG, SELECT_END_TAG };

	public SimpleTransformation(JSONObject json) {
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
		for (String property : PROPERTIES) {
			String tag = (String) json.get(property);
			dom = dom.replace("<" + property, "\n<" + tag);
		}
		return dom.replaceAll("(?m)^[ \t]*\r?\n", "");
	}
}
