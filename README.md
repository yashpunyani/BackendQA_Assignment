# BackendQA_Assignment
- This is a Maven project used for automating REST APIs. Framework used is Rest Assured with BDD, Cucumber and Java.
       
       - Clone project from https://github.com/yashpunyani/BackendQA_Assignment or Download as ZIP file and import as an existing maven project in eclipse
       
       - Let all the dependencies mentioned in pom.xml be installed

- \src\test\java\cucumber\Options\TestRunner.java --> Use this file to initiate the Test Execution. To execute Scenario1 or Scenario2, use annotations @Scenario1 or @Scenario2 respectively inside tags (tags = "@Scenario2"). Send "tags" as empty or comment it, if scenarios in all the feature files needs to be executed.

- \src\test\java\features --> This is path for feature files. All the steps written in the feature file are reusable.
 1) Scenario1 --> GETHotelsGeoSuggest.feature : GET https://www.tajawal.ae/api/hotel/ahs/v2/geo-suggest
 
        - Contains 2 scenarios
        
        - Verifies response content type is JSON and validates one complete record from the list response using Json path
                
 2) Scenario2 --> PostHotelSearch.feature : POST https://www.tajawal.ae/api/hotel/ahs/search/request
 
        - Contains 5 scenarios
        
        - Creates the request body dynamically at runtime using POJO
        
        - Values are sent dynamically form feature file
        
        - Http status 400 with different error messages is verified
                
- \src\test\java\resources --> This is path for property files:

        - config.properties : Will contain the details of baseURI to be used. If multiple testing environments are there (DEV/SIT/UAT), user just needs to give appropriate value like "environment = SIT".
        
        - resourceURL.properties - will contain the associated resource to be used in HTTP method. Resource name will be passed from feature file and associated value will be picked from this file.
                
- \src\main\java --> All the java classes are kept in this folder under packages: pojo, stepDefinations and utilities. Before every method in this classes, appropriate comment is mentioned to explain what this method is being used for.
