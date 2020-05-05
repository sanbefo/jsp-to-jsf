import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import java.util.Stack;

//<% %&lt;
//%&gt; %>

public class JavaTransformation extends Transformation {
	private JSONObject json;

	public JavaTransformation(JSONObject json) {
		this.json = json;
	}

	private void addToStack(Stack<String> ends, String end) {
		ends.push(end);
	}

	private String chooseTransform(String tag, Stack<String> ends) {
		int beginIndex = tag.indexOf("(") + 1;
		int endIndex = tag.lastIndexOf(")");
		addToStack(ends, "\n</c:when>\n");
		return "<c:when test = \"#{" + tag.substring(beginIndex, endIndex) + "}\">";
	}

	private String forEachTransform(String tag, Stack<String> ends) {
		int beginIndex = tag.indexOf("<") + 1;
		int endIndex = tag.lastIndexOf(".");
		addToStack(ends, "\n</c:forEach>\n");
		String var = tag.substring(beginIndex, endIndex).trim();
		return "<forEach items = \"#{" + var + "} var = \"" + var + "\">";
	}

	public String transformJSOUP(Document document, String dom) {
		System.out.println(dom);
		Pattern pattern = Pattern.compile("&lt;%(.*?)%&gt;", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dom);
		ArrayList<String> matches = new ArrayList<String>();
		while (matcher.find()) {
			matches.add(matcher.group(1));
		}
		ArrayList<String> tags = new ArrayList<String>();

		for (String match : matches) {
			match = match.replace("&gt;", ">").replace("&lt;", "<");
			if (!match.contains("for")) {
				for (String tag : match.split("; ")) {
					if (tag.contains("}")) {
						for (String x : tag.split("}")) {
							if (x.trim().isEmpty()) {
								tags.add("}");
							} else {
								tags.add(x);
							}
						}
					} else {
						tags.add(tag);
					}
				}
			} else {
				tags.add(match);
			}
		}
		dom = dom.replace("&gt;", ">").replace("&lt;", "<");
		Stack<String> ends = new Stack<String>();
		for (String tag : tags) {
			if (tag.contains("if")) {
				String jsfTag = "\n<c:choose>\n\t";
				addToStack(ends, "</c:choose>");
				jsfTag += chooseTransform(tag, ends);
				dom = dom.replace(tag, jsfTag);
			}
			if (tag.contains("for")) {
				String jsfTag = forEachTransform(tag, ends);
				dom = dom.replace(tag, jsfTag);
			}
			if (tag.contains("}")) {
				if (!ends.empty()) {
					if (ends.peek().toString() == "\n</c:otherwise>\n") {
						dom = dom.replaceFirst(" } ", ends.pop().toString() + ends.pop().toString());
					} else {
						dom = dom.replaceFirst(" } ", ends.pop().toString());
					}
				}
			}
			if (tag.contains("else {")) {
				tag = tag.replace("{", "");
				dom = dom.replaceFirst("else ", "<c:otherwise>");
				addToStack(ends, "\n</c:otherwise>\n");
			}
		}
//		System.out.println(dom.replace("%>", "").replace("<%", "").replace(">{", ">"));
//		System.out.println(tags);
		return dom.replace("%>", ">").replace("<%", "<");
	}
}
