import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ButtonTransformation {
//	<h:commandButton value = "Click Me!" onclick = "alert('Hello World!');" />
//	<button type="button">Click Me!</button>

	public static String transform(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		String toWrite = original;
		Document doc = Jsoup.parse(original);
		String text = doc.getElementsByTag("button").text();
		System.out.println(text);
		toWrite = toWrite.replaceFirst("button", "h:commandButton").replace(text, "").replaceFirst("type=", "value=\"" + text + "\" ")
				.replace("</button>", "").replace(">", "/>")
				.replace("\"submit\"", "").replace("\"button\"", "");
		return toWrite;
	}
}
