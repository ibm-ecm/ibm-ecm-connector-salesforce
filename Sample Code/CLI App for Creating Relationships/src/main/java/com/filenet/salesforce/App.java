package com.filenet.salesforce;

import picocli.CommandLine;
import com.filenet.salesforce.cli.CLICallable;

public class App {
	
    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new CLICallable()).execute(args); 
        System.exit(exitCode);    	
    	
    }
}
