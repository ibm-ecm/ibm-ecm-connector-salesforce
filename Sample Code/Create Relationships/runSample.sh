#!/bin/bash

# For the following example -
# The "jars" subdirectory must contain Jace.jar and log4j.jar.
# Install a JDK version that is supported for your CPE installation.

# Compile the code:
javac -cp './jars/*' CreateSalesforceRelationships.java

# Run the sample program:
java -cp './:./jars/*' CreateSalesforceRelationships

