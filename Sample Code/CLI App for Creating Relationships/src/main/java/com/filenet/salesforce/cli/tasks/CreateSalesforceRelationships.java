package com.filenet.salesforce.cli.tasks;

import java.util.List;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CmAbstractPersistable;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Property;
import com.filenet.api.util.Id;
import com.filenet.salesforce.config.Environment;
import com.filenet.salesforce.cpe.CEConnection;
import com.filenet.salesforce.db.FileNetDocument;
import com.filenet.salesforce.model.SQLDocument;

public class CreateSalesforceRelationships{

    public static void execute() throws Exception {
		CEConnection cec = new CEConnection();
		cec.establishConnection(Environment.getCpeUserId(), Environment.getCpeUserPassword(), "FileNetP8WSI", Environment.getCpeUri());
		ObjectStore os = cec.fetchOS(Environment.getCpeObjecStoreName());		
		List<SQLDocument> sdl = FileNetDocument.select();
		for (SQLDocument sd: sdl) {
			Document doc = cec.fetchDocument(os, sd.getID());
			createSalesforceRelationship(os, doc, sd);
		}
		FileNetDocument.close();
		
		if(sdl.size() == 0) {
			System.out.println("No Salesforce relationship created");
		}

    }

    private static void createSalesforceRelationship(ObjectStore os, Document doc, SQLDocument sdoc)
    {
        CmAbstractPersistable obj = Factory.CmAbstractPersistable.createInstance(os, "SfSalesforceRelationship");
    	
        obj.getProperties().putValue("SfSalesforceObjectId", sdoc.getRecordId());
        obj.getProperties().putValue("SfSalesforceOrganizationId", Environment.getSalesforceOrgId());
        obj.getProperties().putValue("SfSalesforceObjectType", sdoc.getType() );
        obj.getProperties().putValue("SfSalesforceObjectUrl", sdoc.getUrl());
        obj.getProperties().putObjectValue("SfDocument", doc);
        
        Property vsProp = doc.fetchProperty("VersionSeries", null);
        Id vsId = vsProp.getIdValue();
        obj.getProperties().putValue("SfVersionSeriesId", vsId);

        obj.save(RefreshMode.NO_REFRESH);
        
        FileNetDocument.update(sdoc.getID());
        
        System.out.println("Added Salesforce relationship, VersionSeriesId = " + vsId);
    }   
}
