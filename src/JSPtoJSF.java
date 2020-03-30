import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;

public class JSPtoJSF {
	
	private final static String dictionaryFile = "dictionary.json";
	private final static String jspFile = "index.jsp";
	private final static String INPUT_TAG = "input";
	private final static String IMG_TAG = "img";

	public JSPtoJSF() {}

	public static File changeExtension(File f, String newExtension) {
		int i = f.getName().lastIndexOf('.');
		String name = f.getName().substring(0, i);
		return new File(name + newExtension);
	}

	public static void replaceLine(String line, JSONObject json, BufferedWriter writer) throws IOException, ParseException {
		//TODO missing 'a' tag
		//TODO missing 'select-option' tag
		String[] stringTags = new String[]{ "img", "input" };
		List<String> complexTags = Arrays.asList(stringTags);

		String original = line;
		line = line.replace("<", "").replace(">", "").trim();
		String tag = line.split(" ")[0];
		String toWrite = original;

		if (complexTags.contains(tag)) {
			JSONArray inJson = (JSONArray) json.get(tag);
			JSONObject inArray = (JSONObject) inJson.get(0);
			if (tag.equals(INPUT_TAG)) {
				toWrite = InputTransformation.transform(original, json, tag, inJson, inArray);
			}
			if (tag.equals(IMG_TAG)) {
				toWrite = ImageTransformation.transform(original, json, tag, inJson, inArray);
			}
		} else {
			String inJson = (String) json.get(tag);
			if (inJson != null) System.out.println(inJson);
			toWrite = inJson != null ? original.replaceFirst(tag, inJson) : original;
		}
		writer.write(toWrite + "\n");
	}

	public static void main(String[] args) throws IOException, ParseException {
		System.out.println("===============================================");
		System.out.println("||                  START!!!                 ||");
		System.out.println("===============================================");
		JSONParser parser = new JSONParser();
		try {
			Reader dictionary = new FileReader(dictionaryFile);
			JSONObject jsonObject = (JSONObject) parser.parse(dictionary);
			File fileInput = new File(jspFile);
			Document doc = Jsoup.parse(fileInput, null);
			System.out.println(doc.getElementsByTag("input"));
			System.out.println("-----------------------------");
//			if (0 == 1) {
				File fileOutput = new File("test.txt");
				FileWriter fr = new FileWriter(fileOutput);
				BufferedWriter writer = new BufferedWriter(fr);
				BufferedReader reader = new BufferedReader(new FileReader(fileInput));
				String line;
				while ((line = reader.readLine()) != null) {
					replaceLine(line, jsonObject, writer);
				}
				reader.close();
				writer.close();
//			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("===============================================");
		System.out.println("||                  DONE!!!                  ||");
		System.out.println("===============================================");
	}
}
