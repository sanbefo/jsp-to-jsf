import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class OptionTransformation {

	public static String transform(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		String value = (String) inArray.get("value");
		String option = (String) inArray.get("option");
		String toWrite = original;
		Document doc = Jsoup.parse(original);
		String text = doc.getElementsByTag("option").text();
		toWrite = toWrite.replaceFirst("option", option).replace(text, "").replaceFirst("value", value)
				.replace("</option>", "").replace(">", " itemLabel = \"" + text + "\"/>");
		return toWrite;
	}
}
