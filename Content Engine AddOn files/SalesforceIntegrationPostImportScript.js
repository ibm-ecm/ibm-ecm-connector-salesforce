importPackage(Packages.com.filenet.api.collection);
importPackage(Packages.com.filenet.api.constants);
importPackage(Packages.com.filenet.api.admin);
importPackage(Packages.com.filenet.api.core);
importPackage(Packages.com.filenet.api.exception);
importPackage(Packages.com.filenet.api.util);
importPackage(Packages.java.lang);

function PostImportScriptMethod(objectStore) 
{
    createCompositeIndex(objectStore, "SfSalesforceRelationship", "SfSalesforceOrganizationId", "SfSalesforceObjectId", "SfVersionSeriesId", java.lang.Boolean.TRUE);

    createSingleIndex(objectStore, "SfSalesforceRelationship", "SfDocument", java.lang.Boolean.FALSE);

    createSingleIndex(objectStore, "SfSalesforceRelationship", "SfVersionSeriesId", java.lang.Boolean.FALSE);
}

function createCompositeIndex(objectStore, tableName, property1, property2, property3, isUnique)
{
	var classDef = Factory.ClassDefinition.fetchInstance(objectStore, tableName, null);
	var propDefs = classDef.get_PropertyDefinitions();
	var property1ColName = "";
	var property2ColName = "";
	
   	for (i = 0; i < propDefs.size(); i++)
   	{
		var propDef = propDefs.get(i);

	    	if (propDef.get_SymbolicName().equals(property1))
	    	{
	    		var tblDef = propDef.get_TableDefinition();
	    		var colDefs = tblDef.get_ColumnDefinitions();
	
	    		for (j = 0; j < colDefs.size(); j++)
	    		{
	    			var nameColDef = colDefs.get(j);
	    			if (propDef.get_ColumnId().equals(nameColDef.get_Id()))
	    			{
	    				property1ColName = nameColDef.get_ColumnName();
        				break;
	    			}
	    		}
	    	}
   	}

   	for (i = 0; i < propDefs.size(); i++)
   	{
		var propDef = propDefs.get(i);

	    	if (propDef.get_SymbolicName().equals(property2))
	    	{
	    		var tblDef = propDef.get_TableDefinition();
	    		var colDefs = tblDef.get_ColumnDefinitions();
	
	    		for (j = 0; j < colDefs.size(); j++)
	    		{
	    			var nameColDef = colDefs.get(j);
	    			if (propDef.get_ColumnId().equals(nameColDef.get_Id()))
	    			{
	    				property2ColName = nameColDef.get_ColumnName();
        				break;
	    			}
	    		}
	    	}
   	}

  	for (i = 0; i < propDefs.size(); i++)
   	{
		var propDef = propDefs.get(i);

	    	if (propDef.get_SymbolicName().equals(property3))
	    	{
		    var tblDef = propDef.get_TableDefinition();
	    		var colDefs = tblDef.get_ColumnDefinitions();
	
	    		for (j = 0; j < colDefs.size(); j++)
	    		{
	    			var colDef = colDefs.get(j);
	    			if (propDef.get_ColumnId().equals(colDef.get_Id()))
	    			{
					indDefs = tblDef.get_IndexDefinitions();
					indDef = Factory.CmIndexDefinition.createInstance();
					indDefs.add(indDef);
					indDef.set_RequiresUniqueElements(isUnique);
					
					indCols = Factory.CmIndexedColumn.createList();
					indDef.set_IndexedColumns(indCols);

					indCol1 = Factory.CmIndexedColumn.createInstance();
					indCol1.set_ColumnName(property1ColName);
					indCols.add(indCol1);

					indCol2 = Factory.CmIndexedColumn.createInstance();
					indCol2.set_ColumnName(property2ColName);
					indCols.add(indCol2);

					indCol3 = Factory.CmIndexedColumn.createInstance();
					indCol3.set_ColumnName(colDef.get_ColumnName());
					indCols.add(indCol3);

					tblDef.save(RefreshMode.REFRESH);
	    				break;
	    			}
	    		}
	    	}
   	}
}

function createSingleIndex(objectStore, tableName, propertyName, isUnique)
{
	var classDef = Factory.ClassDefinition.fetchInstance(objectStore, tableName, null);
	var propDefs = classDef.get_PropertyDefinitions();
	var sfObjectIdColName = "";
	
   	for (i = 0; i < propDefs.size(); i++)
   	{
		var propDef = propDefs.get(i);

	    	if (propDef.get_SymbolicName().equals(propertyName))
	    	{
	    		var tblDef = propDef.get_TableDefinition();
	    		var colDefs = tblDef.get_ColumnDefinitions();
	
	    		for (j = 0; j < colDefs.size(); j++)
	    		{
	    			var colDef = colDefs.get(j);
	    			if (propDef.get_ColumnId().equals(colDef.get_Id()))
	    			{
    					indDefs = tblDef.get_IndexDefinitions();
    					indDef = Factory.CmIndexDefinition.createInstance();
    					indDefs.add(indDef);
    					indDef.set_RequiresUniqueElements(isUnique);
    					
    					indCols = Factory.CmIndexedColumn.createList();
    					indDef.set_IndexedColumns(indCols);	

    					indCol1 = Factory.CmIndexedColumn.createInstance();
    					indCol1.set_ColumnName(colDef.get_ColumnName());
    					indCols.add(indCol1);

    					tblDef.save(RefreshMode.REFRESH);
    	    				break;
	    			}
	    		}
	    	}
   	}

 }

