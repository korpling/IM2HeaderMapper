import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class GetDocumentHeader {
	public static File GetDocumentXML(Document input, File documentXML)
			throws IOException, JDOMException {
		
		XMLOutputter documentOutput = new XMLOutputter(Format.getPrettyFormat());
		
		// set xml-namespace
		Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
		
		final String XML_MODEL_HREF = "http://korpling.german.hu-berlin.de/schemata/laudatio/teiODD_LAUDATIODocument_Scheme7.rng";
		final String XML_MODEL_TYPE = "application/xml";
		final String XML_MODEL_SCHEMATYPENS = "http://relaxng.org/ns/structure/1.0";
		
		ProcessingInstruction declaration = new ProcessingInstruction("xml-model", "href=\"" + XML_MODEL_HREF + "\" type=\"" + XML_MODEL_TYPE + "\" schematypens=\""+ XML_MODEL_SCHEMATYPENS +"\""); 

		// create documentHeader-root
		Element TEI = new Element("TEI");
		TEI.setNamespace(xmlns);
		
		// insert schema into new xml-file
		Document output = new Document();
		output.addContent(declaration);

		// create second xml-layer
		Element teiheader = new Element("teiHeader", xmlns);
		Element text = new Element("text", xmlns);
		teiheader.setAttribute(new Attribute("type", "DocumentHeader"));
		teiheader.setAttribute(new Attribute("style", "NA"));

		// create third xml-layer
		// create fileDesc and its child-nodes
		teiheader.addContent(FileDescDocument.getFileDesc(input));
		// create profileDesc and its child-nodes
		teiheader.addContent(ProfileDescDocument.getProfileDesc(input));
		// create encodingDesc and its child-nodes
		teiheader.addContent(EncodingDescDocument.getEncodingDesc(input));
		// create revisionDesc and its child-nodes
		teiheader.addContent(RevisionDescDocument.getRevisionDesc(input));

		output.setRootElement(TEI);
		output.getRootElement().addContent(teiheader);
		output.getRootElement().addContent(text);
		documentOutput.output(output, new FileOutputStream(documentXML));
		return documentXML;
	}

}
