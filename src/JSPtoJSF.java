import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSPtoJSF {

	private final static String VERTICAL_BAR_FILE = "testFiles/verticalBar.html";
	private final static String HIDE_MENU_FILE = "testFiles/hideMenu.html";
	private final static String GOOGLE_SKELETON_FILE = "testFiles/googleSkeleton.html";
	private final static String BUTTON_BAR_FILE = "testFiles/buttonBar.html";
	private final static String INDEX_FILE = "testFiles/index.jsp";
	private final static String CHECKOUT_FILE = "testFiles/checkout.jsp";
	private final static String BOOK_SHOP_FILE = "testFiles/bookShopHome.jsp";
	private final static String JSTL_FILE = "testFiles/req_params_jstl.jsp";
	private final static String JSON_FILE = "dictionary.json";
	private final static String XHTML_EXTENSION = "xhtml";
	private final static String FOLDER_NAME = "transformedFiles/";
	private final static String[] FILES = { VERTICAL_BAR_FILE, HIDE_MENU_FILE,
			GOOGLE_SKELETON_FILE, BUTTON_BAR_FILE, INDEX_FILE, JSTL_FILE,
			CHECKOUT_FILE };

	public JSPtoJSF() {}

	public static String changeExtension(File file, String newExtension) {
		int i = file.getName().lastIndexOf('.');
		String name = file.getName().substring(0, i);
		return name + "." + newExtension;
	}

	public static void message(String message) {
		System.out.println("===============================================");
		System.out.println("||                 " + message + "                  ||");
		System.out.println("===============================================");
	}

	public static String customUpperCase(String dom) {
		return dom.replace("itemlabel", "itemLabel").replace("itemvalue", "itemValue").replace("class", "styleClass");
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
			new JSPTransformation(json),
		};
		String dom = document.toString();

		for (Transformation transformer : transformers) {
			dom = transformer.transform(document, dom);
		}
		dom = customUpperCase(dom);
		return dom;
	}

	public static void main(String[] args) throws IOException, ParseException {
		message("START!!!");
		JSONParser parser = new JSONParser();
		try {
			for (String filename : FILES) {
				Reader dictionary = new FileReader(JSON_FILE);
				JSONObject json = (JSONObject) parser.parse(dictionary);
				File file = new File(filename);
				String res = domJsoup(json, file);
				File folder = new File(FOLDER_NAME);
		        folder.mkdirs();
				FileOutputStream name = new FileOutputStream(FOLDER_NAME + changeExtension(file, XHTML_EXTENSION));
				PrintStream out = new PrintStream(name);
				out.print(res);
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		message("DONE!!! ");
	}
}
