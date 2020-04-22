/*
 * Licensed Materials - Property of IBM © Copyright IBM Corp. 2020 All Rights Reserved.
 * 
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with
 * IBM Corp.
 * 
 * DISCLAIMER OF WARRANTIES :
 * 
 * Permission is granted to copy and modify this Sample code, and to distribute modified versions provided that both the
 * copyright notice, and this permission notice and warranty disclaimer appear in all copies and modified versions.
 * 
 * THIS SAMPLE CODE IS LICENSED TO YOU AS-IS. IBM AND ITS SUPPLIERS AND LICENSORS DISCLAIM ALL WARRANTIES, EITHER
 * EXPRESS OR IMPLIED, IN SUCH SAMPLE CODE, INCLUDING THE WARRANTY OF NON-INFRINGEMENT AND THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT WILL IBM OR ITS LICENSORS OR SUPPLIERS BE LIABLE FOR
 * ANY DAMAGES ARISING OUT OF THE USE OF OR INABILITY TO USE THE SAMPLE CODE, DISTRIBUTION OF THE SAMPLE CODE, OR
 * COMBINATION OF THE SAMPLE CODE WITH ANY OTHER CODE. IN NO EVENT SHALL IBM OR ITS LICENSORS AND SUPPLIERS BE LIABLE
 * FOR ANY LOST REVENUE, LOST PROFITS OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, EVEN IF IBM OR ITS LICENSORS OR SUPPLIERS HAVE
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */

import java.util.ArrayList;
import java.util.Iterator;
import javax.security.auth.login.*;
import javax.security.auth.Subject;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.PageIterator;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.UserContext;
import com.filenet.api.util.Id;


/**
 * This is a "getting started" class for retrieving CPE documents and creating Salesforce
 *   Relationship objects for them.  The code can be customized for each use-case.
 *    
 *    In the sections below:
 *    - Specify the CPE connection properties for your system.
 *    - Specify the Salesforce values for the Salesforce object and org id, type and url.
 *       -- For adding relationships for different Salesforce objects to different sets of 
 *          of documents, the code must be customized further.
 *    - Specify the CPE document search condition, or use the underlying methods as needed.
 */
public class CreateSalesforceRelationships
{
    /*
     * CPE connection properties
     */
    static String CPE_USER_ID = "myUser";
    static String CPE_USER_PW= "";
    static String OBJECT_STORE_NAME = "myOS";
    static String CE_URI = "http://mycpe.mycpe-site.com/wsi/FNCEWS40MTOM";
    static String JAAS_STANZA_NAME = "FileNetP8WSI";

    /*
     * Salesforce properties
     */
    static String SF_OBJ_ID = "myObjId";
    static String SF_ORG_ID = "myOrgId";
    static String SF_OBJ_TYPE = "Account";
    static String SF_OBJ_URL = "myObjectUrl";
 
    /*
     * Object Store values
     */
    static String CPE_SEARCH = "SELECT d.Id, d.DocumentTitle FROM Document d WHERE d.DocumentTitle like 'myDoc%'";
    static int CPE_PAGE_SIZE = 100;

    
    private Connection conn = Factory.Connection.getConnection(CE_URI);

    
    /**
     * The main() method delegates to the run() method.
     */
    public static void main(String[] args) throws LoginException 
    {
    		System.out.println("CPE is at " + CE_URI);
    		System.out.println("ObjectStore is " + OBJECT_STORE_NAME);
     
    		CreateSalesforceRelationships cr = new CreateSalesforceRelationships();
     
    		Subject subject = UserContext.createSubject(cr.conn, CPE_USER_ID, CPE_USER_PW, JAAS_STANZA_NAME);
    		UserContext.get().pushSubject(subject);
    		try
    		{
    			cr.run();
    		}
    		finally
    		{
    			UserContext.get().popSubject();
    		}
    }

    /**
     * This method executes the actual business logic.
     */
    public void run()
    {
    	    Domain dom = Factory.Domain.getInstance(conn, null);

    	    ObjectStore os = Factory.ObjectStore.getInstance(dom, OBJECT_STORE_NAME);
    	    
    	    findDocsAndCreateRelationships(os);    	    
    }

    /**
     * This method finds documents and adds Salesforce Relationship objects for them.
     */
    private void findDocsAndCreateRelationships(ObjectStore os)
    {
        SearchSQL sqlObject = new SearchSQL(CPE_SEARCH);
        SearchScope search = new SearchScope(os);
        
        // Specify a property filter if needed
        PropertyFilter myFilter = new PropertyFilter();
        int myFilterLevel = 1;
        myFilter.setMaxRecursion(myFilterLevel);
        myFilter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null)); 
        
        Boolean continuable = new Boolean(true);
        
        // Fetch document objects
        DocumentSet documentSet = (DocumentSet) search.fetchObjects(sqlObject, CPE_PAGE_SIZE, myFilter, continuable);

        // Process pages of docs
        processDocumentPages(os, documentSet);
        
        System.out.println("\nDone.  Salesforce relationships created.");
    }

    /**
     * This method iterates over each search page of documents, creating relationships.
     */
    private void processDocumentPages(ObjectStore os, DocumentSet documentSet) 
    {
        PageIterator pageIter= documentSet.pageIterator();
        ArrayList<Document> documents = null;
        int pageCount= 0;
        
        while (pageIter.nextPage() == true) 
        {
            pageCount++;
            int elementCount= pageIter.getElementCount();
            System.out.println("\nPage count = " + pageCount + ", Object count = " + elementCount);
            
            Object[] pageObjects= pageIter.getCurrentPage();
            
            documents = new ArrayList<Document>();

            for (int index=0; index<pageObjects.length;index++)
            {
                Document doc = (Document)pageObjects[index];
                documents.add(doc);
            }
            
            createSalesforceRelationships(os, documents);
        }
    }

    /**
     * This method iterates an array of document objects to add Salesforce Relationships.
     */
    private void createSalesforceRelationships(ObjectStore os, ArrayList<Document> docs)
    {
        Iterator<Document> iter = docs.iterator();

        while (iter.hasNext())
        {
            Document doc = (Document) iter.next();
            createSalesforceRelationship(os, doc);
        }
    }

    /**
     * This method creates a Salesforce Relationship object.
     */
    private void createSalesforceRelationship(ObjectStore os, Document doc)
    {
        CmAbstractPersistable obj = Factory.CmAbstractPersistable.createInstance(os, "SfSalesforceRelationship");
    	
        obj.getProperties().putValue("SfSalesforceObjectId", SF_OBJ_ID);
        obj.getProperties().putValue("SfSalesforceOrganizationId", SF_ORG_ID);
        obj.getProperties().putValue("SfSalesforceObjectType", SF_OBJ_TYPE);
        obj.getProperties().putValue("SfSalesforceObjectUrl", SF_OBJ_URL);
        obj.getProperties().putObjectValue("SfDocument", doc);
        
        Property vsProp = doc.fetchProperty("VersionSeries", null);
        Id vsId = vsProp.getIdValue();
        obj.getProperties().putValue("SfVersionSeriesId", vsId);

        obj.save(RefreshMode.NO_REFRESH);
        
        System.out.println("Added Salesforce relationship, VersionSeriesId = " + vsId);
    }
}
