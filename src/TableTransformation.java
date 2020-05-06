import java.util.ArrayList;

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
		return dom.replaceFirst(tag, begin + " name=\"" + tag + "\"").replace(tagEnd, end);
	}

	private String changeElement(String dom, String tag, String tagEnd) {
		String begin = (String) json.get(tag);
		String end = (String) json.get(tagEnd);
		return replace(dom, begin, end, tag, tagEnd);
	}

	private String fors(ArrayList<Elements> groups, String label, String labelEnd, String dom) {
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

	private String cleanDom(String dom) {
		return dom.replace("<tr>", "").replace("</tr>", "").replace("<tbody>", "").replace("</tbody>", "")
				.replace("<td>", "").replace("</td>", "").replace("\t", "").replaceAll("(?m)^[ \t]*\r?\n", "");
	}

	public String transform(Document document, String dom) {
		Elements tables = document.getElementsByTag("table");
		dom = changeElement(dom, THEAD_TAG, THEAD_END_TAG);
		dom = changeElement(dom, TFOOT_TAG, TFOOT_END_TAG);
		for (Element table : tables) {
			String begin = (String) json.get(TABLE_TAG);
			String end = (String) json.get(TABLE_END_TAG);
			Elements tbody = table.getElementsByTag("tbody");
			int columns = tbody.first().getElementsByTag("tr").first().getElementsByTag("td").size();
			dom = dom.replaceFirst(TABLE_TAG, begin + " column=\"" + columns + "\"").replace(TABLE_END_TAG, end);

			Elements thead = document.getElementsByTag("thead");
			Elements tfoot = document.getElementsByTag("tfoot");

			ArrayList<Elements> groups = new ArrayList<>();
			groups.add(tbody.first().getElementsByTag("tr"));
			if (thead.first() != null) {
				groups.add(thead.first().getElementsByTag("tr"));
			}
			if (tfoot.first() != null) {
				groups.add(tfoot.first().getElementsByTag("tr"));
			}
			dom = fors(groups, LABEL_TAG, LABEL_END_TAG, dom);
		}
		return cleanDom(dom);
	}
}
