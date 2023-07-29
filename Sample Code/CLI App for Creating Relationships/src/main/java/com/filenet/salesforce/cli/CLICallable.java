package com.filenet.salesforce.cli;

import java.util.concurrent.Callable;

import com.filenet.salesforce.cli.tasks.CreateSalesforceRelationships;
import com.filenet.salesforce.cli.tasks.ImportFileNetDocuments;
import com.filenet.salesforce.cli.tasks.ImportSalesforceRecords;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "FileNet Salesforce CLI", mixinStandardHelpOptions = true, version = "1.0.0", description = "A CLI application for creating Salesforce Relationship in FileNet.")
public class CLICallable implements Callable<Object> {

	enum Task {
		ImportSalesforceRecords,
		ImportFileNetDocuments,
		CreateSalesforceRelationships
	}
	
    @Option(names = {"-t", "--task"}, required = true, description = "ImportSalesforceRecords|ImportFileNetDocuments|CreateSalesforceRelationships")
    private Task task;    
    
	@Override
	public Object call() throws Exception {
		
		if(task == Task.ImportSalesforceRecords) {
			ImportSalesforceRecords.execute();
		}
		else if(task == Task.ImportFileNetDocuments) {
			ImportFileNetDocuments.execute();
		}
		else if(task == Task.CreateSalesforceRelationships) {
			CreateSalesforceRelationships.execute();
		}
		
		return null;
	}

}
