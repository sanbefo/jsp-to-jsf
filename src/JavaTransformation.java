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
		return "<c:when test=\"#{" + tag.substring(beginIndex, endIndex) + "}\">";
	}

	private String forEachTransform(String tag, Stack<String> ends, String dom) {
		int beginIndex = tag.indexOf("<") + 1;
		int endIndex = tag.lastIndexOf(".");
		addToStack(ends, "</c:forEach>");
		String var = tag.substring(beginIndex, endIndex).trim();
		String jsfTag = "<forEach items=\"#{" + var + "} var=\"" + var + "_elem\">";
		return dom.replace(tag, jsfTag).replace(var + ".get(i)", var + "_elem");
	}

	private ArrayList<String> collectTags(ArrayList<String> matches, ArrayList<String> tags) {
		for (String match : matches) {
			match = match.replace("&gt;", ">").replace("&lt;", "<");
			if (!match.contains("for")) {
				for (String tag : match.split("; ")) {
					if (tag.contains("}")) {
						for (String braces : tag.split("}")) {
							tags.add(braces.trim().isEmpty() ? "}" : braces);
						}
					} else {
						tags.add(tag);
					}
				}
			} else {
				tags.add(match);
			}
		}
		return tags;
	}

	private String replaceTags(ArrayList<String> tags, Stack<String> ends, String dom) {
		for (int i = 0; i < tags.size(); i++) {
			if (tags.get(i).contains("if")) {
				String jsfTag = "\n<c:choose>\n\t";
				addToStack(ends, "</c:choose>");
				jsfTag += chooseTransform(tags.get(i), ends);
				dom = dom.replace(tags.get(i), jsfTag);
			}
			if (tags.get(i).contains("for")) {
				dom = forEachTransform(tags.get(i), ends, dom);
			}
			if (tags.get(i).contains("}")) {
				if (!ends.empty()) {
//					System.out.println("=====================");
//					System.out.println(i);
					if (ends.peek().toString().equals("\n</c:otherwise>\n")) {
						dom = dom.replaceFirst(" } ", ends.pop().toString() + ends.pop().toString());
//					}
//					if (ends.size() == 2 && i == (tags.size() - 1)) {
//						System.out.println("ENTRAMOS");
//						dom = dom.replaceFirst(" } ", ends.pop().toString() + ends.pop().toString());
					} else {
						dom = dom.replaceFirst(" } ", ends.pop().toString());
					}
				}
			}
			if (tags.get(i).contains("else {")) {
				String tag = tags.get(i).replace("{", "");
				dom = dom.replaceFirst(tag, "<c:otherwise>");
				addToStack(ends, "\n</c:otherwise>\n");
			}
		}
		return dom;
	}

	private String cleanDom(String dom) {
		return dom.replace(">%>", ">").replace("<%<", "<").replace("<<", "<")
				.replace(">>", ">").replace(">{", ">").replace("<%\n", "")
				.replace("; \n", " %>\n").replace("> %>", ">");
	}

	public String transform(Document document, String dom) {
		Pattern pattern = Pattern.compile("&lt;%(.*?)%&gt;", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dom);
		ArrayList<String> matches = new ArrayList<String>();
		while (matcher.find()) {
			matches.add(matcher.group(1));
		}
		ArrayList<String> tags = new ArrayList<String>();

		tags = collectTags(matches, tags);
		dom = dom.replace("&gt;", ">").replace("&lt;", "<");
		Stack<String> ends = new Stack<String>();
		dom = replaceTags(tags, ends, dom);
		System.out.println(tags.size());
		return cleanDom(dom);
	}
}
