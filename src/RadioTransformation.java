import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RadioTransformation extends Transformation {

	private final static String INPUT_TAG = "input";
	private final static String LABEL_TAG = "label";
	private final static String RADIO_ATTR = "radio";
	private final static String ID_ATTR = "id";
	private final static String TYPE_ATTR = "type";
	private final static String VALUE_ATTR = "value";
	private final static String NAME_ATTR = "name";
	private final static String FOR_ATTR = "for";
	private boolean flag;
	private JSONObject json;

	public RadioTransformation(JSONObject json) {
		this.json = json;
		this.flag = false;
	}

	public boolean getFlag() {
	    return flag;
	}

	public void setFlag(boolean flag) {
	    this.flag = flag;
	}

	public String transform(Document document, String dom) {
		Elements radios = document.getElementsByAttributeValue(TYPE_ATTR, RADIO_ATTR);
		if (radios.size() > 0) {
			setFlag(true);
			for (Element radio : radios) {
				JSONArray values = (JSONArray) json.get(RADIO_ATTR);
				JSONObject inArray = (JSONObject) values.get(0);
				String value = (String) inArray.get(VALUE_ATTR);
				String radioTag = (String) inArray.get(RADIO_ATTR);
				String label = (String) inArray.get(LABEL_TAG);
				String original = radio.toString();
				String labelId = radio.attr(ID_ATTR);
				Elements labelTag = document.getElementsByAttributeValue(FOR_ATTR, labelId);
				dom = dom.replace(labelTag.toString(), "");
				radio.attr(label, labelTag.text());
				radio.attr(value, radio.attr(VALUE_ATTR));
				radio.removeAttr(ID_ATTR);
				radio.removeAttr(TYPE_ATTR);
				radio.removeAttr(VALUE_ATTR);
				radio.removeAttr(NAME_ATTR);
				String jsfTag = radio.toString().replaceFirst(INPUT_TAG, radioTag).replace(">", " />");
				dom = dom.replace(original, jsfTag);
			}
		}
		return dom;
	}
}
