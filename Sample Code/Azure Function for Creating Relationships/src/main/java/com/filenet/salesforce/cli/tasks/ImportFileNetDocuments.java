package com.filenet.salesforce.cli.tasks;

import java.util.ArrayList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.PageIterator;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.core.Document;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.salesforce.config.Environment;
import com.filenet.salesforce.cpe.CEConnection;
import com.filenet.salesforce.db.FileNetDocument;

public class ImportFileNetDocuments {

    static String CPE_SEARCH = "SELECT d.Id, d.DocumentTitle,d.Name,d.creator FROM Document d";
    static int CPE_PAGE_SIZE = 100;
    
    public static void execute() throws Exception {
    	
		System.out.println("Importing FileNet Documents:");
		System.out.println(" CPE is at " + Environment.getCpeUri());
		System.out.println(" ObjectStore is " + Environment.getCpeObjecStoreName());
		
		CEConnection cec = new CEConnection();
		cec.establishConnection(Environment.getCpeUserId(), Environment.getCpeUserPassword(), "FileNetP8WSI", Environment.getCpeUri());
		ObjectStore os = cec.fetchOS(Environment.getCpeObjecStoreName());
		findAndCreateDocument(os);
		
    }

    private static void findAndCreateDocument(ObjectStore os) throws Exception
    {
        SearchSQL sqlObject = new SearchSQL(CPE_SEARCH);
        SearchScope search = new SearchScope(os);
        
        // Specify a property filter if needed
        PropertyFilter myFilter = new PropertyFilter();
        int myFilterLevel = 1;
        myFilter.setMaxRecursion(myFilterLevel);
        myFilter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null)); 
        
        Boolean continuable = Boolean.TRUE;
        
        // Fetch document objects
        DocumentSet documentSet = (DocumentSet) search.fetchObjects(sqlObject, CPE_PAGE_SIZE, myFilter, continuable);
        // Process pages of docs
        processDocumentPages(os, documentSet);
        
        System.out.println("FileNet Documents imported into the database");
    }    
    
    private static void processDocumentPages(ObjectStore os, DocumentSet documentSet) throws Exception
    {
        PageIterator pageIter= documentSet.pageIterator();
        ArrayList<Document> documents = null;
        int pageCount= 0;
        
        FileNetDocument.getConnection();
        
        while (pageIter.nextPage() == true) 
        {
            pageCount++;
            int elementCount= pageIter.getElementCount();
            System.out.println("Page count = " + pageCount + ", Object count = " + elementCount);
            
            Object[] pageObjects= pageIter.getCurrentPage();
            
            documents = new ArrayList<Document>();

            for (int index=0; index<pageObjects.length;index++)
            {
                Document doc = (Document)pageObjects[index];
                documents.add(doc);
                FileNetDocument.insert(os.get_SymbolicName(), doc.get_Id().toString(), doc.get_Name(), doc.get_Creator());
            }
            
        }
        
        FileNetDocument.close();
    }     
}
