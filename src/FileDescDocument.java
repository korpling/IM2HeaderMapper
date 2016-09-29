import java.util.List;
import java.util.regex.Pattern;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class FileDescDocument {
	static final String DATE_FLAG = "YY";

	protected static Element getFileDesc(Document input) {
		XPathFactory xpathFactory = XPathFactory.instance();

		// get DocumentName for id
		String DocumentName = "/sDocumentInfo/@sName";
		XPathExpression<Attribute> PathToDocumentName = xpathFactory.compile(
				DocumentName, Filters.attribute(), null);
		Attribute docName = PathToDocumentName.evaluateFirst(input);
		
		// set 'a' in front of String, if String starts with a number or a special sign (not allowed at ID-start)
		String DocID = docName.getValue();
		DocID = CategoryListing.replaceSpecialCharacter(DocID);
		
		Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
		Namespace xml = Namespace.XML_NAMESPACE;

		Element fileDesc = new Element("fileDesc", xmlns);
		
		// create Attribute with xml-namespace
		Attribute newContent = new Attribute("id", DocID, xml);
		fileDesc.setAttribute(newContent);
		Element titleStmt = new Element("titleStmt", xmlns);

		Element title = new Element("title", xmlns);
		Element editor = new Element("editor", xmlns);
		// TODO: create editors/ authors for each possible role (create
		// Attribute 'role')
		Element author = new Element("author", xmlns);
		Element persnameA = new Element("persName", xmlns);
		Element forenameA = new Element("forename", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element surnameA = new Element("surname", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);

		persnameA.addContent(forenameA);
		persnameA.addContent(surnameA);
		author.addContent(persnameA);
		
		titleStmt.addContent(title);
		titleStmt.addContent(author);
		titleStmt.addContent(editor);

		Element persname = new Element("persName", xmlns);
		Element forename = new Element("forename", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element surname = new Element("surname", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);

		persname.addContent(forename);
		persname.addContent(surname);

		editor.addContent(persname);

		Element extent = new Element("extent", xmlns);
		extent.setAttribute(new Attribute("type", "Tokens"));

		String tokenAmount = "//structuralInfo/entry[@key='SToken']";
		XPathExpression<Element> PathToTokenNumber = xpathFactory.compile(
				tokenAmount, Filters.element(), null);
		Element NumberOfToken = PathToTokenNumber.evaluateFirst(input);
		extent.setText(NumberOfToken.getText());
		Element idnoPub = new Element("idno", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element publicationStmt = new Element("publicationStmt", xmlns);
		Element pubPlace = new Element("pubPlace", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element publisher = new Element("publisher", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element date = new Element("date", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element biblScope = new Element("biblScope", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		biblScope.setAttribute(new Attribute("unit", IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));

		date.setAttribute(new Attribute("when", DATE_FLAG));
		
		publicationStmt.addContent(idnoPub);
		publicationStmt.addContent(pubPlace);
		publicationStmt.addContent(publisher);
		publicationStmt.addContent(date);
		publicationStmt.addContent(biblScope);

		Element seriesStmt = new Element("seriesStmt", xmlns);

		fileDesc.addContent(titleStmt);
		fileDesc.addContent(extent);
		fileDesc.addContent(publicationStmt);
		fileDesc.addContent(seriesStmt);

		Element titleSeries = new Element("title", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element editorSeries = new Element("editor", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element biblScopeSeries = new Element("biblScope", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		biblScopeSeries.setAttribute(new Attribute("unit", IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		seriesStmt.addContent(titleSeries);
		seriesStmt.addContent(editorSeries);
		seriesStmt.addContent(biblScopeSeries);

		Element sourceDesc = new Element("sourceDesc", xmlns);
		sourceDesc.setAttribute(new Attribute("n", "1"));
		sourceDesc.addContent(new Comment("To describe more than one source, please copy the sourceDesc sequence. Please note, that the value of 'n' should be a sequential number."));
		Element msDesc = new Element("msDesc", xmlns);
		Element recordHist = new Element("recordHist", xmlns);
		Element msIdentifier = new Element("msIdentifier", xmlns);

		sourceDesc.addContent(msDesc);
		sourceDesc.addContent(recordHist);
		msDesc.addContent(msIdentifier);
		fileDesc.addContent(sourceDesc);

		title.addContent(docName.getValue());

		Element msName = new Element("msName", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element altIdentifier = new Element("altIdentifier", xmlns);
		Element repository = new Element("repository", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element collection = new Element("collection", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element idno = new Element("idno", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);

		msIdentifier.addContent(msName);
		altIdentifier.addContent(repository);
		altIdentifier.addContent(collection);
		altIdentifier.addContent(idno);
		msIdentifier.addContent(altIdentifier);

		Element history = new Element("history", xmlns);
		Element origin = new Element("origin", xmlns);

		Element objectType = new Element("objectType", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element origDate = new Element("origDate", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		origDate.setAttribute(new Attribute("precision", IM2HeaderMapper.DEFAULT_VALUE_UNKNOWN));
		origDate.setAttribute(new Attribute("notAfter-custom", IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		origDate.setAttribute(new Attribute("notBefore-custom", IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		origDate.addContent(new Comment("The attribute 'precision' may have the following values: 'high', 'low', 'medium' or 'unknown'."));
		Element origPlace = new Element("origPlace", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element titleOrigin = new Element("title", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element locus = new Element("locus", xmlns);

		origin.addContent(objectType).addContent(origDate)
				.addContent(origPlace).addContent(titleOrigin)
				.addContent(locus);

		history.addContent(origin);
		msDesc.addContent(history);

		Element source = new Element("source", xmlns);
		source.setAttribute(new Attribute("facs", IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		Element ref = new Element("ref", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		ref.setAttribute(new Attribute("target", IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		
		recordHist.addContent(source.addContent(ref));

		// if @sName starts with a number > set a in front of @sName (or take
		// IMOutput-id instead?)
		String DocumentNames = "//sDocumentInfo/@sName";
		XPathExpression<Attribute> DocumentNamePath = xpathFactory.compile(
				DocumentNames, Filters.attribute());
		List<Attribute> DocNames = DocumentNamePath.evaluate(input);

		for (int i = 0; i < DocNames.size(); i++) {
			Attribute sname = DocNames.get(i);
			Element item = new Element("item", xmlns);
			item.setAttribute(new Attribute("n", String.valueOf(i + 1)));
			String sNameString = sname.getValue().toString();
			// check if sName starts with a number or special sign
			if (!Pattern.matches("[a-zA-Z]", sNameString.substring(0, 1)))
				sNameString = "a" + sname.getValue().toString();
			item.setAttribute(new Attribute("corresp", sNameString));
		}
		return fileDesc;
	}

}
