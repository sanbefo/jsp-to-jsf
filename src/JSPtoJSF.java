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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

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

		if (complexTags.contains(tag)) {
			JSONArray inJson = (JSONArray) json.get(tag);
			JSONObject inArray = (JSONObject) inJson.get(0);
			String toWrite = original;			
			switch(tag) {
				case INPUT_TAG:
					Pattern pattern = Pattern.compile("type=*\"(.*?)\"");
					Matcher matcher = pattern.matcher(line);
					System.out.println(line);
					if (matcher.find())
					{
						String toRemove = matcher.group(0);
						String inputType = matcher.group(1);
						toWrite = original.replace(toRemove + " ", "");
						JSONArray array = (JSONArray) inArray.get("type");
						JSONObject types = (JSONObject) array.get(0);
						String type = (String) types.get(inputType);
						inputType = (String) inArray.get(inputType);
						System.out.println(inputType);
						toWrite = toWrite.replaceFirst(tag, type);
					}
					break;
				case IMG_TAG:
					for (Object key : inArray.keySet()) {
						toWrite = toWrite.replaceFirst(key.toString(), inArray.get(key).toString());
					}
					break;
			}
			
			writer.write(toWrite + "\n");
		} else {
			String inJson = (String) json.get(tag);
			if (inJson != null) System.out.println(inJson);
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
		System.out.println("\n====================DONE!!!====================");
	}
}
