import java.io.*;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class IM2HeaderMapper extends SimpleFileVisitor<Path> {
	public static List<Attribute> DocList;
	/**
	 * Mapping for InfoModule-output to LAUDATIO-Header, generates
	 * Document-Header and Corpus-Header to the given corpus. Extracts
	 * informations from the InfoModule-output and creates "NA"-tagged elements
	 * and attribute values to non-extractable informations.
	 * 
	 * 
	 * @author viv
	 * @version 1.0
	 * 
	 * @param setInfo
	 *            : enable further manual input
	 * @throws IOException
	 *             if no xml-files were found
	 * 
	 */

	final String CORPUSROOT = "saltProjectInfo";
	final String SUBCORPUSROOT = "sCorpusInfo";
	final String DOCUMENTROOT = "sDocumentInfo";
	final String INDEX_XML = "index.xml";
	// default-flag "NA" (for every value not restricted by the schema)
	static final String NOT_SET_DEFAULT_FLAG = "NA";
	// default-flag for availability status
	static final String DEFAULT_VALUE_UNKNOWN = "unknown";
	// public static ;
	Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
	public static Element list;

	// function that traverses through every directory included in the
	// "input"-folder
	public static void SearchThroughDirectory() throws IOException {
		final FileVisitor<Path> fileVisitor = new IM2HeaderMapper();
		for (final String arg : new String[] { "input" }) {
			final Path root = Paths.get(arg);
			Files.walkFileTree(root, fileVisitor);
		}
	}

	// function that creates an "output"-folder (if not set)
	public static void MakeOutputDirectories() throws IOException {
		String root = new String(".");
		Path outputDirectory = Paths.get(root).resolve("output");
		Files.createDirectories(outputDirectory);
	}

	/**
	 * 
	 * @throws IOException
	 *             if no matching xml files were found
	 */
	public FileVisitResult preVisitDirectory(final Path dir,
			final BasicFileAttributes attrs) throws IOException {
		// seeks xml-files

		try (DirectoryStream<Path> files = Files.newDirectoryStream(dir,
				"*"+INDEX_XML)) {
			// checks for each file if the root-element is either
			// "saltProjectInfo", "sCorpusInfo" or "sDocumentInfo"

			for (Path path : files) {

				File doc = new File(path.toString());
				SAXBuilder sxbuild = new SAXBuilder();
				Document document = sxbuild.build(doc);
				Path rootDir = dir.toAbsolutePath();
				String Root = rootDir.toString();

				// if root is "saltProjectInfo" create a xml-file with the same
				// name
				// and place it into the output/CorpusHeader-path
				while (rootDir.toString().contains("input")) {
					rootDir = rootDir.getParent();
				}

				Element root = document.getRootElement();

				if (root.getName().equals(CORPUSROOT)) {

					XPathFactory xpathFactory = XPathFactory.instance();

					Element corpus = document.getRootElement();
					String corpusName = dir.toString().replaceFirst("input/", "");
					//String corpusNameBySName = corpus.getAttributeValue("sName");

					String outputCorpDir = new String(rootDir.toString()
							.concat("/output/" + corpusName + "/CorpusHeader/"));

					String outputDocDir = new String(rootDir.toString().concat(
							"/output/" + corpusName + "/DocumentHeader/"));

					File NewCorpDir = new File(outputCorpDir);
					File NewDocDir = new File(outputDocDir);

					NewCorpDir.mkdirs();
					NewDocDir.mkdirs();

					File corpusXML = new File(outputCorpDir + corpusName
							+ ".xml");
					corpusXML.createNewFile();

					String CorpusPath = "//saltProjectInfo/sCorpusInfo/@rel-location";
					XPathExpression<Attribute> CorpPath = xpathFactory.compile(
							CorpusPath, Filters.attribute());
					List<Attribute> PathToCorpus = CorpPath.evaluate(document);

					for (Attribute corp : PathToCorpus) {
						String reLocCorp = corp.getValue();
						reLocCorp = reLocCorp.replace("\\", "/");
						File docCorp = new File(Root + "/" + reLocCorp);
						SAXBuilder sxbuildCorp = new SAXBuilder();
						Document documentCorp = sxbuildCorp.build(docCorp);

						DocList = CategoryListing.setSourceDesc(document);
						GetCorpusHeader.GetCorpusXMLInput(documentCorp,
								corpusXML);
					}

					String DocumentNames = "//saltProjectInfo/sCorpusInfo//sDocumentInfo/@sName";
					// xpath-expression to extract all document-names from
					// IM-output
					XPathExpression<Attribute> DocumentNamePath = xpathFactory
							.compile(DocumentNames, Filters.attribute());
					List<Attribute> DocNames = DocumentNamePath
							.evaluate(document);

					String DocumentPath = "//sDocumentInfo/@rel-location";
					XPathExpression<Attribute> DocPath = xpathFactory.compile(
							DocumentPath, Filters.attribute());
					List<Attribute> PathToDocs = DocPath.evaluate(document);

					for (Attribute includingDoc : PathToDocs) {
						String DocName = includingDoc.getValue();
						DocName = DocName.replace("\\", "/").replaceAll(".+/", "");
						File documentXML = new File(outputDocDir + DocName
								);
						documentXML.createNewFile();
						String relLocDoc = includingDoc.getValue();
						relLocDoc = relLocDoc.replace("\\", "/");
						File docIncluded = new File(Root + "/"+ relLocDoc);
						SAXBuilder sxbuildCorp = new SAXBuilder();
						Document documentCorp = new Document();
						documentCorp = sxbuildCorp.build(docIncluded);
						GetDocumentHeader.GetDocumentXML(documentCorp,
								documentXML);

//						for (Attribute docIn : PathToDocs) {
//							
//								String relLocDoc = docIn.getValue();
//								relLocDoc = relLocDoc.replace("\\", "/");
//								File docIncluded = new File(Root + "/"
//										+ relLocDoc);
//								System.out.println(docIncluded
//										.getAbsolutePath());
//								SAXBuilder sxbuildCorp = new SAXBuilder();
//								Document documentCorp = new Document();
//								documentCorp = sxbuildCorp.build(docIncluded);
//								GetDocumentHeader.GetDocumentXML(documentCorp,
//										documentXML);
//
//						}
					}

					// if @sName starts with a number > set a in front of @sName
					// (or take
					// IMOutput-id instead?)
					// TODO: ensure that all id-unfriendly symbols in @sName get
					// replaced

					// get all documents of the corpus
					Element list = new Element("list", xmlns);
					list.setAttribute(new Attribute("type", "CorpusDocument"));

					// get all @sNames from IM-output
					for (int i = 0; i < DocNames.size(); i++) {

						Attribute sname = DocNames.get(i);
						Element item = new Element("item", xmlns);
						item.setAttribute(new Attribute("n", String
								.valueOf(i + 1)));
						String sNameString = sname.getValue().toString();
						// check if sName starts with a number or special sign
						sNameString = CategoryListing
								.replaceSpecialCharacter(sNameString);
						item.setAttribute(new Attribute("corresp", sNameString));
						list.addContent(item);
					}
				}
			}
		} catch (IOException | JDOMException e) {
			e.printStackTrace();
			throw new IOException(
					"Invalid input, no valid salt-project.xml file(s) found.");
		}
		return FileVisitResult.CONTINUE;
	}
}
