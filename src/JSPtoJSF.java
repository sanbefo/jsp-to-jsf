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

public class JSPtoJSF {
	
	private final static String dictionaryFile = "dictionary.json";
	private final static String jspFile = "index.jsp";

	public JSPtoJSF() {}

	public static File changeExtension(File f, String newExtension) {
		int i = f.getName().lastIndexOf('.');
		String name = f.getName().substring(0, i);
		return new File(name + newExtension);
	}

	public static void replaceLine(String line, JSONObject json, BufferedWriter writer) throws IOException, ParseException {
		//TODO missing 'a' tag
		String[] stringTags = new String[]{ "input", "img" };
		List<String> complexTags = Arrays.asList(stringTags);

		String original = line;
		line = line.replace("<", "").replace(">", "").trim();
		String tag = line.split(" ")[0];
		String test = tag;

		if (complexTags.contains(tag)) {
			JSONParser parser = new JSONParser();
			JSONArray inJson = (JSONArray) json.get(tag);
			JSONObject inArray = (JSONObject) inJson.get(0);
			String toWrite = original;
			for (Object key : inArray.keySet()) {
				toWrite = toWrite.replaceFirst(key.toString(), inArray.get(key).toString());
			}
			writer.write(toWrite + "\n");
		} else {
			String inJson = (String) json.get(test);
			System.out.println(inJson);
			String toWrite = inJson != null ? original.replaceFirst(tag, inJson) : original;
			writer.write(toWrite + "\n");
		}
	}

	public static void main(String[] args) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		try {
			Reader dictionary = new FileReader(dictionaryFile);
			JSONObject jsonObject = (JSONObject) parser.parse(dictionary);
			File fileInput = new File(jspFile);
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("====================DONE!!!====================");
	}
}
