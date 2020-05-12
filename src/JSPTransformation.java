import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;

public class JSPTransformation extends Transformation {

	private JSONObject json;
	private final static String[] IGNORE_CORE = { "c:catch", "c:choose", "c:forEach",
			"c:forTokens", "c:if", "c:import", "c:otherwise", "c:out", "c:param",
			"c:redirect", "c:remove", "c:set", "c:when" };

	private final static String[] IGNORE_i18n = { "fmt:bundle", "fmt:formatDate", "fmt:formatNumber",
			"fmt:message", "fmt:param", "fmt:parseDate", "fmt:parseNumber", "fmt:requestEncoding",
			"fmt:setBundle", "fmt:setLocale", "fmt:setTimeZone", "fmt:timeZone" };

	private final static String[] IGNORE_FUNCTIONS = { "fn:contains", "fn:containsIgnoreCase",
			"fn:endsWith", "fn:escapeXml", "fn:indexOf", "fn:join", "fn:length", "fn:replace",
			"fn:split", "fn:startsWith", "fn:substring", "fn:substringAfter", "fn:substringBefore",
			"fn:toLowerCase", "fn:toUpperCase", "fn:trim" };

	private final static String[] IGNORE_DATABASE = { "sql:dateParam", "sql:param",
			"sql:query", "sql:setDataSource", "sql:transaction", "sql:update" };

	public JSPTransformation(JSONObject json) {
		this.json = json;
	}

	public String transform(Document document, String dom) {
		return dom;
	}
}


