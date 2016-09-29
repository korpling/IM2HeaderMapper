import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class CategoryListing {

	// HashMap including each layerName and its classification(default: "Other")
	public static HashMap<String, String> CategoryHashTable = new HashMap<String, String>();

	// function to set the HashMap values
	public static void setCategoryPairs(String layerName) {
		if (Main.GET_USER_INFO) {
			CategoryHashTable.put(layerName, getMatchingSubset(layerName));
		} else
			CategoryHashTable.put(layerName, "Other");
	}

	// function that creates a list of all classificationNames and its
	// layerNames (function to get those layerNames, that were missing in the
	// corpus.xml-input-file)
	public static List<String> setValueClassPairs(HashMap<String, String> mp) {
		List<String> valueList = new ArrayList<String>();
		for (Entry<String, String> entry : mp.entrySet()) {
			if (!valueList.contains(entry.getValue())) {
				valueList.add(entry.getValue());
			}
		}
		return valueList;

	}

	public static String replaceSpecialCharacter(String idString) {
		if (Pattern.matches("[0-9]", idString.substring(0,1))) {
			idString = "ID_" + idString;
		}
		idString = idString.replace("%", "_PERCENT_");
		idString = idString.replace("+", "_PLUS_");
		idString = idString.replace("&", "_AMP_");
		idString = idString.replace("!", "_EXCL_");
		idString = idString.replace("\"", "_QUOTE_");
		idString = idString.replace("§", "_PARA_");
		idString = idString.replace("$", "_DOL_");
		idString = idString.replace("/", "_SLASH_");
		idString = idString.replace("(", "_LEFT_BR_");
		idString = idString.replace(")", "_RIGHT_BR_");
		idString = idString.replace("=", "_EQUAL_");
		idString = idString.replace("?", "_QUEST_");
		idString = idString.replace("*", "_STAR_");
		idString = idString.replace("~", "_TILDE_");
		idString = idString.replace("'", "_APOS_");
		idString = idString.replace("#", "_HASH_");
		idString = idString.replace(":", "_COLON_");
		idString = idString.replace(";", "_SEMI_COL_");
		idString = idString.replace(" ", "_SPACE_");

		return idString;
	}

	public static List<Attribute> setSourceDesc(Document corpusXML) {
		XPathFactory xpathFactory = XPathFactory.instance();

		// if @sName starts with a number > set a in front of @sName (or take
		// IMOutput-id instead?)
		// TODO: ensure that all id-unfriendly symbols in @sName get replaced
		String DocumentNames = "//sDocumentInfo/@sName";

		// xpath-expression to extract all document-names from IM-output
		XPathExpression<Attribute> DocumentNamePath = xpathFactory.compile(
				DocumentNames, Filters.attribute());
		List<Attribute> DocNames = DocumentNamePath.evaluate(corpusXML);
		return DocNames;

	}

	// function to get the classification of an annotation-layer by user
	private static String getMatchingSubset(String layerName) {
		System.out.println("To which subset does '" + layerName + "' belong?");
		System.out.println("Please type: 'g' for Graphical.");
		System.out.println("Please type: 'l' for Lexical.");
		System.out.println("Please type: 's' for Syntactical.");
		System.out.println("Please type: 'ma' for MarkUp.");
		System.out.println("Please type: 'mo' for Morphological.");
		System.out.println("Please type: 'me' for Meta.");
		System.out.println("Please type: 't' for Transcription.");
		System.out.println("Please type: 'o' for Other.");

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String subsetVar = scanner.nextLine();

		switch (subsetVar) {
		case "g":
			return "Graphical";
		case "l":
			return "Lexical";
		case "s":
			return "Syntactical";
		case "ma":
			return "MarkUp";
		case "mo":
			return "Morphological";
		case "me":
			return "Meta";
		case "t":
			return "Transcription";
		case "o":
			return "Other";
		default:
			System.out.println("Invalid input, please try again.");
			return getMatchingSubset(layerName);
		}
	}
}
