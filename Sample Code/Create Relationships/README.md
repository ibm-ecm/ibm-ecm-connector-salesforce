# Creating Salesforce Relationships in IBM FileNet Content Manager

## Introduction

Example CE JAVA API source code is provided here to assist an Admin in associating existing IBM FileNet object store documents to their Salesforce objects.  Object store documents are associated to Salesforce objects by creating a Salesforce Relationship object in the object store where the document exists, one relationship object for each document.

All the required properties on each created relationship object must be appropriately set, such as the desired Salesforce object Id, type and URL, as well as the document version series Id and object value.  No updates are needed on the document objects.

By customizing the CreateSalesforceRelationships.java sample code to find or supply object store documents to the Relationship creation methods, and setting Relationship object properties appropriately, individual Account, Contract, Opportunity and Case Salesforce objects can be associated to specific IBM FileNet documents.

Also shown below, to assist in the customization of this sample code, is the Data Model, which shows the Classes and Properties added by the Salesforce Integration Extensions AddOn.

## Running the Sample Java API Code 

The example source code can be compiled and run in an IDE or run via command line with the provided scripts.

To compile and run the sample on the command line:

	1. Put CreateSalesforceRelationships.java and runSample.sh (or .bat) into a local directory.
	2. From your IBM FileNet installation, copy Jace.jar and log4j.jar into a "jars" subdirectory.
	3. Install a JDK version that is supported for your IBM FileNet installation.
	4. Edit CreateSalesforceRelationships.java for your connection and search and object data.
	5. To compile and execute the source code, run one of the provided scripts:

		Unix:
			runSample.sh
			
		Windows:
			runSample.bat

## Data Model

The following classes and properties are added by the Salesforce Integration Extensions AddOn.  No changes are necessary in  object store metadata.  This information is provided only to assist the developer in understanding and customizing the sample code.

### Salesforce Relationship Class

- Subclass of AbstractPersistable
- Symbolicname: SfSalesforceRelationship

| Property Name                                           | Datatype         | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|---------------------------------------------------------|------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Salesforce Organization Id (SfSalesforceOrganizationId) | Singleton String | <br>Unique id for Salesforce Organization.<br><br>Required<br>Set on Create Only<br>Len = 45                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| Salesforce Object Url (SfSalesforceObjectUrl)           | Singleton String | Url to link to Salesforce object.<br><br>Required<br>Set on Create Only<br>Len = 1000 (Salesforce limit)                                                                                                                                                                                                                                                                                                                                                                                                                          |
| Version Series Id (SfVersionSeriesId)                    | Singleton Id    | <br>Version Series Id of the ECM document. Target of relationship on ECM side between documents and relationships, also basis for lookup relationship on Salesforce side.<br><br>Required<br>Set on Create Only                                                                                                                                                                                                                                                                                                                   |
| Salesforce Object Id (SfSalesforceObjectId)              | Singleton String | <br>Salesforce object id, may be an Account, Case, Contract or Opportunity.<br><br>Target of relationship on Salesforce side, basis for lookup relationship between ECM document and Salesforce object.<br><br>Required<br>Set on Create Only<br>Len = 45                                                                                                                                                                                                                                                                         |
| Salesforce Object Type (SfSalesforceObjectType)         | Singleton String | <br>The type of Salesforce object that the ECM Documents related to.<br><br>The Salesforce Object Type is one of: Account, Case, Contract or Opportunity.<br><br>Required<br>Settable only on Create<br>Cardinality: Single<br>Len = 64                                                                                                                                                                                                                                                                                   |
| Document (SfDocument)                                    | Object          | <br>Association property whose value will be assigned to the current document version.  With the paired reflective SfSalesforceRelationships property on the Document class, deletion of the current document version will result in the cascade deletion of all associated SF relationships. <br><br>This property is also a full proxySecurity OVP.  The relationship will inherit the permissions of the document.<br><br>Required<br>Read-Write<br>Required Class: Document<br>SecurityProxyType = 1 (full)<br>AFO = false |


### Document Class

| Property Name                                           | Datatype         | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|---------------------------------------------------------|------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Salesforce Relationships (SfSalesforceRelationships) | Object | <br>Association property with reflective property set to the paired SfDocument property on the SfSalesforceRelationship class.<br><br>Through this property, deletion of the current document version will result in the deletion of all associated relationship objects.<br><br>Required<br>Read-Only<br>Required Class: SfSalesforceRelationship<br><br>Len = 45<br>                                                                                                                                                                                                                                                                                                                                                                                                                               |
