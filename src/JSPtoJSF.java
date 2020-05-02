import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JSPtoJSF {

	private final static String jspFile = "index.jsp";
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
//			new HTMLTransformation(json),
//			new LinkTransformation(json),
//			new ScriptTransformation(json),
//			new RadioTransformation(json),
//			new InputTransformation(json),
//			new OptionTransformation(json),
//			new ATransformation(json),
//			new ImageTransformation(json),
//			new ButtonTransformation(json),
//			new TableTransformation(json),
//			new SimpleTransformation(json),
			new JavaTransformation(json),
//			new JSPTransformation(json),
		};
		String dom = document.toString();

//		System.out.println(dom);

		for (Transformation transformer : transformers) {
			dom = transformer.transformJSOUP(document, dom);
//			System.out.println("====================================================================");
//			System.out.println(dom);
		}
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

//			System.out.println(res);
//			if (1 == 0) {
//				File fileOutput = new File("test.txt");
//				FileWriter fr = new FileWriter(fileOutput);
//				BufferedWriter writer = new BufferedWriter(fr);
//				BufferedReader reader = new BufferedReader(new FileReader(fileInput));
//				String line;
//				while ((line = reader.readLine()) != null) {
//					replaceLine(line, json, writer);
//				}
//				reader.close();
//				writer.close();
//			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		message("DONE!!! ");
	}
}
