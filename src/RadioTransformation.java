import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RadioTransformation extends Transformation {

	private final static String INPUT_TAG = "input";
	private final static String RADIO_ATTR = "radio";
//	private final static String SELECT_TAG = "select";
//	private final static String SELECT_END_TAG = "/select";
	private JSONObject json;

	public RadioTransformation(JSONObject json) {
		this.json = json;
	}

	public String transformJSOUP(Document document, String dom) {
		Elements radios = document.getElementsByAttributeValue("type", RADIO_ATTR);
		for (Element radio : radios) {
			JSONArray values = (JSONArray) json.get(RADIO_ATTR);
			JSONObject inArray = (JSONObject) values.get(0);
			String value = (String) inArray.get("value");
			String radioTag = (String) inArray.get(RADIO_ATTR);
			String label = (String) inArray.get("label");
			String original = radio.toString();
			String labelId = radio.attr("id");
			Elements labelTag = document.getElementsByAttributeValue("for", labelId);
			dom = dom.replace(labelTag.toString(), "");
			radio.attr(label, labelTag.text());
			radio.attr(value, radio.attr("value"));
			radio.removeAttr("id");
			radio.removeAttr("type");
			radio.removeAttr("value");
			radio.removeAttr("name");
			String jsfTag = radio.toString().replaceFirst(INPUT_TAG, radioTag).replace(">", " />");
			dom = dom.replace(original, jsfTag);
		}
		return dom;
	}
}
