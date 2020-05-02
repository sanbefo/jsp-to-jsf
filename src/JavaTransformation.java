import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import java.util.Stack;

//<% %&lt;
//%&gt; %>

public class JavaTransformation extends Transformation {
	private JSONObject json;
//	private Stack ends;

	public JavaTransformation(JSONObject json) {
		this.json = json;
//		this.ends = new Stack();
	}

	private void addToStack(Stack ends, String end) {
		ends.push(end);
	}
	
	private String chooseTransform(String tag, Stack ends) {
		int beginIndex = tag.indexOf("(") + 1;
		int endIndex = tag.lastIndexOf(")");
		addToStack(ends, "\n</c:when>\n");
		return "<c:when test = \"#{" + tag.substring(beginIndex, endIndex) + "}\">";
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
		System.out.println(tags);
		System.out.println(matches);
		Stack ends = new Stack();
		for (String tag : tags) {
//			if (tag.contains("= ")) {
//				String value = tag.replace("=", "");
//				String jsfTag = "\n<c:out value=\"#{" + value.trim() + "}\"/>\n\t";
//				dom = dom.replace(tag, jsfTag);
//			}
			if (tag.contains("if")) {
				String jsfTag = "\n<c:choose>\n\t";
				addToStack(ends, "</c:choose>");
				jsfTag += chooseTransform(tag, ends);
				dom = dom.replace(tag, jsfTag);
			}
			if (tag.contains("}")) {
				dom = dom.replaceFirst(" } ", ends.pop().toString());
			}
			if (tag.contains("else {")) {
				System.out.println("=====================================");
				System.out.println(tag);
				tag = tag.replace("{", "");
				System.out.println(tag);
				System.out.println("=====================================");
				dom = dom.replaceFirst("else ", "<c:otherwise>");
				addToStack(ends, "\n</c:otherwise>\n");
			}
		}
		System.out.println(dom.replace("%>", "").replace("<%", "").replace(">{", ">"));
		System.out.println(tags);
		return dom.replace("%>", ">").replace("<%", "<");
	}
}
