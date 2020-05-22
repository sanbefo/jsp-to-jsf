import org.jsoup.nodes.Document;

public class Transformation {

	private boolean flag;
	
	public boolean getFlag() {
	    return flag;
	}

	public void setFlag(boolean flag) {
	    this.flag = flag;
	}

	public String transform(Document document, String dom) {
		return dom;
	}

	public String notes() {
		return "";
	}
}
