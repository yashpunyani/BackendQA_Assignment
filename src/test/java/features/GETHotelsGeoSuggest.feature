Feature: GETHotelsGeoSuggest

  Scenario Outline: 1) Verify details of Hotels and its associated locations is fetched for <queryValue> city
    Given Testing Environment
    When I pass Headers
      | Content-Type | application/json |
    And I pass queryparameter
      | query | <queryValue> |
    And I perform "GET" operation "<resourceURL>"
    Then I should get http status "<status>" in response
    And I verify response content type is JSON
    And I verify response body contains
      | hotels        |
      | hotelId       |
      | name          |
      | city          |
      | country       |
      | thumbnail_url |
      | displayType   |
      | latitude      |
      | longitude     |
      | isActive      |
      | locations     |
      | placeId       |
      | source        |
      | country       |
      | city          |
      | displayType   |
      | googleType    |
      | TypeNameEN    |
      | TypeNameAR    |
    And I verify number of records in body for jsonpath "hotels" is "5"
    And I fetch a single record using jsonPath "hotels.hotelId" and value "4026401" from response
    And I validate below jsonpaths and values in response
      | hotelId       |                                                                              4026401 |
      | name          | Splendid Etoile Hotel                                                                |
      | city          | Paris                                                                                |
      | country       | France                                                                               |
      | thumbnail_url | https://az712897.vo.msecnd.net/images/full/58a45c7d-beef-42fa-a5f3-167610ba59a9.jpeg |
      | displayType   | Hotel                                                                                |
      | latitude      |                                                                            48.875126 |
      | longitude     |                                                                             2.293814 |
      | isActive      |                                                                                    1 |
    And I fetch a single record using jsonPath "locations.placeId" and value "ChIJD7fiBh9u5kcRYJSMaMOCCwQ" from response
    And I validate below jsonpaths and values in response
      | name                  | Paris                       |
      | placeId               | ChIJD7fiBh9u5kcRYJSMaMOCCwQ |
      | source                | google                      |
      | country               | France                      |
      | city                  | Paris                       |
      | displayType           | Location                    |
      | googleType.TypeNameEN | city                        |
      | googleType.TypeNameAR | مدينة                       |
@Scenario1

    Examples: 
      | status | resourceURL         | queryValue |
      |    200 | getHotelsGeoSuggest | paris      |

  Scenario Outline: 2) Validation Scenarios for Query parameter : <scenario>
    Given Testing Environment
    When I pass Headers
      | Content-Type | application/json |
    And I pass queryparameter
      | query | <queryValue> |
    And I perform "GET" operation "<resourceURL>"
    Then I should get http status "<status>" in response
    And I verify response content type is JSON
    And I verify response body contains
      | hotels    |
      | locations |
    And I verify number of records in body for jsonpath "<jsonpath>" is "<numberOfRecords>"

    Examples: 
      | scenario          | status | resourceURL         | queryValue | jsonpath | numberOfRecords |
      | Pass invalid city |    200 | getHotelsGeoSuggest | test1334   | hotels   |               0 |
