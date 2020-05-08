import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSPtoJSF {

//	private final static String jspFile = "testFiles/verticalBar.html";
//	private final static String jspFile = "testFiles/hideMenu.html";
//	private final static String jspFile = "testFiles/googleSkeleton.html";
//	private final static String jspFile = "testFiles/buttonBar.html";
	private final static String jspFile = "testFiles/index.jsp";
	private final static String dictionaryFile = "dictionary.json";

	public JSPtoJSF() {}

	public static File changeExtension(File f, String newExtension) {
		int i = f.getName().lastIndexOf('.');
		String name = f.getName().substring(0, i);
		return new File(name + newExtension);
	}

	public static void message(String message) {
		System.out.println("===============================================");
		System.out.println("||                 " + message + "                  ||");
		System.out.println("===============================================");
	}

	public static String domJsoup(JSONObject json, File fileInput) throws IOException {
		Document document = Jsoup.parse(fileInput, "UTF-8");

		Transformation[] transformers = {
			new HTMLTransformation(json),
			new LinkTransformation(json),
			new ScriptTransformation(json),
			new RadioTransformation(json),
			new InputTransformation(json),
			new OptionTransformation(json),
			new ATransformation(json),
			new ImageTransformation(json),
			new ButtonTransformation(json),
			new TableTransformation(json),
			new SimpleTransformation(json),
			new JavaTransformation(json),
//			new JSPTransformation(json),
		};
		String dom = document.toString();

//		System.out.println(dom);

		for (Transformation transformer : transformers) {
			dom = transformer.transform(document, dom);
//			System.out.println("====================================================================");
//			System.out.println(dom);
		}
		dom = dom.replace("itemlabel", "itemLabel").replace("itemvalue", "itemValue").replace("class", "styleClass");
		return dom;
	}

	public static void main(String[] args) throws IOException, ParseException {
		message("START!!!");
		JSONParser parser = new JSONParser();
		try {
			Reader dictionary = new FileReader(dictionaryFile);
			JSONObject json = (JSONObject) parser.parse(dictionary);
			File fileInput = new File(jspFile);
			String res = domJsoup(json, fileInput);

			System.out.println(res);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		message("DONE!!! ");
	}
}
