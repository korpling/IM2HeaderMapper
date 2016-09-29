import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class EncodingDescDocument {

	final static String GI_VALUE_DEFAULT = "String";
	final static String REND_VALUE_DEFAULT = "Other";

	protected static Element getEncodingDesc(Document input) {
		Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

		Element encodingDesc = new Element("encodingDesc", xmlns);

		Element schemaSpec = new Element("schemaSpec", xmlns);

		// TODO: get grouping

		schemaSpec.setAttribute(new Attribute("ident", "AnnotationKey"));

		XPathFactory xpathFactory = XPathFactory.instance();

		// if @sName starts with a number > set a in front of @sName (or take
		// IMOutput-id instead?)
		String Layer = "//sAnnotationInfo/@sName";
		XPathExpression<Attribute> LayerPath = xpathFactory.compile(Layer,
				Filters.attribute());
		List<Attribute> LayerNames = LayerPath.evaluate(input);

		List<String> AttributeList = new ArrayList<String>();
		for (int i = 0; i < LayerNames.size(); i++) {
			Attribute LayerName = LayerNames.get(i);
			AttributeList.add(LayerName.getValue());
			Element valItem = new Element("valItem", xmlns);
			String layerString = LayerName.getValue().toString();
			// check if sName starts with a number or special sign
			String layerStringX = layerString;
			layerStringX = CategoryListing
					.replaceSpecialCharacter(layerStringX);
			valItem.setAttribute(new Attribute("ident", layerStringX));

			Attribute corresp = new Attribute("corresp", layerStringX);
			valItem.setAttribute(corresp);

			if (!CategoryListing.CategoryHashTable.containsKey(layerString)) {
				CategoryListing.setCategoryPairs(layerString);
			}
		}
		List<String> elementSpecList = CategoryListing
				.setValueClassPairs(CategoryListing.CategoryHashTable);
		for (String element : elementSpecList) {
			Element elementSpec = new Element("elementSpec", xmlns);
			elementSpec
					.addContent(new Comment(
							"If you used the tool without 'setInfo'-option copy the elementSpec sequence for each linguistical category of your corpus. 'ident' describes the category, possible values are:'Graphical', 'Lexical', 'MarkUp', 'Meta', 'Morphological', 'Other', 'Syntactical' and 'Transcription'"));
			Attribute ident = new Attribute("ident", element);
			Element valList = new Element("valList", xmlns);
			for (int i = 0; i < LayerNames.size(); i++) {

				Attribute LayerName = LayerNames.get(i);
				Element valItem = new Element("valItem", xmlns);
				String layerString = LayerName.getValue().toString();
				// check if sName starts with a number or special sign
				String layerStringX = layerString;
				layerStringX = CategoryListing
						.replaceSpecialCharacter(layerStringX);
				if (CategoryListing.CategoryHashTable.get(layerString) == element) {
					valItem.setAttribute(new Attribute("ident", layerStringX));
					Attribute corresp = new Attribute("corresp", layerStringX);
					valItem.setAttribute(corresp);
					valList.addContent(valItem);
				}

			}
			elementSpec.addContent(valList);
			if (!valList.getContent().isEmpty()) {
				schemaSpec.addContent(valList.getParentElement());
			}
			elementSpec.setAttribute(ident);

			// the following code extracts metaDataInfo
			// start
//			String Meta = "//metaDataInfo//entry/@key";
//			XPathExpression<Attribute> MetaPath = xpathFactory.compile(Meta,
//					Filters.attribute());
//			List<Attribute> MetaKeys = MetaPath.evaluate(input);
//			if (!MetaKeys.isEmpty()) {
//				Element valListMeta = new Element("valList", xmlns);
//				Element elementSpecMeta = new Element("elementSpec", xmlns);
//				elementSpecMeta.setAttribute(new Attribute("ident", "Meta"));
//
//				for (int i = 0; i < MetaKeys.size(); i++) {
//
//					String metaString = MetaKeys.get(i).getValue().toString();
//
//					Element valItemMeta = new Element("valItem", xmlns);
//					Attribute identMeta = new Attribute("ident", metaString);
//					Attribute correspMeta = new Attribute("corresp", CategoryListing.replaceSpecialCharacter(metaString));
//					valItemMeta.setAttribute(identMeta).setAttribute(
//							correspMeta);
//					valListMeta.addContent(valItemMeta);
//				}
//				elementSpecMeta.addContent(valListMeta);
//				schemaSpec.addContent(elementSpecMeta);
//			}
			// end

		}

		encodingDesc.addContent(schemaSpec);
		return encodingDesc;

	}

}
