import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OptionTransformation extends Transformation {

	private final static String OPTION_TAG = "option";
	private JSONObject json;

	public OptionTransformation(JSONObject json) {
		this.json = json;
	}

	public static String transform(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		String value = (String) inArray.get("value");
		String option = (String) inArray.get("option");
		String toWrite = original;
		Document doc = Jsoup.parse(original);
		String text = doc.getElementsByTag(tag).text();
		toWrite = toWrite.replaceFirst(tag, option).replace(text, "").replaceFirst("value", value)
				.replace("</option>", "").replace(">", " itemLabel = \"" + text + "\"/>");
		return toWrite;
	}

	public String transformJSOUP(Document document, String dom) {
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
		return dom;
	}
}
