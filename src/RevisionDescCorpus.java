import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;


public class RevisionDescCorpus {
	static String DATE_FLAG = "YYYY";
	protected static Element getRevisionDesc(Document input){
		Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

		Element revisionDesc = new Element("revisionDesc", xmlns);
		Element change = new Element("change", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		
		change.setAttribute(new Attribute("n", "1.0"));
		change.setAttribute(new Attribute("when", FileDescCorpus.date));
		change.setAttribute(new Attribute("who", IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		change.setAttribute(new Attribute("type", IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		
		revisionDesc.addContent(change);
		return revisionDesc;
		
	}
}
