package com.filenet.salesforce;

import com.filenet.salesforce.cli.tasks.CreateSalesforceRelationships;
import com.filenet.salesforce.cli.tasks.ImportFileNetDocuments;
import com.filenet.salesforce.cli.tasks.ImportSalesforceRecords;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    @FunctionName("fnsfRelatioship")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        Logger logger =  context.getLogger();
        String taskName = request.getQueryParameters().get("task");
        String message = null;
        try {
            if(taskName.equalsIgnoreCase("ImportSalesforceRecords")) {
                ImportSalesforceRecords.execute();
                message = "Request Completed";
            }
            else if(taskName.equalsIgnoreCase("ImportFileNetDocuments")) {
                ImportFileNetDocuments.execute();
                message = "Request Completed";
            }
            else if(taskName.equalsIgnoreCase("CreateSalesforceRelationships")) {
                CreateSalesforceRelationships.execute();
                message = "Request Completed";
            }
            else {
                 message = "Invalid Task Name";
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            message = "Task failed: " + e.getMessage();
        }
        logger.info("Java HTTP trigger processed a request.");

        return request.createResponseBuilder(HttpStatus.OK).body(message).build();
    }
}
