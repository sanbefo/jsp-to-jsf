import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageTransformation extends Transformation {

	private final static String IMG_TAG = "img";
	
	public static String transform(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		String toWrite = original;
		for (Object key : inArray.keySet()) {
			toWrite = toWrite.replaceFirst(key.toString(), inArray.get(key).toString());
		}
		return toWrite;
	}
	
	public String transformJSOUP(Document document, JSONObject json, String dom, Elements html) {
		JSONArray values = (JSONArray) json.get(IMG_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String value = (String) inArray.get("src");
		String img = (String) inArray.get(IMG_TAG);//tag
		Elements tokens = document.getElementsByTag(IMG_TAG);
		for (Element token : tokens) {
			String original = token.toString();
			token.attr(value, token.attr("src"));
			token.removeAttr("src");
			String jsfTag = token.toString().replaceFirst(IMG_TAG, img).replace(">", "/>\n");
			dom = dom.replace(original, jsfTag);
		}
		return dom;
	}
}
