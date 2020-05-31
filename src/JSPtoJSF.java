import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
public class JSPtoJSF {

	private static CommandLine cmd;
	private final static String VERTICAL_BAR_FILE = "testFiles/verticalBar.html";
	private final static String HIDE_MENU_FILE = "testFiles/hideMenu.html";
	private final static String GOOGLE_SKELETON_FILE = "testFiles/googleSkeleton.html";
	private final static String BUTTON_BAR_FILE = "testFiles/buttonBar.html";
	private final static String INDEX_FILE = "testFiles/index.jsp";
	private final static String CHECKOUT_FILE = "testFiles/checkout.jsp";
	private final static String JSTL_FILE = "testFiles/req_params_jstl.jsp";
	private final static String JSON_FILE = "C:\\Users\\Personal\\eclipse-workspace\\Conversor\\dictionary.json";
	private final static String TXT_EXTENSION = "txt";
	private final static String XHTML_EXTENSION = "xhtml";
	private final static String NOTES_FOLDER = "notesFiles/";
	private final static String TRANSFORMATIONS_FOLDER = "transformedFiles/";
	private final static String[][] REPLACERS = { { "itemlabel", "itemLabel" },
			{ "itemvalue", "itemValue" }, { "class", "styleClass" } };	
	private final static String[] FILES = { VERTICAL_BAR_FILE, HIDE_MENU_FILE,
			GOOGLE_SKELETON_FILE, BUTTON_BAR_FILE, INDEX_FILE, JSTL_FILE,
			CHECKOUT_FILE };

	private final static String[] FILE = { INDEX_FILE };
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
		for (int i = 0; i < REPLACERS.length; i++) {
			dom = dom.replace(REPLACERS[i][0], REPLACERS[i][1]);
		}
		return dom;
	}

	public static String domJsoup(JSONObject json, File file) throws IOException {
		Document document = Jsoup.parse(file, "UTF-8");
		File folder = new File(NOTES_FOLDER);
        folder.mkdirs();

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

		FileOutputStream name = new FileOutputStream(NOTES_FOLDER + changeExtension(file, TXT_EXTENSION));
		PrintStream out = new PrintStream(name);
		int i = file.getName().lastIndexOf('.');
		String filename = file.getName().substring(0, i);

		out.println("=====================================================================================================");
		out.println("Warning notes and recommendations for the file " + filename);
		out.println("=====================================================================================================");
		out.println("Check tabulations and linebreakes for better readability\n");

		for (Transformation transformer : transformers) {
			dom = transformer.transform(document, dom);
			if (transformer.getFlag()) {
				out.println(transformer.notes());
			}
		}
		out.close();
		return customUpperCase(dom);
	}

	public static boolean cliFiles(String[] args) {
		Options options = new Options();
	    options.addOption("f", true, "Path of file(s) to be transformed");

	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("From JSP to JSF", options);
        CommandLineParser parser = new DefaultParser();
	      try {
	          cmd = parser.parse(options, args);
	      } catch (ParseException e1) {
	          e1.getMessage();
	          return false;
	      }
	      return true;
	}

	public static void processFile() throws IOException, org.json.simple.parser.ParseException {
		if (cmd.hasOption("f")) {
			File file = new File(cmd.getOptionValue("f"));
			message("START!!!");
			String filename = cmd.getOptionValue("f").substring(cmd.getOptionValue("f").lastIndexOf("\\") + 1, cmd.getOptionValue("f").length());
			System.out.println("\n The file to be transformed is: " + filename + "\n");
			JSONParser parser = new JSONParser();
			try {
				Reader dictionary = new FileReader(JSON_FILE);
				JSONObject json = (JSONObject) parser.parse(dictionary);
				String res = domJsoup(json, file);
				File folder = new File(TRANSFORMATIONS_FOLDER);
		        folder.mkdirs();
				FileOutputStream name = new FileOutputStream(TRANSFORMATIONS_FOLDER + changeExtension(file, XHTML_EXTENSION));
				PrintStream out = new PrintStream(name);
				out.print(res);
				out.close();
			} catch (FileNotFoundException e) {
				System.out.println("\n The file seems to be wrong \n");
			}
			message("DONE!!! ");
		}
	}

	public static void main(String[] args) throws IOException, org.json.simple.parser.ParseException  {
		cmd = null;
		if (cliFiles(args)) {
			processFile();
		} else {
			System.out.println("NADA");
		}
	}
}
