
import java.util.Scanner;
import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class FileDescCorpus {
	static String default_date = "1111";
	protected static String date;
	protected static Element getFileDesc(Document SaltProjectInput) {
		Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

		XPathFactory xpathFactory = XPathFactory.instance();

		Element fileDesc = new Element("fileDesc", xmlns);
		Element titleStmt = new Element("titleStmt", xmlns);
		
		// if directory-name = corpus-name > get CorpusName from IM-Output here
//		String corpusName = "/sCorpusInfo/@sName";
//		XPathExpression<Attribute> PathToDirectoryName = xpathFactory.compile(
//				corpusName, Filters.attribute(), null);
//		Attribute DirectoryName = PathToDirectoryName.evaluateFirst(input);

		// set corpus-Name by terminal-request
		Element title = new Element("title", xmlns);
		if(Main.GET_USER_INFO)
			title.setText(getCorpusName(SaltProjectInput));
		// or by sName of sCorpusInfo ()
		else {
			String corpusName = "//sCorpusInfo/@sName";
			XPathExpression<Attribute> PathToDirectoryName = xpathFactory.compile(
					corpusName, Filters.attribute(), null);
			Attribute DirectoryName = PathToDirectoryName.evaluateFirst(SaltProjectInput);
			title.setText(DirectoryName.getValue());
		}
		title.setAttribute(new Attribute("type", "Corpus"));
		titleStmt.addContent(title);
				
		// get editor-info
		Element editor = new Element("editor", xmlns);
		editor.setAttribute(new Attribute("role", "CorpusEditor"));
		editor.setAttribute(new Attribute("n", "1"));
		editor.addContent(new Comment("Please duplicate the editor sequence if needed. 'role' have to be set to 'CorpusEditor'. Please note, that the value of 'n' should be a sequential number."));
		Element persname = new Element("persName", xmlns);
		Element forename = new Element("forename", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element surname = new Element("surname", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element affiliation = new Element("affiliation", xmlns);
		Element orgName = new Element("orgName", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		orgName.setAttribute(new Attribute("type", IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		persname.addContent(forename);
		persname.addContent(surname);
		affiliation.addContent(orgName);
		editor.addContent(persname);
		editor.addContent(affiliation);
		titleStmt.addContent(editor);
		
		// get author-info
		Element author = new Element("author", xmlns);
		author.setAttribute(new Attribute("role", "Annotator"));
		author.setAttribute(new Attribute("n", "1"));
		author.addContent(new Comment("Please duplicate the author sequence if needed. 'role' should have one of the following values: 'Annotator', 'Infrastructure' or 'Transcription'. Please note, that the value of 'n' should be a sequential number."));
		Element persnameA = new Element("persName", xmlns);
		Element forenameA = new Element("forename", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element surnameA = new Element("surname", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element affiliationA = new Element("affiliation", xmlns);
		Element orgNameA = new Element("orgName", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		persnameA.addContent(forenameA);
		persnameA.addContent(surnameA);
		affiliationA.addContent(orgNameA);
		author.addContent(persnameA);
		author.addContent(affiliationA);
		titleStmt.addContent(author);
		fileDesc.addContent(titleStmt);
		
		//get number of token
		Element extent = new Element("extent", xmlns);
		extent.setAttribute(new Attribute("type", "Tokens"));
		// xpath-expression to extract number of token from IM-output
		String tokenAmount = "//structuralInfo/entry[@key='SToken']";
		XPathExpression<Element> PathToTokenNumber = xpathFactory.compile(
				tokenAmount, Filters.element(), null);
		Element NumberOfToken = PathToTokenNumber.evaluateFirst(SaltProjectInput);
		extent.setText(NumberOfToken.getText());
		fileDesc.addContent(extent);
		
		// set publication-info
		Element publicationStmt = new Element("publicationStmt", xmlns);
		Element authority = new Element("authority", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element idno = new Element("idno", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element availability = new Element("availability", xmlns);
		// set availability-status to "unknown" as default
		availability.setAttribute(new Attribute("status", IM2HeaderMapper.DEFAULT_VALUE_UNKNOWN));
		availability.addContent(new Comment("The availability status can be set to: 'free', 'restricted' or 'unknown'."));
		Element p = new Element("p", xmlns);
		availability.addContent(p.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		Element date = new Element("date", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		// get date of first corpus-release by terminal-request
		if(Main.GET_USER_INFO){
			FileDescCorpus.date = getDate();
		}else FileDescCorpus.date = default_date;
		
		date.setAttribute(new Attribute("when", FileDescCorpus.date));
		date.setAttribute(new Attribute("type", "CorpusRelease"));
		date.addContent(new Comment("Please copy the date sequence for each publication of your corpus. Note that 'CorpusRelease' as value of 'type' is just a suggestion. You can replace it by another String if you want."));
		publicationStmt.addContent(authority);
		publicationStmt.addContent(idno);
		publicationStmt.addContent(availability);
		publicationStmt.addContent(date);
		fileDesc.addContent(publicationStmt);
		
		// set source-description-info
		Element sourceDesc = new Element("sourceDesc", xmlns);
//		sourceDesc.addContent(list);
		Element list = new Element("list", xmlns);
		list.setAttribute(new Attribute("type", "CorpusDocument"));
		


		for (int i = 0; i < IM2HeaderMapper.DocList.size(); i++) {
			Attribute sname = IM2HeaderMapper.DocList.get(i);
			Element item = new Element("item", xmlns);
			item.setAttribute(new Attribute("n", String.valueOf(i + 1)));
			String sNameString = sname.getValue().toString();
			// check if sName starts with a number or special sign
			sNameString = CategoryListing.replaceSpecialCharacter(sNameString);
			item.setAttribute(new Attribute("corresp", sNameString));
			list.addContent(item);
		}
		sourceDesc.addContent(list);
		fileDesc.addContent(sourceDesc);
		
		return fileDesc;
	}
	
	private static String getDate() {
		System.out.println("When were the corpus (first) released? Please type in the year or the date in 'YY-MM-DD'-format.");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String releaseDate = scanner.nextLine();
		return releaseDate;
	}
	
	private static String getCorpusName(Document doc) {
		System.out.println("Please type in the name of the corpus "+ doc.getBaseURI().substring(doc.getBaseURI().lastIndexOf("/")+1) + ".");
		@SuppressWarnings("resource")
		Scanner nameScanner = new Scanner(System.in);
		String corpusName = nameScanner.nextLine();
		return corpusName;
	}

}
