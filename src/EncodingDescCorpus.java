import java.util.List;
//import java.util.Scanner;
import java.util.regex.Pattern;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class EncodingDescCorpus {

	// default value for annotation-values of a non-closed set of values (such
	// as lemma or tok)
	final static String GI_VALUE_DEFAULT = "String";
	// default value for annotation-classifications (set as default, if no
	// further input by the user is requested)
	final static String REND_VALUE_DEFAULT = "Other";
	// default version-flag
	final static String VERSION_FLAG = "1xxx";

	protected static Element getEncodingDesc(Document input) {
		Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
		Namespace xml = Namespace.XML_NAMESPACE;

		// create structure of encodingDesc
		Element encodingDesc = new Element("encodingDesc", xmlns);
		encodingDesc.setAttribute(new Attribute("n", "1"));
		encodingDesc.addContent(new Comment("Please copy the whole encodingDesc sequence for each application. Note, that the value of 'n' should be a sequential number."));

		Element appInfo = new Element("appInfo", xmlns);
		Element application = new Element("application", xmlns);
		application.setAttribute(new Attribute("ident",
				IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		application.setAttribute(new Attribute("version", VERSION_FLAG));
		Element label = new Element("label", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
		application.addContent(label);
		appInfo.addContent(application);

		Element projectDesc = new Element("projectDesc", xmlns);
		Element p = new Element("p", xmlns);
		Element ref = new Element("ref", xmlns);
		ref.setAttribute(new Attribute("target",
				IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		projectDesc.addContent(p.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		p.addContent(ref);

		Element editorialDecl = new Element("editorialDecl", xmlns);
		Element segmentation = new Element("segmentation", xmlns);
		Element normalization = new Element("normalization", xmlns);
		normalization.addContent(new Element("p", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		segmentation.addContent(new Element("p", xmlns)
				.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG));
		editorialDecl.addContent(segmentation);
		editorialDecl.addContent(normalization);

		Element tagsDecl = new Element("tagsDecl", xmlns);
		XPathFactory xpathFactory = XPathFactory.instance();
		// if @sName starts with a number > set a in front of @sName (or take
		// IMOutput-id instead?)
		String Layer = "//sAnnotationInfo/@sName";
		XPathExpression<Attribute> LayerPath = xpathFactory.compile(Layer,
				Filters.attribute());
		List<Attribute> LayerNames = LayerPath.evaluate(input);
		// create a namespace-element for each annotation-layer
		for (int i = 0; i < LayerNames.size(); i++) {
			Attribute LayerName = LayerNames.get(i);
			Element namespace = new Element("namespace", xmlns);
			String layerString = LayerName.getValue().toString();
			// check if sName starts with a number or special sign
			String layerStringX = layerString;
			// TODO: fix condition (check if there are special character that
			// are not allowed for ids)
			layerStringX = CategoryListing.replaceSpecialCharacter(layerStringX);
			namespace.setAttribute(new Attribute("name", layerString));
			if (Main.GET_USER_INFO) {
				CategoryListing.setCategoryPairs(layerString);
				String subSetType = CategoryListing.CategoryHashTable
						.get(layerString);// getMatchingSubset(layerString);
				namespace.setAttribute(new Attribute("rend", subSetType));
			} else {
				namespace
						.setAttribute(new Attribute("rend", REND_VALUE_DEFAULT));
			}
			Attribute id = new Attribute("id", layerStringX, xml);
			namespace.setAttribute(id);
			// get the annotation-values for each annotation-layer
			String Values = "//sAnnotationInfo[@sName='" + LayerName.getValue()
					+ "']" + "//sValue";
			XPathExpression<Element> ValuePath = xpathFactory.compile(Values,
					Filters.element());
			List<Element> ValueNames = ValuePath.evaluate(input);
			String giValue;
			// if the given annotation-layer includes more than 100 different
			// values and the values are not of type integer, set "String" as
			// annotation-value
			if (ValueNames.size() > 100
					& !(Pattern.matches("[0-9]+", ValueNames.iterator()
							.toString()))) {
				Element tagUsage = new Element("tagUsage", xmlns);
				Attribute gi = new Attribute("gi", GI_VALUE_DEFAULT);
				tagUsage.setAttribute(gi);
				namespace.addContent(tagUsage);
				// if the given annotation-layer includes more than 100
				// different
				// values and the values are of type integer, set "Int" as
				// annotation-value
			} else if (ValueNames.size() > 100
					&& (Pattern.matches("[0-9]+", ValueNames.iterator()
							.toString()))) {
				Element tagUsage = new Element("tagUsage", xmlns);
				Attribute gi = new Attribute("gi", "Int");
				tagUsage.setAttribute(gi);
				namespace.addContent(tagUsage);
			}
			// if the given annotation-layer does not include less than 101
			// annotation-values
			else {
				for (int j = 0; j < ValueNames.size(); j++) {
					Element valueName = ValueNames.get(j);
					giValue = valueName.getText();
					Element tagUsage = new Element("tagUsage", xmlns)
							.setText(IM2HeaderMapper.NOT_SET_DEFAULT_FLAG);
					Attribute gi = new Attribute("gi", giValue);
					tagUsage.setAttribute(gi);
					namespace.addContent(tagUsage);
				}
			}

			tagsDecl.addContent(namespace);
		}
		// TODO: fill in MetaData-info (not yet merged in InfoModule)

		encodingDesc.addContent(appInfo);
		encodingDesc.addContent(projectDesc);
		encodingDesc.addContent(editorialDecl);
		encodingDesc.addContent(tagsDecl);
		return encodingDesc;

	}

}
