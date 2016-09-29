import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;


public class RevisionDescDocument {
	protected static Element getRevisionDesc(Document input){
		Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

		Element revisionDesc = new Element("revisionDesc", xmlns);
		Element change = new Element("change", xmlns);
		
		change.setAttribute(new Attribute("n", "1.0"));
		//change.setAttribute(new Attribute("when", FileDescCorpus.date));
		change.setAttribute(new Attribute("who", IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		change.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		change.addContent(new Comment("Please copy the change sequence for each new release. The value of 'n' describes therefore the version number, 'when' describes the date of release, 'who' describes the person or group that made the changes/ released and 'type' describes the type of changes that have been made."));
		
		revisionDesc.addContent(change);
		return revisionDesc;
		
	}
}
