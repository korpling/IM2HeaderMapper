<img src="./gh-site/img/husiegel_bw.gif" align="right" />
# IM2HeaderMapper

This project provides a mapper to transform the xml-output of the [SaltInfoModule](https://github.com/korpling/pepperModules-SaltInfoModules) into the header xml format, needed to show corpus information in [Laudatio](http://www.laudatio-repository.org/laudatio/).
Therefore you first need to extract the informations of your corpus by using [Pepper](http://corpus-tools.org/pepper/) with an importer that maps your corpus format (e.g. the TEI-importer if the format of your corpus is TEI) and the SaltInfo-exporter. 
> Please note, that the output should not contain any sLayer (therefore don't use any properties that set sLayers to produce the SaltInfo-output), otherwise there could be doubled annotation layers (if an annotation layer is in more than one sLayer) in the header-xml later.
To use the IM2HeaderMapper you can either clone or download the project from gitHub and copy the output of the SaltInfo-exporter into the *input* folder. 

## Requirements
Since the here provided module is an extension of Pepper, you first need an instance of the Pepper framework to produce the needed SaltInfo-output. If you do not already have a running Pepper instance, click on the link below and download the latest stable version (not a SNAPSHOT):

> Note:
> Pepper is a Java based program, therefore you need to have at least Java 7 (JRE or JDK) on your system. You can download Java from https://www.oracle.com/java/index.html or http://openjdk.java.net/ .

To use the IM2HeaderMapper itself, which is a Java based program as well, you need at least Java 7 (JRE or JDK) on your system.


## Usage
To use this tool with the provided scripts open your terminal/ cmd, navigate to the parent directory "IM2Header" then type, dependend on your plattform:

**Windows**
```
easy2Use.bat [setInfo] 
```
(or double click on the script)

**Linux/Unix**
```
bash easy2Use.sh [setInfo]
```
An option to set few further informations by console is applicable (disabled by default). Enable this option by passing "setInfo" as argument (no further arguments allowed currently).
With this option the corpus-name is inquired (default-value: name of the saltInfo-input-directory).
In addition the date of the (first) corpus-release is inquired (default-value "1111" (to ensure validity)).
Furthermore a classification for the included annotation-layer (fix set) is enquired (default-value "Other").

# SpreadsheetImporter
At this stage, we want to explain the mapping of the SaltInfoModule output model to the laudatio header model. For that, it seems necessary to outline the two different models.

## SaltInfoModule output
The SaltInfoModule extracts all annotations (names and values as well) used in your corpus and how often they occur in a single document or in the entire corpus into xml files, that can be processed furthermore.
A possible xml output could look like the following sample:
```xml
<?xml version='1.0' encoding='UTF-8'?>
<sCorpusInfo generatedOn="2016-06-06 17:06:53" sName="CORPUS_NAME"
    id="salt:/CORPUS_NAME">
    <metaDataInfo>
        <entry key="meta-date">meta value</entry>
    </metaDataInfo>
    <structuralInfo>
        <entry key="SNode">number of nodes</entry>
        <entry key="STimeline">number of timelines</entry>
        <entry key="STextualDS">number of primary texts</entry>
        <entry key="SToken">number of token</entry>
        <entry key="SSpan">number of spans</entry>
        <entry key="SRelation">number of relations</entry>
    </structuralInfo>
    <sLayerInfo sName="Syntactic_Annotation">
        <sAnnotationInfo sName="syntactic-annotation" occurrence="number-of-syntactic-annotations">
            <sValue occurrence="number-of-syntactic-value">syntactic value</sValue>
        </sAnnotationInfo>
    </sLayerInfo>
    <sLayerInfo sName="Morphological_Annotation">
        <sAnnotationInfo sName="morphologic-annotation" occurrence="number-of-morphologic-annotations">
            <sValue occurrence="number-of-morphologic-value">morphologic value</sValue>
        </sAnnotationInfo>
    </sLayerInfo>
</sCorpusInfo>
```
## Laudatio header model
The LAUDATIO metadata is composed of three components: Information about the corpus (CorpusHeader) itself, information about all historical documents (DocumentHeader) in the corpus and about all annotation layers (PreparationHeader) and how they were annotated (e.g. which formats where used).
Since the final corpus usually don't provide any informations about the preparation steps that were used to create the corpus, the IM2HeaderMapper does not consider this part of the LAUDATIO metadata model and it has to be produced manually.
The other two components can (partly) be build from the SaltInfoModule, any values that can't get extracted from the InfoModule-output are set as "NA" by default. Those values need to get changed manually as well.

For the encoding-description in the corpus-header the version of the used application is needed, to ensure validity the default value is currently "1xxx".

The default availability of the corpus is set to "unknown".

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-model href="http://korpling.german.hu-berlin.de/schemata/laudatio/teiODD_LAUDATIOCorpus_Scheme7.rng" type="application/xml" schematypens="http://relaxng.org/ns/structure/1.0"?>
<TEI xmlns="http://www.tei-c.org/ns/1.0">
  <teiHeader type="CorpusHeader">
    <fileDesc>
      <titleStmt>
        <title type="Corpus">CORPUS_NAME</title>
        <editor role="CorpusEditor" n="1">
          <!--Please duplicate the editor sequence if needed. 'role' have to be set to 'CorpusEditor'. Please note, that the value of 'n' should be a sequential number.-->
          <persName>
            <forename>NA</forename>
            <surname>NA</surname>
          </persName>
          <affiliation>
            <orgName type="NA">NA</orgName>
          </affiliation>
        </editor>
        <author role="Annotator" n="1">
          <!--Please duplicate the author sequence if needed. 'role' should have one of the following values: 'Annotator', 'Infrastructure' or 'Transcription'. Please note, that the value of 'n' should be a sequential number.-->
          <persName>
            <forename>NA</forename>
            <surname>NA</surname>
          </persName>
          <affiliation>
            <orgName>NA</orgName>
          </affiliation>
        </author>
      </titleStmt>
      <extent type="Tokens">number of token</extent>
      <publicationStmt>
        <authority>NA</authority>
        <idno>NA</idno>
        <availability status="unknown">
          <!--The availability status can be set to: 'free', 'restricted' or 'unknown'.-->
          <p>NA</p>
        </availability>
        <date when="2016" type="CorpusRelease">
          NA
          <!--Please copy the date sequence for each publication of your corpus. Note that 'CorpusRelease' as value of 'type' is just a suggestion. You can replace it by another String if you want.-->
        </date>
      </publicationStmt>
      <sourceDesc>
        <list type="CorpusDocument">
          <item n="1" corresp="Name-of-a-document-of-the-corpus" />
          <item n="2" corresp="Name-of-another-document-of-the-corpus" />
        </list>
      </sourceDesc>
    </fileDesc>
    <profileDesc>
      <langUsage>
        <language style="Language" ident="NA">NA</language>
        <language style="LanguageArea" ident="NA">NA</language>
        <language style="LanguageType" ident="NA">NA</language>
      </langUsage>
    </profileDesc>
    <encodingDesc n="1">
      <!--Please copy the whole encodingDesc sequence for each application. Note, that the value of 'n' should be a sequential number.-->
      <appInfo>
        <application ident="NA" version="1xxx">
          <label>NA</label>
        </application>
      </appInfo>
      <projectDesc>
        <p>
          NA
          <ref target="NA" />
        </p>
      </projectDesc>
      <editorialDecl>
        <segmentation>
          <p>NA</p>
        </segmentation>
        <normalization>
          <p>NA</p>
        </normalization>
      </editorialDecl>
      <tagsDecl>
        <namespace name="syntactic-annotation" rend="Other" xml:id="syntactic-annotation">
          <tagUsage gi="syntactic value">NA</tagUsage>
        </namespace>
        <namespace name="morphologic-annotation" rend="Other" xml:id="morphologic-annotation">
          <tagUsage gi="morphologic value">NA</tagUsage>
        </namespace>
      </tagsDecl>
    </encodingDesc>
    <revisionDesc>
      <change n="1.0" when="2016" who="NA" type="NA">NA</change>
    </revisionDesc>
  </teiHeader>
  <text />
</TEI>
```
>>>>>>>>>>>>>> insert steps to show what happens

## Contribute
Since the IM2HeaderMapper is under a free license, please feel free to fork it from github and improve the mapper. If you even think that others can benefit from your improvements, don't hesitate to make a pull request, so that your changes can be merged.

## Funders
This project has been funded by the [department of corpus linguistics and morphology](https://www.linguistik.hu-berlin.de/institut/professuren/korpuslinguistik/) of the Humboldt-Universität zu Berlin. 

## License
  Copyright 2016 Humboldt-Universität zu Berlin.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

  This product furthermore includes software developed by the
      [JDOM Project](http://www.jdom.org/).

