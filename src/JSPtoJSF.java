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

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JSPtoJSF {

	public JSPtoJSF() {}

	public static File changeExtension(File f, String newExtension) {
		int i = f.getName().lastIndexOf('.');
		String name = f.getName().substring(0, i);
		return new File(name + newExtension);
	}

	public static void replaceLine(String line, JSONObject json, BufferedWriter writer) throws IOException {
        String[] stringTags = new String[]{ "input", "a", "img" };

        List<String> tags = Arrays.asList(stringTags);

		String original = line;
		line = line.replace("<", "").replace(">", "").trim();
		String tag = line.split(" ")[0];
		String test = tag;
//		System.out.println(tag);
//		if (tag.equals("option")) {
//		    String inJson = (String) json.get(tag);
//			System.out.println(inJson);
//			System.out.println("====================");
//		}
//		String test = tags.contains(tag) ? tag : line;
	    String inJson = (String) json.get(test);
	    System.out.println(inJson);
		String toWrite = inJson != null ? original.replaceFirst(test, inJson) : original;
//		System.out.println(toWrite);
		writer.write(toWrite + "\n");
	}

    public static void main(String[] args) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try {
			Reader dictionary = new FileReader("dictionary.json");
			JSONObject jsonObject = (JSONObject) parser.parse(dictionary);
			File fileInput = new File("index.jsp");
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
		System.out.println("====================");
        System.out.println("DONE!!!");
    }
}
