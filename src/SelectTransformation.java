import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;

public class SelectTransformation extends Transformation {

	private static final String SELECT_TAG = "select";
	private static final String SELECT_END_TAG = "/select";
	private JSONObject json;

	public SelectTransformation(JSONObject json) {
		this.json = json;
	}

	public String transformJSOUP(Document document, String dom) {
		String select = (String) json.get(SELECT_TAG);
		String selectEnd = (String) json.get(SELECT_END_TAG);
		dom = dom.replace("<" + SELECT_TAG, "\n<" + select).replace(SELECT_END_TAG, selectEnd);
		return dom;
	}
}
