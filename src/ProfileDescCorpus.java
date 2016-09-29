import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;


public class ProfileDescCorpus {
	static String LanguageID = "NA";
	static String LanguageAreaID = "NA";
	static String LanguageTypeID = "NA";
	protected static Element getProfileDesc(Document input){
		Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

		Element profileDesc = new Element("profileDesc", xmlns);
		Element langUsage = new Element("langUsage", xmlns);
		Element language = new Element("language", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element language2 = new Element("language", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		Element language3 = new Element("language", xmlns).setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		
		langUsage.addContent(language.setAttribute(new Attribute("style", "Language")));
		langUsage.addContent(language2.setAttribute(new Attribute("style", "LanguageArea")));
		langUsage.addContent(language3.setAttribute(new Attribute("style", "LanguageType")));
		
		language.setAttribute(new Attribute("ident", LanguageID));
		language2.setAttribute(new Attribute("ident", LanguageAreaID));
		language3.setAttribute(new Attribute("ident", LanguageTypeID));
		
		profileDesc.addContent(langUsage);
		return profileDesc;
		
	}
}
