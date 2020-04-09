import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ButtonTransformation {

	private final static String BUTTON_TAG = "button";
	public static String transform(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		String toWrite = original;
		Document doc = Jsoup.parse(original);
		String text = doc.getElementsByTag(tag).text();
		toWrite = toWrite.replaceFirst(tag, "h:commandButton").replace(text, "").replaceFirst("type=", "value=\"" + text + "\" ")
				.replace("</button>", "").replace(">", "/>")
				.replace("\"submit\"", "").replace("\"button\"", "");
		return toWrite;
	}

	public static String transformJSOUP(Document document, JSONObject json, String dom, Elements html) {
		JSONArray values = (JSONArray) json.get(BUTTON_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String button = (String) inArray.get(BUTTON_TAG);//tag
		Elements tokens = document.getElementsByTag(BUTTON_TAG);
		for (Element token : tokens) {
			String original = token.toString();
			token.attr("value", token.text());
			token.removeAttr("type");
			token.text("");
			String jsfTag = token.toString().replace("</button>", "")
					.replaceFirst(BUTTON_TAG, button).replace(">", " />\n");
			dom = dom.replace(original, jsfTag);
		}
		return dom;
	}
}
