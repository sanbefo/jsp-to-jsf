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
	private JSONObject json;

	public TableTransformation(JSONObject json) {
		this.json = json;
	}

	public String transformJSOUP(Document document, String dom) {
		String begin = (String) json.get(TABLE_TAG);
		String end = (String) json.get(TABLE_END_TAG);
		Elements tbody = document.getElementsByTag("tbody");
		int columns = tbody.first().getElementsByTag("tr").first().getElementsByTag("td").size();
		dom = dom.replaceFirst(TABLE_TAG, begin + " column = \"" + columns + "\"").replace(TABLE_END_TAG, end);
		begin = (String) json.get(THEAD_TAG);
		end = (String) json.get(THEAD_END_TAG);
		Elements thead = document.getElementsByTag("thead");
		dom = dom.replaceFirst(THEAD_TAG, begin + " name = \"header\"").replace(THEAD_END_TAG, end);
		begin = (String) json.get(TFOOT_TAG);
		end = (String) json.get(TFOOT_END_TAG);
		Elements tfoot = document.getElementsByTag("tfoot");
		dom = dom.replaceFirst(TFOOT_TAG, begin + " name = \"footer\"").replace(TFOOT_END_TAG, end);

		
		Elements[] groups = {
			thead.first().getElementsByTag("tr"),
			tfoot.first().getElementsByTag("tr"),
			tbody.first().getElementsByTag("tr"),
		};

		for (Elements group : groups) {
			for (Element body : group) {
				for (Element td : body.children()) {
					String jsfTrHead = "<label>" + td.text() + "</label>";
					dom = dom.replace(td.toString(), jsfTrHead);
				}
			}
		}
		dom = dom.replace("<tr>", "").replace("</tr>", "").replace("<tbody>", "").replace("</tbody>", "");
		System.out.println(dom);
		return dom;
	}
}
