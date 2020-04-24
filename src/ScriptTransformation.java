import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScriptTransformation extends Transformation {

	private final static String SCRIPT_TAG = "script";
	private JSONObject json;

	public ScriptTransformation(JSONObject json) {
		this.json = json;
	}

	public String transformJSOUP(Document document, String dom) {
		JSONArray values = (JSONArray) json.get(SCRIPT_TAG);
		JSONObject inArray = (JSONObject) values.get(0);
		String library = (String) inArray.get("type");
		String name = (String) inArray.get("src");
		String tag = (String) inArray.get(SCRIPT_TAG);
		Elements tokens = document.getElementsByTag(SCRIPT_TAG);
		for (Element token : tokens) {
			String nameAttr = token.attr("src");
			String original = token.toString().replace("</script>", "");
			token.attr(nameAttr, token.attr("src"));
			token.attr(library, "js");
			token.attr(name, nameAttr);
			token.removeAttr("type");
			token.removeAttr("src");
			token.text("");
			String jsfTag = token.toString().replaceFirst(SCRIPT_TAG, tag)
					.replace("</script>", "").replace(">", " />");
			dom = dom.replace(original, jsfTag);
		}
		dom = dom.replace("</script>", "");
		return dom;
	}
}
