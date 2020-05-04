import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableTransformation  extends Transformation {

	private final static String TABLE_TAG = "table";
	private final static String TABLE_END_TAG = "/table";
	private final static String THEAD_TAG = "thead";
	private final static String THEAD_END_TAG = "/thead";
	private final static String TFOOT_TAG = "tfoot";
	private final static String TFOOT_END_TAG = "/tfoot";
	private final static String LABEL_TAG = "label";
	private final static String LABEL_END_TAG = "/label";
	private JSONObject json;

	public TableTransformation(JSONObject json) {
		this.json = json;
	}

	private String replace(String dom, String begin, String end, String tag, String tagEnd) {
		return dom.replaceFirst(tag, begin + " name = \"" + tag + "\"").replace(tagEnd, end);
	}

	private String changeElement(String dom, String tag, String tagEnd) {
		String begin = (String) json.get(tag);
		String end = (String) json.get(tagEnd);
		return replace(dom, begin, end, tag, tagEnd);
	}

	private String fors(Elements[] groups, String label, String labelEnd, String dom) {
		for (Elements group : groups) {
			for (Element body : group) {
				for (Element td : body.children()) {
					String jsfTrHead = "<" + label + ">" + td.text() + "<" + labelEnd + ">";
					dom = dom.replace(td.toString(), jsfTrHead);
				}
			}
		}
		return dom;
	}

	public String transformJSOUP(Document document, String dom) {
		Elements tables = document.getElementsByTag("table");
		for (Element table : tables) {
			String begin = (String) json.get(TABLE_TAG);
			String end = (String) json.get(TABLE_END_TAG);
			Elements tbody = table.getElementsByTag("tbody");
			int columns = tbody.first().getElementsByTag("tr").first().getElementsByTag("td").size();
			dom = dom.replaceFirst(TABLE_TAG, begin + " column = \"" + columns + "\"").replace(TABLE_END_TAG, end);

			dom = changeElement(dom, THEAD_TAG, THEAD_END_TAG);
			dom = changeElement(dom, TFOOT_TAG, TFOOT_END_TAG);
			Elements thead = document.getElementsByTag("thead");
			Elements tfoot = document.getElementsByTag("tfoot");

			Elements[] groups = {
				thead.first().getElementsByTag("tr"),
				tfoot.first().getElementsByTag("tr"),
				tbody.first().getElementsByTag("tr"),
			};
			dom = fors(groups, LABEL_TAG, LABEL_END_TAG, dom);
		}
		dom = dom.replace("<tr>", "").replace("</tr>", "").replace("<tbody>", "").replace("</tbody>", "")
				.replace("<td>", "").replace("</td>", "").replace("\t", "");
		dom = dom.replaceAll("(?m)^[ \t]*\r?\n", "");
		return dom;
	}
}
