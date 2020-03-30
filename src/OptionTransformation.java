import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class OptionTransformation {

	public static String transform(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		System.out.println(original);
		String value = (String) inArray.get("value");
		String option = (String) inArray.get("option");
//		System.out.println("------------------------");
//		System.out.println(inJson);
//		System.out.println(tag);
		String toWrite = original;
		Document doc = Jsoup.parse(original);
//		System.out.println(doc);
//		doc.getElementsByTag("option");
		toWrite = toWrite.replaceFirst("option", option).replace(doc.getElementsByTag("option").text(), "")
				.replaceFirst("value", value).replace("</option>", "")
				.replace(">", " itemLabel = \"" + doc.getElementsByTag("option").text() + "\"/>");

		
		//		for (Object key : inArray.keySet()) {
//			toWrite = toWrite.replaceFirst(key.toString(), inArray.get(key).toString());
//		}
		return toWrite;
	}
}
