import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
		Elements tokens = document.getElementsByTag(SELECT_TAG);
		for (Element token : tokens) {
			String original = token.toString();
			String jsfTag = "\n";
			jsfTag += token.toString().replaceFirst(SELECT_TAG, select);
			dom = dom.replace(original, jsfTag);
		}
		dom = dom.replace(SELECT_END_TAG, selectEnd);
		return dom;
	}
}
