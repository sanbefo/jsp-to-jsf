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
import org.w3c.tidy.Tidy;

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
			case INPUT_TAG:
				result = InputTransformation.transform(original, json, tag, inJson, inArray);
				break;
			case IMG_TAG:
				result = ImageTransformation.transform(original, json, tag, inJson, inArray);
				break;
			case OPTION_TAG:
				result = OptionTransformation.transform(original, json, tag, inJson, inArray);
				break;
			case A_TAG:
				result = ATransformation.transform(original, json, tag, inJson, inArray);
				break;
			case BUTTON_TAG:
				result = ButtonTransformation.transform(original, json, tag, inJson, inArray);
				break;
		}
		return result;
	}

	public static void replaceLine(String line, JSONObject json, BufferedWriter writer) throws IOException, ParseException {
		//TODO button tag missing
		//TODO form tag missing
		//TODO table tag missing
		String[] stringTags = new String[]{ "img", "input", "option", "a", "button" };
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

//	public static String cleanData(String data) throws UnsupportedEncodingException {
////	    Tidy tidy = new Tidy();
////	    tidy.setInputEncoding("UTF-8");
////	    tidy.setOutputEncoding("UTF-8");
////	    tidy.setWraplen(Integer.MAX_VALUE);
////	    tidy.setPrintBodyOnly(true);
////	    tidy.setXmlOut(true);
////	    tidy.setSmartIndent(true);
////	    ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
////	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
////	    tidy.parseDOM(inputStream, outputStream);
////	    return outputStream.toString("UTF-8");
//
////		BufferedReader br = new BufferedReader(new StringReader(str));
//		StringWriter sw = new StringWriter();
//
//		Tidy t = new Tidy();
//		t.setDropEmptyParas(true);
//		t.setShowWarnings(false); //to hide errors
//		t.setQuiet(true); //to hide warning
//		t.setUpperCaseAttrs(false);
//		t.setUpperCaseTags(false);
////		t.parse(br,sw);
//		StringBuffer sb = sw.getBuffer();
//		String strClean = sb.toString();
////		br.close();
////		sw.close();
//
//		//do another round of tidyness
////		br = new BufferedReader(new StringReader(strClean));
//		sw = new StringWriter();
//
//		t = new Tidy();
//		t.setXmlTags(true);
////		t.parse(br,sw);
//		sb = sw.getBuffer();
//		String strClean2 = sb.toString();
////		br.close();
////		sw.close();
//	}

	public static Document getHtmlDocument() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head><style language='text/css'>");
		sb.append("@page{ margin: 0; }");
		sb.append("body{ margin:0;}");
		sb.append("</style></head>");
		sb.append("<body>");
		sb.append("<h1>HELLO WORLD</h1>");
		sb.append("</body>");
		sb.append("</html>");
		
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		
		return (Document) tidy.parseDOM(new ByteArrayInputStream(sb.toString().getBytes()), null);
	}

	public static void message(String message) {
		System.out.println("===============================================");
		System.out.println("||                 " + message + "                  ||");
		System.out.println("===============================================");
	}

	public static void main(String[] args) throws IOException, ParseException {
		message("START!!!");
//		System.out.println(getHtmlDocument().toString());
		JSONParser parser = new JSONParser();
		try {
			Reader dictionary = new FileReader(dictionaryFile);
			JSONObject jsonObject = (JSONObject) parser.parse(dictionary);
			File fileInput = new File(jspFile);
//			System.out.println(fileInput.toString());
//			System.out.println(cleanData(fileInput.toString()));
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
		message("DONE!!! ");
	}
}
