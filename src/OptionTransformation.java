import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OptionTransformation extends Transformation {

	private final static String OPTION_TAG = "option";
	private JSONObject json;

	public OptionTransformation(JSONObject json) {
		this.json = json;
	}

	public String transform(Document document, String dom) {
		JSONArray values = (JSONArray) json.get(OPTION_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String value = (String) inArray.get("value");
		String option = (String) inArray.get(OPTION_TAG);//tag
		Elements tokens = document.getElementsByTag(OPTION_TAG);
		for (Element token : tokens) {
			String original = token.toString();
			token.attr(value, token.attr("value"));
			token.removeAttr("value");
			token.attr("itemLabel", token.text());
			token.text("");
			String jsfTag = "\n";
			jsfTag += token.toString().replaceFirst(OPTION_TAG, option).replace("</option>", "\n").replace(">", "/>");
			dom = dom.replace(original, jsfTag);
		}
		dom = dom.replaceAll("(?m)^[ \t]*\r?\n", "");
		return dom;
	}
}
