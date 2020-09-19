# BackendQA_Assignment
- This is a Maven project used for automating REST APIs. Framewrok used is Rest Assured with BDD, Cuccumber and Java.

- \src\test\java\cucumber\Options\TestRunner.java --> Use this file to trigger the execution. To execute Scenario1 or Scenario2, use annotations @Scenario1 or @Scenario2 respectively inside tags (tags = "@Scenario2"). Send "tags" as empty or comment it, if scenarios in all the feature files needs to be executed.

- \src\test\java\features --> This is path for feature files. All the steps written in the feature file are resuable.
 1) Scenario1 --> GETHotelsGeoSuggest.feature : GET https://www.tajawal.ae/api/hotel/ahs/v2/geo-suggest
 
        - Contains 2 scenarios
        - Verifies response content type is JSON and validates one complete record from the list response using json path
                
 2) Scenario2 --> PostHotelSearch.feature : POST https://www.tajawal.ae/api/hotel/ahs/search/request
 
        - Contains 5 scenarios
        - Creates the request body dynamically at runtime using POJO
        - Values are sent dynamically form feature file
        - Http status 400 with different error messages is verified
                
- \src\test\java\resources --> This ia path  for property files:

        - config.properties : Will contain the details of baseURI to be used. If multile testing environments are there (DEV/SIT/UAT), user just needs to give  appropriate value like "environment = SIT".
        - resourceURL.properties - will contain the associated resource to be used in HTTP method. Reource name will be passed from feature file and associated value will be picked from this file.
                
- \src\main\java --> All the java classes are kept in this folder under packages pojo, stepDefinations and utilities. Before every method in this classes, appropriate comment is mentioned to explain what is this method being used for.
         

