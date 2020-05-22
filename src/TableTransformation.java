import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableTransformation  extends Transformation {

	private final static String TD_TAG = "td";
	private final static String TR_TAG = "tr";
	private final static String TABLE_TAG = "table";
	private final static String TABLE_END_TAG = "/table";
	private final static String THEAD_TAG = "thead";
	private final static String THEAD_END_TAG = "/thead";
	private final static String TBODY_TAG = "tbody";
	private final static String TBODY_END_TAG = "/tbody";
	private final static String TFOOT_TAG = "tfoot";
	private final static String TFOOT_END_TAG = "/tfoot";
	private final static String LABEL_TAG = "<label>";
	private boolean flag;
	private final static String LABEL_END_TAG = "</label>";
	private final static String[] REPLACERS = { "<tr>", "</tr>",
			"<tbody>", "</tbody>", "<td>", "</td>", "(?m)^[ \\t]*\\r?\\n" };

	private JSONObject json;

	public TableTransformation(JSONObject json) {
		this.json = json;
		this.flag = false;
	}

	public boolean getFlag() {
	    return flag;
	}

	public void setFlag(boolean flag) {
	    this.flag = flag;
	}

	private String customReplace(String dom, String begin, String end, String tag, String tagEnd) {
		return dom.replaceFirst(tag, begin + " name=\"" + tag + "\"").replace(tagEnd, end);
	}

	private String changeElement(String dom, String tag, String tagEnd) {
		String begin = (String) json.get(tag);
		String end = (String) json.get(tagEnd);
		return customReplace(dom, begin, end, tag, tagEnd);
	}

	private String fors(ArrayList<Elements> groups, String label, String labelEnd, String dom) {
		for (Elements group : groups) {
			for (Element body : group) {
				for (Element td : body.children()) {
					String jsfTrHead = label + td.text() + labelEnd;
					dom = dom.replace(td.toString(), jsfTrHead);
				}
			}
		}
		return dom;
	}

	private String cleanDom(String dom) {
		for (int i = 0; i < REPLACERS.length; i++) {
			dom = dom.replace(REPLACERS[i], "");
		}
		return dom;
	}

	public String transform(Document document, String dom) {
		Elements tables = document.getElementsByTag(TABLE_TAG);
		dom = changeElement(dom, THEAD_TAG, THEAD_END_TAG);
		dom = changeElement(dom, TFOOT_TAG, TFOOT_END_TAG);
		if (tables.size() > 0) {
			setFlag(true);
			for (Element table : tables) {
				String begin = (String) json.get(TABLE_TAG);
				String end = (String) json.get(TABLE_END_TAG);
				Elements tbody = table.getElementsByTag(TBODY_TAG);
				int columns = tbody.first().getElementsByTag(TR_TAG).first().getElementsByTag(TD_TAG).size();
				dom = dom.replaceFirst(TABLE_TAG, begin + " column=\"" + columns + "\"").replace(TABLE_END_TAG, end);
	
				Elements thead = document.getElementsByTag(THEAD_TAG);
				Elements tfoot = document.getElementsByTag(TFOOT_TAG);
	
				ArrayList<Elements> groups = new ArrayList<>();
				groups.add(tbody.first().getElementsByTag(TR_TAG));
				if (thead.first() != null) {
					groups.add(thead.first().getElementsByTag(TR_TAG));
				}
				if (tfoot.first() != null) {
					groups.add(tfoot.first().getElementsByTag(TR_TAG));
				}
				dom = fors(groups, LABEL_TAG, LABEL_END_TAG, dom);
			}
		}
		return cleanDom(dom);
	}
}
