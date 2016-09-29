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



public class GetCorpusHeader{
	public static File GetCorpusXMLInput(Document SaltProjectInput, File reader) throws IOException, JDOMException{
		// set xml-namespace
		Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
		
		final String XML_MODEL_HREF = "http://korpling.german.hu-berlin.de/schemata/laudatio/teiODD_LAUDATIOCorpus_Scheme7.rng";
		final String XML_MODEL_TYPE = "application/xml";
		final String XML_MODEL_SCHEMATYPENS = "http://relaxng.org/ns/structure/1.0";
		
		ProcessingInstruction declaration = new ProcessingInstruction("xml-model", "href=\"" + XML_MODEL_HREF + "\" type=\"" + XML_MODEL_TYPE + "\" schematypens=\""+ XML_MODEL_SCHEMATYPENS +"\""); 
		
		XMLOutputter corpusOutput = new XMLOutputter(Format.getPrettyFormat());
		
		// create corpusHeader-root
		Element TEI = new Element("TEI");
		TEI.setNamespace(xmlns);
		
		
		// insert schema into new xml-file
		Document output = new Document();
		output.addContent(declaration);
		
		
		// create second xml-layer
		Element teiheader = new Element("teiHeader", xmlns);
		Element text = new Element("text", xmlns);
		teiheader.setAttribute(new Attribute("type", "CorpusHeader"));
		
		// create third xml-layer
		// create fileDesc and its child-nodes
		teiheader.addContent(FileDescCorpus.getFileDesc(SaltProjectInput));
		// create profileDesc and its child-nodes
		teiheader.addContent(ProfileDescCorpus.getProfileDesc(SaltProjectInput));
		// create encodingDesc and its child-nodes
		teiheader.addContent(EncodingDescCorpus.getEncodingDesc(SaltProjectInput));
		// create revisionDesc and its child-nodes
		teiheader.addContent(RevisionDescCorpus.getRevisionDesc(SaltProjectInput));

		
		output.setRootElement(TEI);
		output.getRootElement().addContent(teiheader);
		output.getRootElement().addContent(text);
		corpusOutput.output(output, new FileOutputStream(reader));
		corpusOutput.getXMLOutputProcessor();
		return reader;
	}

}
