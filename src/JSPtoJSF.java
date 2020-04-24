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
import org.json.simple.parser.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JSPtoJSF {

	private final static String A_TAG = "a";
	private final static String IMG_TAG = "img";
	private final static String INPUT_TAG = "input";
	private final static String BUTTON_TAG = "button";
	private final static String OPTION_TAG = "option";
	private final static String jspFile = "index.jsp";
	private final static String dictionaryFile = "dictionary.json";

	public JSPtoJSF() {}

	public static File changeExtension(File f, String newExtension) {
		int i = f.getName().lastIndexOf('.');
		String name = f.getName().substring(0, i);
		return new File(name + newExtension);
	}

	public static String switchTag(String original, JSONObject json, String tag, JSONArray inJson, JSONObject inArray) {
		String result = "";
		switch(tag) {
			case A_TAG:
				result = ATransformation.transform(original, json, tag, inJson, inArray);
				break;
			case IMG_TAG:
				result = ImageTransformation.transform(original, json, tag, inJson, inArray);
				break;
			case INPUT_TAG:
				result = InputTransformation.transform(original, json, tag, inJson, inArray);
				break;
			case OPTION_TAG:
				result = OptionTransformation.transform(original, json, tag, inJson, inArray);
				break;
			case BUTTON_TAG:
				result = ButtonTransformation.transform(original, json, tag, inJson, inArray);
				break;
		}
		return result;
	}

	public static void replaceLine(String line, JSONObject json, BufferedWriter writer) throws IOException, ParseException {
		String[] stringTags = new String[]{ "img", "input", "option", "a", "button", "html" };
		List<String> complexTags = Arrays.asList(stringTags);

		String original = line;
		line = line.replace("<", "").replace(">", "").trim();
		String tag = line.split(" ")[0];
		String toWrite = original;
		if (complexTags.contains(tag)) {
			JSONArray inJson = (JSONArray) json.get(tag);
			JSONObject inArray = (JSONObject) inJson.get(0);
			toWrite = switchTag(original, json, tag, inJson, inArray);
		} else {
			String inJson = (String) json.get(tag);
			toWrite = inJson != null ? original.replaceFirst(tag, inJson) : original;
		}
		writer.write(toWrite + "\n");
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
			new InputTransformation(json),
			new OptionTransformation(json),
			new ATransformation(json),
			new ImageTransformation(json),
			new ButtonTransformation(json),
			new TableTransformation(json),
			new SimpleTransformation(json),
		};
		String dom = document.html();
		
		System.out.println(dom);

		for (Transformation transformer : transformers) {
			dom = transformer.transformJSOUP(document, dom);
			System.out.println("====================================================================");
			System.out.println(dom);
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

			System.out.println(res);

			
			
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
